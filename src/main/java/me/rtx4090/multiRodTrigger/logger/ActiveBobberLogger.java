package me.rtx4090.multiRodTrigger.logger;


import me.rtx4090.multiRodTrigger.MultiRodTrigger;
import me.rtx4090.multiRodTrigger.item.FishRodData;
import me.rtx4090.multiRodTrigger.manager.FishRodDataManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Slime;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class ActiveBobberLogger {
    private static File file;
    private static FileConfiguration log;

    public static void setUp() {
        file = new File(MultiRodTrigger.getPlugin().getDataFolder(), "bobber-log.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                Bukkit.getLogger().warning("Failed to create the bobber log: " + e.getMessage());
            }
        }

        log = YamlConfiguration.loadConfiguration(file);
    }

    public static FileConfiguration getLog() {
        return log;
    }

    public static void saveLog() {
        try {
            log.save(file);
        } catch (IOException e) {
            Bukkit.getLogger().warning("Failed to save the bobber log: " + e.getMessage());
        }
    }

    public static void reloadLog() {
        log = YamlConfiguration.loadConfiguration(file);
    }

    public static void syncToLog() {
        log.set("active-rods", null); // Clear old entries

        for (FishRodData data : FishRodDataManager.activeBobbers) {
            String path = "active-rods." + data.uuid.toString();

            // bobbers in active will all be null so no need to save
            log.set(path + ".rod", data.rod); // save data.rod using Bukkit's built-in serialization
            if (data.pressurePlateLocation != null) { // save data.pressurePlateLocation
                log.set(path + ".pressurePlateLocation.world", data.pressurePlateLocation.getWorld().getName());
                log.set(path + ".pressurePlateLocation.x", data.pressurePlateLocation.getX());
                log.set(path + ".pressurePlateLocation.y", data.pressurePlateLocation.getY());
                log.set(path + ".pressurePlateLocation.z", data.pressurePlateLocation.getZ());
            }

            log.set(path + ".slime", data.slimeUuid.toString()); // save data.slime in UUID

        }
        Bukkit.getLogger().info("Synced " + FishRodDataManager.activeBobbers.size() + " active rods to log.");
    }

    public static void loadFromLog() {
        if (!log.contains("active-rods")) return; // nothing to load

        for (String key : log.getConfigurationSection("active-rods").getKeys(false)) {
            String path = "active-rods." + key;
            UUID uuid = UUID.fromString(key); // retrieve uuid
            FishHook bobber = null; // bobbers in active will all be null so no need to load
            ItemStack rod = log.getItemStack(path + ".rod"); // retrieve rod
            Location pressurePlateLocation = null; // retrieve pressurePlateLocation
            if (log.contains(path + ".pressurePlateLocation.world")) {
                World world = Bukkit.getWorld(log.getString(path + ".pressurePlateLocation.world"));
                if (world != null) {
                    double x = log.getDouble(path + ".pressurePlateLocation.x");
                    double y = log.getDouble(path + ".pressurePlateLocation.y");
                    double z = log.getDouble(path + ".pressurePlateLocation.z");
                    pressurePlateLocation = new Location(world, x, y, z);

                    pressurePlateLocation.getChunk().load(); // to load slime
                }
            }

            Slime slime = null;
            if (log.contains(path + ".slime") && log.getString(path + ".slime") != null) {
                slime = (Slime) Bukkit.getEntity(UUID.fromString(log.getString(path + ".slime")));
            }
            if (slime == null) {
                Bukkit.getLogger().warning("Failed to load slime for active rod: " + uuid.toString() + ". Skipping this entry.");
                continue;
            }

            FishRodData data = new FishRodData(uuid, bobber, rod, pressurePlateLocation, slime.getUniqueId());
            FishRodDataManager.addActive(data);
        }
        Bukkit.getLogger().info("Loaded " + log.getConfigurationSection("active-rods").getKeys(false).size() + " active rods from log.");
    }
}