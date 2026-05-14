package me.rtx4090.multiRodTrigger.listener;

import me.rtx4090.multiRodTrigger.item.Key;
import org.bukkit.entity.Slime;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class SlimeDamageListener implements Listener {
    @EventHandler
    public void onSlimeDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Slime)) return;
        Slime slime = (Slime) event.getEntity();
        if (!slime.getPersistentDataContainer().has(Key.BOUND_SLIME)) return;
        event.setCancelled(true);
    }

}
