package co.waterflake.modules;

import co.waterflake.Main;
import co.waterflake.modules.authenticate.AuthenticateService;
import co.waterflake.modules.config.ConfigService;
import co.waterflake.modules.tunnel.TunnelService;

public class Context {
    private Main javaPluginContext = null;

    private AuthenticateService authenticateService = null;
    private TunnelService tunnelService = null;
    private ConfigService configService = null;


    public Context(Main javaPluginContext) {
        this.javaPluginContext = javaPluginContext;

        this.authenticateService = new AuthenticateService(this);
        this.tunnelService = new TunnelService(this);
        this.configService = new ConfigService(this);
    }

    public Main getJavaPluginContext() {
        return this.javaPluginContext;
    }

    public AuthenticateService getAuthenticateService() {
        return this.authenticateService;
    }

    public TunnelService getTunnelService() {
        return this.tunnelService;
    }

    public ConfigService getConfigService() {
        return this.configService;
    }
}
