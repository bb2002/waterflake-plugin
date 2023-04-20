package co.waterflake.modules.authenticate;

import co.waterflake.constants.WaterflakeAPI;
import co.waterflake.libs.WaterflakeHttp;
import co.waterflake.modules.Context;
import co.waterflake.types.ClientInfo;
import co.waterflake.types.Region;
import co.waterflake.types.Tunnel;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.Response;

import java.util.Objects;

public class AuthenticateService {
    private Context context = null;

    private Tunnel currentTunnel = null;

    public AuthenticateService(Context context) {
        this.context = context;
    }

    public Tunnel login(ClientInfo clientInfo) {
        try {
            String url = WaterflakeAPI.TUNNEL_LOGIN + "/"+  clientInfo.getClientId();
            Response response = WaterflakeHttp.execute(WaterflakeHttp.post(url, "", clientInfo));

            if (response.isSuccessful()) {
                String body = Objects.requireNonNull(response.body()).string();
                this.currentTunnel = parsingLoginResult(body);
            }
        } catch(Exception e) {
            this.currentTunnel = null;
            e.printStackTrace();
        }

        return this.currentTunnel;
    }

    public boolean isAuthenticated() {
        return this.currentTunnel != null;
    }

    public Tunnel getCurrentTunnel() {
        return this.currentTunnel;
    }

    private Tunnel parsingLoginResult(String body) {
        JsonParser parser = new JsonParser();

        JsonObject jsonObject = parser.parse(body).getAsJsonObject();
        JsonObject regionObject = jsonObject.getAsJsonObject("region");

        Region region = new Region();
        region.name = regionObject.get("name").getAsString();
        region.SRVTarget = regionObject.get("SRVTarget").getAsString();

        Tunnel tunnel = new Tunnel();
        tunnel._id = jsonObject.get("_id").getAsInt();
        tunnel.name = jsonObject.get("name").getAsString();
        tunnel.subDomain = jsonObject.get("subDomain").getAsString();
        tunnel.rootDomain = jsonObject.get("rootDomain").getAsString();
        tunnel.inPort = jsonObject.get("inPort").getAsInt();
        tunnel.outPort = jsonObject.get("outPort").getAsInt();
        tunnel.region = region;

        return tunnel;
    }
}
