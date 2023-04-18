package co.waterflake.modules.tunnel.threads;

import co.waterflake.modules.tunnel.types.TunnelThreadParams;
import com.google.gson.JsonObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class TunnelThread {
    private final TunnelThreadParams params;
    private Socket localSocket;
    private Socket remoteSocket;

    public TunnelThread(TunnelThreadParams params) {
        this.params = params;
    }

    public void start() throws IOException {
        this.localSocket = new Socket(this.params.getLocalServerHost(), this.params.getLocalServerPort());
        this.remoteSocket = new Socket(this.params.getRemoteServerHost(), this.params.getRemoteServerPort());
        
        // Waterflake 터널링 서버에 인증
        this.authenticate(this.remoteSocket);

        // 인증이 완료되면 터널 시작
        (new Thread(new RunnableImpl(this.localSocket, this.remoteSocket))).start();
        (new Thread(new RunnableImpl(this.remoteSocket, this.localSocket))).start();
    }

    public boolean isSocketClosed() {
        return this.localSocket.isClosed() || this.remoteSocket.isClosed();
    }

    private void authenticate(Socket remoteSocket) throws IOException {
        OutputStream output = remoteSocket.getOutputStream();

        JsonObject authObject = new JsonObject();
        authObject.addProperty("clientId", this.params.getClientId());
        authObject.addProperty("clientSecret", this.params.getClientSecret());

        output.write(authObject.toString().getBytes());
    }
}

class RunnableImpl implements Runnable {
    private final Socket inSocket;
    private final Socket outSocket;

    public RunnableImpl(Socket inSocket, Socket outSocket) {
        this.inSocket = inSocket;
        this.outSocket = outSocket;
    }

    @Override
    public void run() {
        System.out.println("start RUN()");
        try {
            InputStream input = inSocket.getInputStream();
            OutputStream output = outSocket.getOutputStream();

            byte[] buffer = new byte[1024];
            int length;
            while ((length = input.read(buffer)) > 0) {
                output.write(buffer, 0, length);
                System.out.println("Write byte " + length);
            }
        } catch (Exception ignored) {
            ignored.printStackTrace();
        } finally {
            try {
                inSocket.close();
            } catch (IOException ignored){}
            try {
                outSocket.close();
            } catch (IOException ignored){}
        }
        System.out.println("end RUN()");
    }
}