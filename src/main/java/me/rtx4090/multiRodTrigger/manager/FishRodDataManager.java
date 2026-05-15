package me.rtx4090.multiRodTrigger.manager;

import me.rtx4090.multiRodTrigger.MultiRodTrigger;
import me.rtx4090.multiRodTrigger.item.FishRodData;
import me.rtx4090.multiRodTrigger.logger.ActiveBobberLogger;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Fish;
import org.bukkit.entity.FishHook;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.ArrayList;

public class FishRodDataManager {
    public static List<FishRodData> pendingBobbers = new ArrayList<>(); // add when bobbers on pressure plate, remove when bobber confirm not to be activate or activate
    public static List<FishRodData> activeBobbers = new ArrayList<>();

    private static Map<UUID, FishRodData> pendingByUUID = new HashMap<>();
    private static Map<UUID, FishRodData> activeByUUID = new HashMap<>();
    private static Map<Location, FishRodData> pendingByLocation = new HashMap<>();
    private static Map<Location, FishRodData> activeByLocation = new HashMap<>();
    private static Map<Chunk, List<FishRodData>> pendingByChunk = new HashMap<>();

    public static Map<UUID, FishRodData> getPendingByUUID() {
        return pendingByUUID;
    }

    public static Map<UUID, FishRodData> getActiveByUUID() {
        return activeByUUID;
    }

    public static Map<Location, FishRodData> getPendingByLocation() {
        return pendingByLocation;
    }

    public static Map<Location, FishRodData> getActiveByLocation() {
        return activeByLocation;
    }

    public static Map<Chunk, List<FishRodData>> getPendingByChunk() {
        return pendingByChunk;
    }

    public static void addPending(FishRodData data) {
        pendingBobbers.add(data);
        pendingByUUID.put(data.uuid, data);
        pendingByLocation.put(data.pressurePlateLocation, data);
        Chunk chunk = data.pressurePlateLocation.getChunk();
        pendingByChunk.computeIfAbsent(chunk, k -> new ArrayList<>()).add(data);
        Bukkit.getLogger().info("Added pending bobber: " + data.uuid.toString() + " at " + data.pressurePlateLocation.toString());
    }

    public static void removePending(FishRodData data) {
        pendingBobbers.remove(data);
        pendingByUUID.remove(data.uuid);
        pendingByLocation.remove(data.pressurePlateLocation);
        Chunk chunk = data.pressurePlateLocation.getChunk();
        pendingByChunk.computeIfPresent(chunk, (k, list) -> {
            list.remove(data);
            return list.isEmpty() ? null : list; // Removes the chunk key if the list becomes empty
        });
        Bukkit.getLogger().info("Removed pending bobber: " + data.uuid.toString() + " at " + data.pressurePlateLocation.toString());
    }

    public static void addActive(FishRodData data) {
        activeBobbers.add(data);
        activeByUUID.put(data.uuid, data);
        activeByLocation.put(data.pressurePlateLocation, data);
        Bukkit.getLogger().info("Added active bobber: " + data.uuid.toString() + " at " + data.pressurePlateLocation.toString());
        ActiveBobberLogger.syncToLog();
        ActiveBobberLogger.saveLog();
    }

    public static void removeActive(FishRodData data) {
        activeBobbers.remove(data);
        activeByUUID.remove(data.uuid);
        activeByLocation.remove(data.pressurePlateLocation);
        Bukkit.getLogger().info("Removed active bobber: " + data.uuid.toString() + " at " + data.pressurePlateLocation.toString());
        ActiveBobberLogger.syncToLog();
        ActiveBobberLogger.saveLog();
    }

}