package me.minigames.utils.file;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

/**
 * Created by Digital on 21/01/2018.
 *
 * YMLFile file = new YMLFile("data.yml", Plugin instance);
 *
 */
public class YMLFile {

    public String configname;
    private FileConfiguration customConfig = null;
    private File customConfigFile = null;

    private Plugin instance;

    public YMLFile(String filename, Plugin instance){
        this.configname = filename;
        this.instance = instance;
    }

    public void reloadCustomConfig(){
        if (customConfigFile == null){
            customConfigFile = new File(instance.getDataFolder(), configname);
        }

        customConfig = YamlConfiguration.loadConfiguration(customConfigFile);
        InputStream defConfigStream = instance.getResource(configname);
        if(defConfigStream != null){
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(customConfigFile);
            customConfig.setDefaults(defConfig);
        }
    }

    public FileConfiguration getCustomConfig(){
        if(customConfig == null){
            reloadCustomConfig();
        }
        return customConfig;
    }

    public void saveCustomConfig(){
        if(customConfig == null || customConfigFile == null){
            return;
        }
        try{
            getCustomConfig().save(customConfigFile);
        } catch (IOException ex){
            instance.getLogger().log(Level.SEVERE, "Could not save config to " + customConfigFile, ex);
        }
    }

    public List<String> getAsPathList(final String path) {
        final List<String> list=new ArrayList<String>();
        if(this.getCustomConfig().contains(path)&&this.getCustomConfig().isConfigurationSection(path)){
            final Set<String> keys=this.getCustomConfig().getConfigurationSection(path).getKeys(false);
            if(keys.size()>0){
                final Object[] key=keys.toArray();
                for(final Object element:key){
                    list.add((String) element);
                }
            }
        }
        return list;
    }

}
