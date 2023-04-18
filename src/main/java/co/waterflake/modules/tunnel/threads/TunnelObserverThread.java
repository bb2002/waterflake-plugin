package co.waterflake.modules.tunnel.threads;

import co.waterflake.modules.tunnel.TunnelService;

public class TunnelObserverThread extends Thread {
    private TunnelService tunnelService;

    public TunnelObserverThread(TunnelService tunnelService) {
        this.tunnelService = tunnelService;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {}
    }
}
