package net.sunken.bungeecord.config;

import com.google.common.io.ByteStreams;
import lombok.Getter;
import net.sunken.bungeecord.BungeeMain;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;

import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import net.sunken.common.Common;

public class ConfigHandler {

    private BungeeMain plugin;

    @Getter
    private String configName;

    @Getter
    private Configuration config;

    public ConfigHandler(BungeeMain instance, String configName){
        this.plugin = instance;
        this.configName = configName;
        this.loadConfig();
    }

    public void loadConfig(){
        File file = new File(plugin.getDataFolder(), configName);
        Configuration configuration = null;

        try {
            configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
        } catch (IOException e) {
            Common.getLogger().log(Level.INFO, "Could not load config: " + configName);
            e.printStackTrace();
        }

        this.config = configuration;
    }

    public void saveConfig(Configuration c){
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(c, new File(plugin.getDataFolder(), configName));
        } catch (IOException e) {
            Common.getLogger().log(Level.INFO, "Could not save config: " + configName);
            e.printStackTrace();
        }
    }

    public void saveDefaultConfig(){
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }

        File configFile = new File(plugin.getDataFolder(), configName);
        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
                try (InputStream is = plugin.getResourceAsStream(configName);
                     OutputStream os = new FileOutputStream(configFile)) {
                    ByteStreams.copy(is, os);
                }
            } catch (IOException e) {
                throw new RuntimeException("Unable to create configuration file", e);
            }
        }
    }
}