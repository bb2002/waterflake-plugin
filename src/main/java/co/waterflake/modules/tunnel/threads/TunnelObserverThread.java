package co.waterflake.modules.tunnel.threads;

import co.waterflake.modules.tunnel.TunnelService;

import java.util.concurrent.ConcurrentHashMap;

public class TunnelObserverThread extends Thread {
    private final TunnelService tunnelService;

    public TunnelObserverThread(TunnelService tunnelService) {
        this.tunnelService = tunnelService;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(100);
                ConcurrentHashMap<Integer, TunnelThread> threads = this.tunnelService.getTunnelThreads();

                for (Integer tunnelId : threads.keySet()) {
                    TunnelThread tunnelThread = threads.get(tunnelId);
                    if (tunnelThread.isSocketClosed()) {
                        threads.remove(tunnelId);
                        this.tunnelService.spawn();
                    }
                }
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
