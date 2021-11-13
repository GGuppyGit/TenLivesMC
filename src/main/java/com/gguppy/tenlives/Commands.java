package com.gguppy.tenlives;

import jdk.incubator.vector.VectorOperators;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class Commands implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equalsIgnoreCase("setlives")){
            if((args.length != 2)){
                sender.sendMessage(TenLives.colorFormat("&c&l❌ &cUsage: /setlives player amount"));
            } else {
                Player p = Bukkit.getPlayer(args[0]);
                int amount = Integer.parseInt(args[1]);
                int deaths = 11 - amount;
                File f = new File(TenLives.getPlugin().getDataFolder().getAbsolutePath() + "/userdata/", p.getUniqueId() + ".yml");
                FileConfiguration c = YamlConfiguration.loadConfiguration(f);
                c.set("deaths", deaths);
                p.setMaxHealth(deaths * 2);
                sender.sendMessage(TenLives.colorFormat("&a&l✔ " + "&a" + p.getName() + " now has &c" + amount + "&a lives and &c" + (11- amount) + " &ahearts."));
                try {
                    c.save(f);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }
}
