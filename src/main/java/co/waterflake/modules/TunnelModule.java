package co.waterflake.modules;

import co.waterflake.WaterflakeClient;
import co.waterflake.interfaces.TunnelModuleListener;

import java.net.InetSocketAddress;
import java.net.Socket;

public class TunnelModule {
    private WaterflakeClient context = null;
    private TunnelModuleListener listener = null;
    private static TunnelModule instance = null;

    private TunnelModule(WaterflakeClient context) {
        this.context = context;
    }

    public void setTunnelModuleListener(TunnelModuleListener listener) {
        this.listener = listener;
    }

    public TunnelModuleListener getTunnelModuleListener() {
        return this.listener;
    }

    public void waitForServerListening(int port) {
        ServerListeningThread thread = new ServerListeningThread(port);
        thread.start();
    }

    public static TunnelModule getInstance(WaterflakeClient context) {
        if (TunnelModule.instance == null) {
            TunnelModule.instance = new TunnelModule(context);
        }

        return TunnelModule.instance;
    }
}

class ServerListeningThread extends Thread {
    private int port;

    public ServerListeningThread(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Socket socket = new Socket();
                socket.connect(new InetSocketAddress("127.0.0.1", this.port), 2000);
                socket.close();
                break;
            } catch (Exception e) {}

            try {
                Thread.sleep(1000);
            } catch (Exception e) {}
        }

        TunnelModule tunnelModule = TunnelModule.getInstance(null);
        TunnelModuleListener listener = tunnelModule.getTunnelModuleListener();
        if (listener != null) {
            listener.onServerListening();
        }
    }
}
