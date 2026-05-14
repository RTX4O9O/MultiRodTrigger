package me.rtx4090.multiRodTrigger.item;

import org.bukkit.Location;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Slime;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.index.qual.PolyUpperBound;

import java.util.UUID;

public class FishRodData {
    public UUID uuid;
    public FishHook bobber;
    public ItemStack rod;
    public Location pressurePlateLocation;
    public UUID slimeUuid; // this is used to maintain the active status of the pressureplate after chunk unload.

    public FishRodData(UUID uuid, FishHook bobber, ItemStack rod, Location pressurePlateLocation, UUID slimeUuid) {
        this.uuid = uuid;
        this.bobber = bobber;
        this.rod = rod;
        this.pressurePlateLocation = pressurePlateLocation;
        this.slimeUuid = slimeUuid;
    }
}
