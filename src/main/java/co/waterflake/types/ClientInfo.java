package co.waterflake.types;

public class ClientInfo {
    private final String clientId;
    private final String clientSecret;

    public ClientInfo(String clientId, String clientSecret) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    public String getClientId() {
        return this.clientId;
    }

    public String getClientSecret() {
        return this.clientSecret;
    }
}
