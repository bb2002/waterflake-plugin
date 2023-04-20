package co.waterflake;

import co.waterflake.modules.Context;
import co.waterflake.modules.authenticate.AuthenticateService;
import co.waterflake.modules.config.ConfigService;
import co.waterflake.modules.tunnel.OnServerListen;
import co.waterflake.modules.tunnel.TunnelService;
import co.waterflake.types.ClientInfo;
import co.waterflake.types.Tunnel;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
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

    @Override
    public void onDisable() {
        TunnelService tunnelService = this.context.getTunnelService();
        if (tunnelService != null) {
            tunnelService.stopObserver();
            tunnelService.stopTunneling();
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.getPlayer().sendMessage(ChatColor.AQUA + "[WaterflakeTunnel] " + ChatColor.WHITE + "이 서버는 Waterflake 에 의해 호스팅 되고 있습니다.");
    }

    private void bootstrap() {
        AuthenticateService authenticateService = this.context.getAuthenticateService();
        ConfigService configService = this.context.getConfigService();
        TunnelService tunnelService = this.context.getTunnelService();

        ClientInfo clientInfo = configService.getClientInfo();
        if (clientInfo.getClientId().isEmpty() || clientInfo.getClientSecret().isEmpty()) {
            getLogger().warning(Color.RED + "config.yml 파일에 Waterflake Client 인증 정보를 입력해주세요.");
            return;
        }

        Tunnel tunnel = authenticateService.login(clientInfo);
        if (tunnel == null) {
            getLogger().warning("Waterflake 에 로그인 할 수 없습니다.");
            getLogger().warning("config.yml 파일의 인증 정보가 정확한지 다시 확인해주세요.");
            return;
        }

        getLogger().info("로그인 되었습니다. 서버 활성화를 기다리는 중...");
        tunnelService.onServerListenEvent(this);
    }

    @Override
    public void successful() {
        TunnelService tunnelService = this.context.getTunnelService();
        Tunnel tunnel = this.context.getAuthenticateService().getCurrentTunnel();

        ConfigService configService = this.context.getConfigService();
        int maxPlayers = configService.getMaxPlayers();

        if (maxPlayers > 75) {
            maxPlayers = 75;
            getLogger().warning("Waterflake 는 최대 75명의 플레이어를 지원합니다.");
            getLogger().warning("server.properties 의 max-players 설정이 제대로 동작하지 않을 수 있습니다.");
        }

        tunnelService.startObserver();
        tunnelService.startTunneling(maxPlayers);

        getLogger().info("터널링 성공! 연결 대상 도메인 -> " + tunnel.subDomain + "." + tunnel.rootDomain);
    }

    @Override
    public void timeout() {
        getLogger().warning("Waterflake 가 게임 서버에 연결하는데 너무 오랜 시간이 걸렸습니다.");
        getLogger().warning("ip-address=127.0.0.1 옵션이 도움이 될 수 있습니다. 플러그인이 로컬 서버에 연결 할 수 있는지 네트워크 설정을 확인하세요.");
    }
}
