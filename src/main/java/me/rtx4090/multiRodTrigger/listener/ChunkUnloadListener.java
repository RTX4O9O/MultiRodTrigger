package me.rtx4090.multiRodTrigger.listener;

import me.rtx4090.multiRodTrigger.item.FishRodData;
import me.rtx4090.multiRodTrigger.item.Key;
import me.rtx4090.multiRodTrigger.manager.FishRodDataManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Fish;
import org.bukkit.entity.Slime;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class ChunkUnloadListener implements Listener {
    @EventHandler
    public void onChunkUnload(ChunkUnloadEvent event) {
        if (!FishRodDataManager.getPendingByChunk().containsKey(event.getChunk())) return;
        List<FishRodData> toActivate = FishRodDataManager.getPendingByChunk().get(event.getChunk());

        for (FishRodData fishRodData : toActivate) {
            Slime slime = fishRodData.pressurePlateLocation.getWorld().spawn(fishRodData.bobber.getLocation(), Slime.class, s -> {
                s.setSize(0);
                s.setGravity(false);
                s.setInvulnerable(true);
                s.setSilent(true);
                s.setAI(false);
                s.setInvisible(true);
                s.setLootTable(null);

                s.setCollidable(false);
                s.setPersistent(true);
                s.setRemoveWhenFarAway(false);
                s.getPersistentDataContainer().set(Key.BOUND_SLIME, PersistentDataType.STRING, fishRodData.uuid.toString());
            });

            fishRodData.slimeUuid = slime.getUniqueId();

            if (fishRodData.rod.getItemMeta() != null) {
                ItemMeta rodMeta = fishRodData.rod.getItemMeta();
                rodMeta.getPersistentDataContainer().set(Key.BOUND_ROD, PersistentDataType.STRING, fishRodData.uuid.toString());
                fishRodData.rod.setItemMeta(rodMeta);
            }

            fishRodData.bobber = null;

            FishRodDataManager.removePending(fishRodData);
            FishRodDataManager.addActive(fishRodData);
            Bukkit.getLogger().info("Activated rod: " + fishRodData.uuid.toString().substring(0, 8) + " in chunk " + event.getChunk().toString());
        }
    }
}
