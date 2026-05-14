package me.rtx4090.multiRodTrigger.listener;

import me.rtx4090.multiRodTrigger.item.FishRodData;
import me.rtx4090.multiRodTrigger.item.Key;
import me.rtx4090.multiRodTrigger.manager.FishRodDataManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

public class PlayerUseRodListener implements Listener {
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction().isLeftClick()) return;
        if (event.getItem() == null) return;
        if (event.getItem().getType() != Material.FISHING_ROD) return;
        if (!event.getItem().getPersistentDataContainer().has(Key.BOUND_ROD, PersistentDataType.STRING)) return;

        UUID dataUUID = UUID.fromString(event.getItem().getPersistentDataContainer().get(Key.BOUND_ROD, PersistentDataType.STRING));
        FishRodData fishRodData = FishRodDataManager.getActiveByUUID().get(dataUUID);

        Bukkit.getLogger().info("Player " + event.getPlayer().getName() + " used rod: " + dataUUID.toString());

        fishRodData.pressurePlateLocation.getChunk().load();

        Bukkit.getEntity(fishRodData.slimeUuid).remove();

        ItemMeta meta = event.getItem().getItemMeta();
        meta.getPersistentDataContainer().remove(Key.BOUND_ROD);
        event.getItem().setItemMeta(meta);


        FishRodDataManager.removeActive(fishRodData);

    }
}
