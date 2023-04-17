package co.waterflake.modules.tunnel;

import java.net.InetSocketAddress;
import java.net.Socket;

public class ServerStatusListenerThread extends Thread {
    private final int serverPort;
    private final OnServerListen listener;
    private int maxTryCount = 300;

    public ServerStatusListenerThread(int serverPort, OnServerListen listener) {
        this.serverPort = serverPort;
        this.listener = listener;
    }

    public void setMaxTryCount(int maxTryCount) {
        this.maxTryCount = maxTryCount;
    }

    @Override
    public void run() {
        boolean isSuccessful = false;

        for (int i = 0; i < this.maxTryCount; ++i) {
            try {
                Socket socket = new Socket();
                socket.connect(new InetSocketAddress("127.0.0.1", this.serverPort), 1000);
                socket.close();
                isSuccessful = true;
                break;
            } catch (Exception ignored) {}

            try {
                Thread.sleep(1000);
            } catch (Exception ignored) {}
        }

        if (isSuccessful) {
            this.listener.successful();
        } else {
            this.listener.timeout();
        }
    }
}
