package com.gguppy.tenlives;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Configuration {

    private File file;
    private FileConfiguration configFile;
    private String fileName;

    public Configuration(String fileName){
        this.fileName = fileName;
    }

    public void setup(){
        file = new File(Bukkit.getServer().getPluginManager().getPlugin("XLives").getDataFolder().getAbsolutePath(), fileName + ".yml");

        if(!file.exists()){
            try{
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        configFile = YamlConfiguration.loadConfiguration(file);
    }
    public FileConfiguration getConfigFile(){
        return configFile;
    }

    public void save(){
        try {
            configFile.save(file);
        } catch (IOException e) {
            System.out.println("Couldn't save XLives config file.");
        }
    }

    public void reload(){
        configFile = YamlConfiguration.loadConfiguration(file);
    }

}
