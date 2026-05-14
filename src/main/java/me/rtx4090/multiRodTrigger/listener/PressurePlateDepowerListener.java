package me.rtx4090.multiRodTrigger.listener;

import me.rtx4090.multiRodTrigger.item.FishRodData;
import me.rtx4090.multiRodTrigger.item.Key;
import me.rtx4090.multiRodTrigger.manager.FishRodDataManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.inventory.meta.ItemMeta;

public class PressurePlateDepowerListener implements Listener {
    @EventHandler
    public void onBlockPhysics(BlockRedstoneEvent event) {
        if (!isActivablePressurePlate(event.getBlock().getType())) return; // not pressure plate
        if (!(event.getOldCurrent() > 0 && event.getNewCurrent() == 0)) return; // not depowering
        if (!FishRodDataManager.getPendingByLocation().containsKey(event.getBlock().getLocation())) return; // not recorded in pending

        // depowering while chunk still loading means it wont enter the "wireless" state = remove from pending
        FishRodData data = FishRodDataManager.getPendingByLocation().get(event.getBlock().getLocation());
        FishRodDataManager.removePending(data);
        Bukkit.getLogger().info("Removed pending rod due to pressure plate depower: " + data.uuid.toString());

    }


    private boolean isActivablePressurePlate(Material m) {
        switch (m) {
            case ACACIA_PRESSURE_PLATE:
            case BIRCH_PRESSURE_PLATE:
            case DARK_OAK_PRESSURE_PLATE:
            case JUNGLE_PRESSURE_PLATE:
            case OAK_PRESSURE_PLATE:
            case SPRUCE_PRESSURE_PLATE:
            case BAMBOO_PRESSURE_PLATE:
            case CHERRY_PRESSURE_PLATE:
            case CRIMSON_PRESSURE_PLATE:
            case MANGROVE_PRESSURE_PLATE:
            case WARPED_PRESSURE_PLATE:
            case PALE_OAK_PRESSURE_PLATE:
            case HEAVY_WEIGHTED_PRESSURE_PLATE:
            case LIGHT_WEIGHTED_PRESSURE_PLATE:
                return true;
            default:
                return false;
        }
    }
}
