package co.waterflake;

import co.waterflake.interfaces.TunnelModuleListener;
import co.waterflake.modules.AuthenticateModule;
import co.waterflake.modules.ConfigModule;
import co.waterflake.modules.TunnelModule;
import co.waterflake.types.ClientInfo;
import co.waterflake.types.Tunnel;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import static co.waterflake.libs.WaterflakeSettings.MAX_PLAYERS;

public class WaterflakeClient extends JavaPlugin implements Listener, TunnelModuleListener {
    private AuthenticateModule authenticateModule = null;
    private ConfigModule configModule = null;
    private TunnelModule tunnelModule = null;

    @Override
    public void onLoad() {
        this.loadModules();
    }

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);

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
            return;
        }

        getLogger().info("You are logged in to Waterflake.");

        int serverPort = Bukkit.getPort();
        this.tunnelModule.waitForServerListening(serverPort);
    }

    @Override
    public void onServerListening() {
        int maxPlayers = this.configModule.getMaxPlayers();
        if (maxPlayers > MAX_PLAYERS) {
            getLogger().info("Waterflake supports up to 75 players.");
            getLogger().info("The max-players setting in server.properties may not work properly.");
            maxPlayers = MAX_PLAYERS;
        }

        // getLogger().info("The server broadcasts to " + tunnel.subDomain + "." + tunnel.rootDomain);
        System.out.println("onServerListeningonServerListeningonServerListening");
    }

    private void loadModules() {
        this.authenticateModule = AuthenticateModule.getInstance();
        this.configModule = ConfigModule.getInstance(this);
        this.tunnelModule = TunnelModule.getInstance(this);
        this.tunnelModule.setTunnelModuleListener(this);
    }
}

//class TestThread extends Thread {
//    @Override
//    public void run() {
//        while (true) {
//            try {
//                Server server = Bukkit.getServer();
//                System.out.println("SERVER: " + server.getOnlineMode());
//                Thread.sleep(1000);
//            } catch(Exception e) {
//                e.printStackTrace();
//            }
//
//        }
//    }
//}
