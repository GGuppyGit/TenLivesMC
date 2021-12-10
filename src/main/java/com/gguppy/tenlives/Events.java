package com.gguppy.tenlives;

import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.File;
import java.io.IOException;

public class Events implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        try {
            Player p = e.getPlayer();
            File f = new File(XLives.getPlugin().getDataFolder().getAbsolutePath() + "/userdata/", p.getUniqueId() + ".yml");
            FileConfiguration c = YamlConfiguration.loadConfiguration(f);
            if(!f.exists()){
                c.set("deaths", 1);
                p.setMaxHealth(2);
                c.save(f);
            }

        } catch (IOException err){
            System.out.println("XLives IOException. Report this.");
        }
        
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) throws IOException {
        Player p = e.getEntity().getPlayer();
        File f = new File(XLives.getPlugin().getDataFolder().getAbsolutePath() + "/userdata/", p.getUniqueId() + ".yml");
        FileConfiguration c = YamlConfiguration.loadConfiguration(f);
        int deaths = c.getInt("deaths");
        deaths++;
        if(deaths > XLives.maxLives){
            ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();

            for(int i = 0; i < XLives.maxVaults; i++){
                String command = "pvdel " + p.getName() + " " + i;
                Bukkit.dispatchCommand(console, command);

            }
            if(XLives.clearHomes){
                File essData = new File(Bukkit.getPluginManager().getPlugin("Essentials").getDataFolder().getAbsolutePath() + "/userdata/" + p.getUniqueId() + ".yml");
                FileConfiguration essFileConf = YamlConfiguration.loadConfiguration(essData);
                essFileConf.set("homes", null);
                essFileConf.save(essData);
            }
            if(XLives.clearBalance){
                EconomyResponse r = XLives.econ.withdrawPlayer(p, XLives.econ.getBalance(p));
            }
            if(XLives.clearEnder) p.getEnderChest().clear();

            p.setMaxHealth(2);
            c.set("deaths", 1);
            c.save(f);
            p.spigot().respawn();
            p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 3, 2));
            Bukkit.getServer().broadcastMessage(XLives.colorFormat("&4&l" + p.getName() + "&c has been banned! &4&l0 &clives left."));
            if(XLives.banPlayers) Bukkit.dispatchCommand(console, "tempban " + p.getName() + " 1d " + XLives.colorFormat("&4&lYou're out of lives!"));

        } else {
            p.setMaxHealth(deaths * 2);
            c.set("deaths", deaths);
            c.save(f);
        }


    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e){
        Player p = e.getPlayer();
        File f = new File(XLives.getPlugin().getDataFolder().getAbsolutePath() + "/userdata/", p.getUniqueId() + ".yml");
        FileConfiguration c = YamlConfiguration.loadConfiguration(f);
        int deaths = c.getInt("deaths");
        int lives = XLives.maxLives - deaths;
        p.sendTitle(XLives.colorFormat("&4&lYou died!"), XLives.colorFormat("&c" + lives + " lives left."), 1, 30, 1);
    }

}
