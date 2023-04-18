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
        TunnelService tunnelService = this.context.getTunnelService();

        ConfigService configService = this.context.getConfigService();
        int maxPlayers = configService.getMaxPlayers();
        int serverPort = configService.getServerPort();

        if (maxPlayers > 75) {
            maxPlayers = 75;
            getLogger().warning("Waterflake supports up to 75 players.");
            getLogger().warning("The max-players setting in server.properties may not work properly.");
        }

//        for (int i = 0; i < maxPlayers; i++) {
//            tunnelService.spawn(serverPort);
//        }
        tunnelService.startTunneling(3);
    }

    @Override
    public void timeout() {
        getLogger().warning("Waterflake failed to connect to the game server.");
        getLogger().warning("Please make sure that the server is hosted as localhost and that the plugin can connect.");
    }
}
