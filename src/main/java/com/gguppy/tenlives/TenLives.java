package com.gguppy.tenlives;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class TenLives extends JavaPlugin {

    public static Plugin getPlugin() {
        return Bukkit.getServer().getPluginManager().getPlugin("TenLives");

    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(new Events(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static String colorFormat(String msg){
        return ChatColor.translateAlternateColorCodes('&',msg);
    }
}
