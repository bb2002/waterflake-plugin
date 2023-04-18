package co.waterflake.modules.tunnel.types;

public class TunnelThreadParams {
    private final String localServerHost;
    private final int localServerPort;
    private final String remoteServerHost;
    private final int remoteServerPort;
    private final String clientId;
    private final String clientSecret;

    public TunnelThreadParams(String localServerHost, int localServerPort, String remoteServerHost, int remoteServerPort,  String clientId, String clientSecret) {
        this.localServerHost = localServerHost;
        this.localServerPort = localServerPort;
        this.remoteServerHost = remoteServerHost;
        this.remoteServerPort = remoteServerPort;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    public String getLocalServerHost() {
        return localServerHost;
    }


    public int getLocalServerPort() {
        return localServerPort;
    }

    public String getRemoteServerHost() {
        return remoteServerHost;
    }

    public int getRemoteServerPort() {
        return remoteServerPort;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }
}
