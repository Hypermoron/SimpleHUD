package com.scrappymc.simplehud.command;

import com.scrappymc.simplehud.Display;
import com.scrappymc.simplehud.SimpleHUD;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class HUDReload implements CommandExecutor {

    private final SimpleHUD plugin;

    public HUDReload(SimpleHUD plugin)
    {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("hud.reload")) {
            plugin.reloadConfig();
            Bukkit.getScheduler().cancelTasks(plugin);
            Display.enable(plugin);
        } else {
            sender.sendMessage(ChatColor.RED + "You do not have permission to perform this action.");
        }
        return true;
    }

}
