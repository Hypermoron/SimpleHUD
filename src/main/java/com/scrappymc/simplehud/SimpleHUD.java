package com.scrappymc.simplehud;

import com.scrappymc.simplehud.command.HUDReload;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class SimpleHUD extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        registerCommands();
        Display.enable(this);
        Bukkit.getLogger().info("[SimpleHUD] Plugin enabled.");
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info("[SimpleHUD] Plugin disabled.");
    }

    public void registerCommands() {
        getCommand("hudreload").setExecutor(new HUDReload(this));
    }

}