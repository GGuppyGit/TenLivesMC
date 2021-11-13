package com.gguppy.tenlives;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import javax.imageio.IIOException;
import java.io.File;
import java.io.IOException;

public class Events implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        try {
            Player p = e.getPlayer();
            File f = new File(TenLives.getPlugin().getDataFolder().getAbsolutePath() + "/userdata/", p.getUniqueId() + ".yml");
            FileConfiguration c = YamlConfiguration.loadConfiguration(f);
            if(!f.exists()){
                c.set("deaths", 1);
                p.setMaxHealth(2);
                c.save(f);
            }

        } catch (IOException err){
            System.out.println("TenLives IOException. Report this.");
        }
        
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) throws IOException {
        Player p = e.getEntity().getPlayer();
        File f = new File(TenLives.getPlugin().getDataFolder().getAbsolutePath() + "/userdata/", p.getUniqueId() + ".yml");
        FileConfiguration c = YamlConfiguration.loadConfiguration(f);
        int deaths = c.getInt("deaths");
        deaths++;
        if(deaths > 10){
            ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
            String command = "eco set " + p.getName() + " 0";
            Bukkit.dispatchCommand(console, command);
            for(int i = 0; i < 7; i++){
                command = "pv reset " + i + " " + p.getName();
                Bukkit.dispatchCommand(console, command);
            }
            p.setMaxHealth(2);
            c.set("deaths", 1);
            c.save(f);
            p.spigot().respawn();
            command = "tempban " + p.getName() + " 1d " + TenLives.colorFormat("&4&lYou're out of lives!");
            Bukkit.dispatchCommand(console, command);



        } else {
            p.setMaxHealth(deaths * 2);
            c.set("deaths", deaths);
            c.save(f);
        }


    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e){
        Player p = e.getPlayer();
        File f = new File(TenLives.getPlugin().getDataFolder().getAbsolutePath() + "/userdata/", p.getUniqueId() + ".yml");
        FileConfiguration c = YamlConfiguration.loadConfiguration(f);
        int deaths = c.getInt("deaths");
        int lives = 11 - deaths;
        p.sendTitle(TenLives.colorFormat("&4&lYou died!"), TenLives.colorFormat("&c" + lives + " lives left."), 1, 30, 1);
    }

}
