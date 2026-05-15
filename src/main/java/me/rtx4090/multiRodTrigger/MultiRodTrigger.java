package me.rtx4090.multiRodTrigger;

import me.rtx4090.multiRodTrigger.command.DebugCommand;
import me.rtx4090.multiRodTrigger.item.FishRodData;
import me.rtx4090.multiRodTrigger.listener.*;
import me.rtx4090.multiRodTrigger.logger.ActiveBobberLogger;
import org.bukkit.Bukkit;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class MultiRodTrigger extends JavaPlugin {
    private static MultiRodTrigger plugin;
    //public boolean enabled;

    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getLogger().info("MultiRodTrigger has been enabled.");
        plugin = this;
        ActiveBobberLogger.setUp();
        ActiveBobberLogger.loadFromLog();
        Bukkit.getLogger().info("[MultiRodTrigger] Loaded active bobbers from log.");

        // Event registration
        Bukkit.getPluginManager().registerEvents(new BobberOnPressurePlateListener(), this);
        Bukkit.getPluginManager().registerEvents(new PressurePlateDepowerListener(), this);
        Bukkit.getPluginManager().registerEvents(new PressurePlateBreakListener(), this);
        Bukkit.getPluginManager().registerEvents(new ChunkUnloadListener(), this);
        Bukkit.getPluginManager().registerEvents(new SlimeDamageListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerUseRodListener(), this);

        // Command registration
        registerCommand("multirodtrigger", "Commands for MultiRodTrigger",
                List.of("mrt"), new DebugCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        ActiveBobberLogger.syncToLog();
        ActiveBobberLogger.saveLog();
        Bukkit.getLogger().info("[MultiRodTrigger] Saved active bobbers to log.");
        Bukkit.getLogger().info("MultiRodTrigger has been disabled.");

    }

    public static MultiRodTrigger getPlugin() {
        return plugin;
    }
}