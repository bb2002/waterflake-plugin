package co.waterflake.modules.tunnel;

import co.waterflake.modules.Context;
import co.waterflake.modules.authenticate.AuthenticateService;
import co.waterflake.modules.config.ConfigService;
import co.waterflake.modules.tunnel.threads.ServerStatusListenerThread;
import co.waterflake.modules.tunnel.threads.TunnelObserverThread;
import co.waterflake.modules.tunnel.threads.TunnelThread;
import co.waterflake.modules.tunnel.types.TunnelThreadParams;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class TunnelService {
    private Context context = null;
    private ConcurrentHashMap<Integer, TunnelThread> tunnelThreads = new ConcurrentHashMap<>();
    private int nextConnectionId = 0;
    private TunnelObserverThread tunnelObserverThread = null;

    public TunnelService(Context context) {
        this.context = context;
    }

    public void onServerListenEvent(OnServerListen listener) {
        ConfigService configService = this.context.getConfigService();

        int serverPort = configService.getServerPort();

        ServerStatusListenerThread thread = new ServerStatusListenerThread(serverPort, listener);
        thread.start();
    }

    public void startTunneling(int tunnelCount) {
for (int i = 0; i < tunnelCount; i++) {
            try {
                Thread.sleep(100);
            } catch(Exception ex) {}

            this.context.getJavaPluginContext().getLogger().info("Spawn tunnel... ID: " + (i + 1));
            this.spawn();
        }
    }

    public void stopTunneling() {
        for (Integer tunnelId : this.tunnelThreads.keySet()) {
            this.tunnelThreads.get(tunnelId).stop();
        }

        this.tunnelThreads.clear();
    }

    public void startObserver() {
        if (this.tunnelObserverThread == null || !this.tunnelObserverThread.isAlive()) {
            this.context.getJavaPluginContext().getLogger().info("TunnelObserver 서비스를 시작했습니다.");
            this.tunnelObserverThread = new TunnelObserverThread(this);
            this.tunnelObserverThread.start();
        }
    }

    public void stopObserver() {
        this.tunnelObserverThread.interrupt();
    }

    public void spawn() {
        AuthenticateService authenticateService = this.context.getAuthenticateService();
        ConfigService configService = this.context.getConfigService();

        TunnelThreadParams params = new TunnelThreadParams(
                "127.0.0.1",
                configService.getServerPort(),
                authenticateService.getCurrentTunnel().region.SRVTarget,
                authenticateService.getCurrentTunnel().inPort,
                configService.getClientInfo().getClientId(),
                configService.getClientInfo().getClientSecret()
        );

        try {
            TunnelThread thread = new TunnelThread(params);
            thread.start();
            this.tunnelThreads.put(this.getNextConnectionId(), thread);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public int getNextConnectionId() {
        return this.nextConnectionId++;
    }

    public ConcurrentHashMap<Integer, TunnelThread> getTunnelThreads() {
        return this.tunnelThreads;
    }
}
