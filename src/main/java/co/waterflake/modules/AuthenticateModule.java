package co.waterflake.modules;

import co.waterflake.libs.WaterflakeAPI;
import co.waterflake.libs.WaterflakeHttp;
import co.waterflake.types.ClientInfo;
import co.waterflake.types.Tunnel;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.*;

import java.util.Objects;

public class AuthenticateModule {
    private static AuthenticateModule instance = null;
    private Tunnel currentTunnel = null;

    private AuthenticateModule() {}

    public static AuthenticateModule getInstance() {
        if (AuthenticateModule.instance == null) {
            AuthenticateModule.instance = new AuthenticateModule();
        }

        return AuthenticateModule.instance;
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
        JsonObject jsonObject = JsonParser.parseString(body).getAsJsonObject();

        Tunnel tunnel = new Tunnel();
        tunnel._id = jsonObject.get("_id").getAsInt();
        tunnel.name = jsonObject.get("name").getAsString();
        tunnel.subDomain = jsonObject.get("subDomain").getAsString();
        tunnel.rootDomain = jsonObject.get("rootDomain").getAsString();
        tunnel.inPort = jsonObject.get("inPort").getAsInt();
        tunnel.outPort = jsonObject.get("outPort").getAsInt();

        return tunnel;
    }
}
