package co.waterflake;

import co.waterflake.modules.AuthenticateModule;
import co.waterflake.modules.ConfigModule;
import co.waterflake.types.ClientInfo;
import co.waterflake.types.Tunnel;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class WaterflakeClient extends JavaPlugin implements Listener {
    private AuthenticateModule authenticateModule = null;
    private ConfigModule configModule = null;

    @Override
    public void onLoad() {
        this.loadModules();
    }

    @Override
    public void onEnable() {
        // config.yml 파일에 인증 정보가 있는지 확인
        ClientInfo clientInfo = this.configModule.getClientInfo();
        if (clientInfo.getClientId().isEmpty() || clientInfo.getClientSecret().isEmpty()) {
            getLogger().warning("Please enter your credentials in the config.yml file.");
            return;
        }

        // 터널링 서버에 로그인
        Tunnel tunnel = this.authenticateModule.login(clientInfo);
        if (tunnel == null) {
            getLogger().warning("Unable to log in to Waterflake.");
            getLogger().warning("Make sure that the client information in the config.yml file is correct.");
        } else {
            getLogger().info("You are logged in to Waterflake.");
            getLogger().info("The server broadcasts to " + tunnel.subDomain + "." + tunnel.rootDomain);
        }

        // TODO Tunnel 을 생성하고 커넥션을 생성
    }

    private void loadModules() {
        this.authenticateModule = AuthenticateModule.getInstance();
        this.configModule = ConfigModule.getInstance(this);
    }
}
