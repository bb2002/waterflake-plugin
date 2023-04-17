package co.waterflake.modules.tunnel;

import co.waterflake.modules.Context;
import co.waterflake.modules.config.ConfigService;

public class TunnelService {
    private Context context = null;

    public TunnelService(Context context) {
        this.context = context;
    }

    public void onServerListenEvent(OnServerListen listener) {
        ConfigService configService = this.context.getConfigService();

        int serverPort = configService.getServerPort();

        ServerStatusListenerThread thread = new ServerStatusListenerThread(serverPort, listener);
        thread.start();
    }
}
