package co.waterflake.modules;

import co.waterflake.WaterflakeClient;
import co.waterflake.types.ClientInfo;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;

public class ConfigModule {
    private WaterflakeClient context = null;
    private static ConfigModule instance = null;

    private ConfigModule(WaterflakeClient context) {
        this.context = context;

        File configFile = new File(this.context.getDataFolder(), "config.yml");
        if (configFile.length() == 0) {
            this.context.saveDefaultConfig();
        }
    }

    public ClientInfo getClientInfo() {
        FileConfiguration config = this.context.getConfig();
        String clientId = config.getString("Authenticate.ClientId");
        String clientSecret = config.getString("Authenticate.ClientSecret");

        return new ClientInfo(clientId, clientSecret);
    }

    public int getMaxPlayers() {
        return Bukkit.getServer().getMaxPlayers();
    }

    public static ConfigModule getInstance(WaterflakeClient context) {
        if (ConfigModule.instance == null) {
            ConfigModule.instance = new ConfigModule(context);
        }

        return ConfigModule.instance;
    }
}
