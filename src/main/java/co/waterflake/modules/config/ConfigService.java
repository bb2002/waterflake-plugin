package co.waterflake.modules.config;

import co.waterflake.Main;
import co.waterflake.modules.Context;
import co.waterflake.types.ClientInfo;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;

public class ConfigService {
    private Context context = null;

    public ConfigService(Context context) {
        this.context = context;

        Main javaPluginContext = this.context.getJavaPluginContext();
        File configFile = new File(javaPluginContext.getDataFolder(), "config.yml");
        if (configFile.length() == 0) {
            javaPluginContext.saveDefaultConfig();
        }
    }

    public ClientInfo getClientInfo() {
        Main javaPluginContext = this.context.getJavaPluginContext();
        FileConfiguration config = javaPluginContext.getConfig();
        String clientId = config.getString("Authenticate.ClientId");
        String clientSecret = config.getString("Authenticate.ClientSecret");

        return new ClientInfo(clientId, clientSecret);
    }

    public int getMaxPlayers() {
        return context.getJavaPluginContext().getServer().getMaxPlayers();
    }

    public int getServerPort() {
        return context.getJavaPluginContext().getServer().getPort();
    }
}
