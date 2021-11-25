package com.gguppy.tenlives;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.logging.Logger;

public final class XLives extends JavaPlugin {
    protected static Economy econ = null;
    private static final Logger log = Logger.getLogger("Minecraft");
    public static int maxLives, maxVaults;
    public static boolean banPlayers, clearEnder, clearVaults, clearBalance, clearHomes;




    @Override
    public void onEnable() {
        //Config File
        if (!setupEconomy() ) {
            log.severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        Configuration config = new Configuration("config");
        setupConfig(config);
        registerAllEvents();
        setCommandExecutors();





    }

    @Override

    public void onDisable() {
        // Plugin shutdown logic
    }

    public void registerAllEvents(){
        getServer().getPluginManager().registerEvents(new Events(), this);
    }
    public void setCommandExecutors(){
        getCommand("setlives").setExecutor(new Commands());
    }
    public void setupConfig(Configuration config){
        config.setup();
        config.getConfigFile().addDefault("max-lives", 10);
        config.getConfigFile().addDefault("player-vaults", 7);
        config.getConfigFile().addDefault("ban-players", true);
        config.getConfigFile().addDefault("clear-balance", true);
        config.getConfigFile().addDefault("clear-enderchest", true);
        config.getConfigFile().addDefault("clear-vaults", true);
        config.getConfigFile().addDefault("clear-homes", true);
        config.getConfigFile().options().copyDefaults(true);
        config.save();

        maxLives = config.getConfigFile().getInt("max-lives");
        maxVaults = config.getConfigFile().getInt("player-vaults");
        banPlayers = config.getConfigFile().getBoolean("ban-players");
        clearBalance= config.getConfigFile().getBoolean("clear-balance");
        clearEnder = config.getConfigFile().getBoolean("clear-enderchest");
        clearVaults = config.getConfigFile().getBoolean("clear-vaults");
        clearHomes = config.getConfigFile().getBoolean("clear-homes");
    }

    public static Plugin getPlugin() {
        return Bukkit.getServer().getPluginManager().getPlugin("XLives");

    }

    public static String colorFormat(String msg){
        return ChatColor.translateAlternateColorCodes('&',msg);
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
}
