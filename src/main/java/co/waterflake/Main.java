package co.waterflake;

import co.waterflake.modules.Context;
import co.waterflake.modules.authenticate.AuthenticateService;
import co.waterflake.modules.config.ConfigService;
import co.waterflake.modules.tunnel.OnServerListen;
import co.waterflake.modules.tunnel.TunnelService;
import co.waterflake.types.ClientInfo;
import co.waterflake.types.Tunnel;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener, OnServerListen {
    private Context context = null;

    @Override
    public void onLoad() {
        this.context = new Context(this);
    }

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        this.bootstrap();
    }

    private void bootstrap() {
        AuthenticateService authenticateService = this.context.getAuthenticateService();
        ConfigService configService = this.context.getConfigService();
        TunnelService tunnelService = this.context.getTunnelService();

        ClientInfo clientInfo = configService.getClientInfo();
        if (clientInfo.getClientId().isEmpty() || clientInfo.getClientSecret().isEmpty()) {
            getLogger().warning("Please enter your credentials in the config.yml file.");
            return;
        }

        Tunnel tunnel = authenticateService.login(clientInfo);
        if (tunnel == null) {
            getLogger().warning("Unable to log in to Waterflake.");
            getLogger().warning("Make sure that the client information in the config.yml file is correct.");
            return;
        }

        getLogger().info("Logged in. Wait for the server to start listening...");
        tunnelService.onServerListenEvent(this);
    }

    @Override
    public void successful() {

    }

    @Override
    public void timeout() {

    }
}
