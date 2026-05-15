package me.rtx4090.multiRodTrigger.listener;

import me.rtx4090.multiRodTrigger.item.Key;
import me.rtx4090.multiRodTrigger.manager.FishRodDataManager;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Slime;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.EntitiesLoadEvent;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.UUID;

public class SlimeLoadListener implements Listener {
    @EventHandler
    public void onEntityLoad(EntitiesLoadEvent event) {
        List<Entity> eventEntities = event.getEntities();
        eventEntities.forEach((Entity e) -> {
            if (!(e instanceof Slime)) return;

            if (!e.getPersistentDataContainer().has(Key.BOUND_SLIME, PersistentDataType.STRING)) return;
            String uuidStr = e.getPersistentDataContainer().get(Key.BOUND_SLIME, PersistentDataType.STRING);
            if (uuidStr == null) return;

            try {
                UUID dataUuid = UUID.fromString(uuidStr);
                // If it is not in the active map, it's an orphan. Remove it.
                if (!FishRodDataManager.getActiveByUUID().containsKey(dataUuid)) {
                    e.remove();
                }
            } catch (IllegalArgumentException ex) {
                e.remove(); // Invalid UUID format, corrupted tag -> remove
            }
        });
    }
}