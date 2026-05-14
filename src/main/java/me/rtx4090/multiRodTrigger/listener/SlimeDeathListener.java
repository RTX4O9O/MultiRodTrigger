package me.rtx4090.multiRodTrigger.listener;

import me.rtx4090.multiRodTrigger.item.FishRodData;
import me.rtx4090.multiRodTrigger.item.Key;
import me.rtx4090.multiRodTrigger.manager.FishRodDataManager;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Slime;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

public class SlimeDeathListener implements Listener {
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        Entity e = event.getEntity();
        if (!(e instanceof Slime)) return;
        if (!e.getPersistentDataContainer().has(Key.BOUND_SLIME, PersistentDataType.STRING)) return;

        String uuidStr = e.getPersistentDataContainer().get(Key.BOUND_SLIME, PersistentDataType.STRING);
        if (uuidStr == null) return;

        UUID dataUuid;
        try {
            dataUuid = UUID.fromString(uuidStr);
        } catch (IllegalArgumentException ex) {
            return;
        }

        FishRodData data = FishRodDataManager.getActiveByUUID().get(dataUuid);
        if (data == null) return;

        // clear live reference and remove from active set to avoid immediate respawn loops
        data.slimeUuid = null;
        FishRodDataManager.removeActive(data);

        // optional: decide whether to move back to pending or drop the entry entirely depending on game logic
    }
}