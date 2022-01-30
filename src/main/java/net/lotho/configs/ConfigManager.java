package net.lotho.configs;

import net.lotho.Azazel;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class ConfigManager {

    static Azazel instance;
    public static YamlConfiguration getConfigFile;
    public static File configFile;

    public ConfigManager(Azazel instance) {
        ConfigManager.instance = instance;

        if (!instance.getDataFolder().exists()) {
            instance.getDataFolder().mkdir();
        }

        configFile = new File(instance.getDataFolder(), "config.yml");

        if (!configFile.exists()) {
            try (InputStream inputStream = instance.getResource("config.yml")) {
                if (inputStream != null) {
                    Files.copy(inputStream, configFile.toPath());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        getConfigFile = YamlConfiguration.loadConfiguration(configFile);
    }

    public void saveConfigFile() {
        try {
            getConfigFile.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public YamlConfiguration getConfigFile() {
        return getConfigFile;
    }

}
