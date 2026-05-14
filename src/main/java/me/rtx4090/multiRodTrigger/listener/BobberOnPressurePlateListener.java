package me.rtx4090.multiRodTrigger.listener;

import me.rtx4090.multiRodTrigger.MultiRodTrigger;
import me.rtx4090.multiRodTrigger.item.FishRodData;
import me.rtx4090.multiRodTrigger.item.Key;
import me.rtx4090.multiRodTrigger.manager.FishRodDataManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;


public class BobberOnPressurePlateListener implements Listener {
    @EventHandler
    public void onEntityInteract(EntityInteractEvent event) {
        if (!(event.getEntity() instanceof FishHook)) return;
        if (!isActivablePressurePlate(event.getBlock().getType())) return;

        FishRodData data;
        UUID dataUUID = UUID.randomUUID();

        FishHook fishHook = (FishHook) event.getEntity();

        if (!(fishHook.getShooter() instanceof Player)) return; // how is this possible
        Player player = (Player) fishHook.getShooter();
        if (player.getInventory().getItemInMainHand().getType() == Material.FISHING_ROD) {
            ItemStack rod = player.getInventory().getItemInMainHand();
            data = new FishRodData(dataUUID, fishHook, rod, event.getBlock().getLocation(), null);

        } else if (player.getInventory().getItemInOffHand().getType() == Material.FISHING_ROD) {
            ItemStack rod = player.getInventory().getItemInOffHand();
            data = new FishRodData(dataUUID, fishHook, rod, event.getBlock().getLocation(), null);

        } else { // this shouldnt happen tho
            Bukkit.getLogger().warning("a player " + player.getName() + " triggered a bobber on pressure plate event without holding a fishing rod in either hand.");
            return;

        }

        FishRodDataManager.addPending(data);
        Bukkit.getLogger().info("Bobber landed on pressure plate: " + dataUUID.toString() + " by " + player.getName());

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
