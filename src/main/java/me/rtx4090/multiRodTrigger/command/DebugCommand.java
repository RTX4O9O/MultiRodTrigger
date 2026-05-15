package me.rtx4090.multiRodTrigger.command;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import me.rtx4090.multiRodTrigger.MultiRodTrigger;
import me.rtx4090.multiRodTrigger.item.FishRodData;
import me.rtx4090.multiRodTrigger.logger.ActiveBobberLogger;
import me.rtx4090.multiRodTrigger.manager.FishRodDataManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Fish;
import org.bukkit.entity.Player;
import org.jspecify.annotations.Nullable;

import java.util.Collection;
import java.util.UUID;

public class DebugCommand implements BasicCommand {
    @Override
    public void execute(CommandSourceStack commandSourceStack, String[] args) {
        if (!commandSourceStack.getSender().hasPermission("multirodtrigger.op")) handleVersion(commandSourceStack);
        if (args.length < 1) {
            handleVersion(commandSourceStack);
            return;
        }

        switch (args[0]) {
            case "list":
                handleList(commandSourceStack);
                break;

            case "getrod":
                handleGet(commandSourceStack, args);
                break;

            case "tp":
                handleTp(commandSourceStack, args);
                break;

            case "remove":
                handleRemove(commandSourceStack, args);
                break;

            case "cleanup":
                handleCleanup(commandSourceStack);
                break;

            case "reload":
                handleReload(commandSourceStack, args);
                break;

            case "version":
                handleVersion(commandSourceStack);
                break;

            default:
                handleVersion(commandSourceStack);
                break;
        }
    }

    @Override
    public Collection<String> suggest(CommandSourceStack commandSourceStack, String[] args) {
        return BasicCommand.super.suggest(commandSourceStack, args);
    }


    private void handleList(CommandSourceStack commandSourceStack) {
        commandSourceStack.getSender().sendMessage("There are currently " + FishRodDataManager.getActiveByUUID().size() + " active bobbers and " + FishRodDataManager.getPendingByUUID().size() + " pending bobbers.");
        Component messages = Component.empty();
        messages.append(Component.text("Active bobbers:").decorate(TextDecoration.BOLD)).append(Component.newline());
        FishRodDataManager.getActiveByUUID().forEach((uuid, fishRodData) -> {
            messages.append(Component.text(uuid.toString()).decorate(TextDecoration.BOLD).clickEvent(ClickEvent.copyToClipboard(uuid.toString())))
                    .append(Component.newline())
                    .append(Component.text("  Pressure Plate @ " + fishRodData.pressurePlateLocation.getWorld() + " (" + fishRodData.pressurePlateLocation.getBlockX() + ", " + fishRodData.pressurePlateLocation.getBlockY() + ", " + fishRodData.pressurePlateLocation.getBlockZ() + ")").decorate(TextDecoration.ITALIC));
        });
        messages.append(Component.text("Pending bobbers:").decorate(TextDecoration.BOLD)).append(Component.newline());
        FishRodDataManager.getPendingByUUID().forEach((uuid, fishRodData) -> {
            messages.append(Component.text(uuid.toString()).decorate(TextDecoration.BOLD).clickEvent(ClickEvent.copyToClipboard(uuid.toString())))
                    .append(Component.newline())
                    .append(Component.text("  Pressure Plate @ " + fishRodData.pressurePlateLocation.getWorld() + " (" + fishRodData.pressurePlateLocation.getBlockX() + ", " + fishRodData.pressurePlateLocation.getBlockY() + ", " + fishRodData.pressurePlateLocation.getBlockZ() + ")").decorate(TextDecoration.ITALIC));
        });
        commandSourceStack.getSender().sendMessage(messages);
    }

    private void handleGet(CommandSourceStack commandSourceStack, String[] args) {
        if (!(commandSourceStack.getSender() instanceof Player)) return;
        Player player = (Player) commandSourceStack.getSender();

        UUID uuid;
        try {
            uuid = UUID.fromString(args[1]);
        } catch (IllegalArgumentException e) {
            commandSourceStack.getSender().sendMessage("Invalid UUID format: " + args[1]);
            return;
        }

        FishRodData data = FishRodDataManager.getActiveByUUID().get(uuid);
        if (data == null) {
            data = FishRodDataManager.getPendingByUUID().get(uuid);
            if (data == null) {
                commandSourceStack.getSender().sendMessage("No bobber found with UUID: " + uuid.toString());
                return;
            } else {
                player.getInventory().addItem(data.rod);
                commandSourceStack.getSender().sendMessage("Getting a clone of pending rod item with UUID " + uuid.toString() + " which currently pending at " + data.pressurePlateLocation.toString());
            }
        } else {
            player.getInventory().addItem(data.rod);
            commandSourceStack.getSender().sendMessage("Getting a clone of active rod item with UUID with UUID " + uuid.toString() + " which is currently active at " + data.pressurePlateLocation.toString());
        }
    }

    private void handleTp(CommandSourceStack commandSourceStack, String[] args) {
        if (!(commandSourceStack.getSender() instanceof Player)) return;
        Player player = (Player) commandSourceStack.getSender();

        UUID uuid;
        try {
            uuid = UUID.fromString(args[1]);
        } catch (IllegalArgumentException e) {
            commandSourceStack.getSender().sendMessage("Invalid UUID format: " + args[1]);
            return;
        }

        FishRodData data = FishRodDataManager.getActiveByUUID().get(uuid);
        if (data == null) {
            data = FishRodDataManager.getPendingByUUID().get(uuid);
            if (data == null) {
                commandSourceStack.getSender().sendMessage("No bobber found with UUID: " + uuid.toString());
                return;
            } else {
                player.teleport(data.pressurePlateLocation);
                commandSourceStack.getSender().sendMessage("Teleported to pending bobber with UUID " + uuid.toString() + " which currently pending at " + data.pressurePlateLocation.toString());
            }
        } else {
            player.teleport(data.pressurePlateLocation);
            commandSourceStack.getSender().sendMessage("Teleported to active bobber with UUID " + uuid.toString() + " which is currently active at " + data.pressurePlateLocation.toString());
        }
    }

    private void handleRemove(CommandSourceStack commandSourceStack, String[] args) {
        UUID uuid;
        try {
            uuid = UUID.fromString(args[1]);
        } catch (IllegalArgumentException e) {
            commandSourceStack.getSender().sendMessage("Invalid UUID format: " + args[1]);
            return;
        }

        FishRodData data = FishRodDataManager.getActiveByUUID().get(uuid);
        if (data != null) {
            data.pressurePlateLocation.getChunk().load();
            Bukkit.getEntity(data.slimeUuid).remove();
            FishRodDataManager.removeActive(data);
            commandSourceStack.getSender().sendMessage("Removed active bobber with UUID: " + uuid.toString());
            return;
        } else {
            commandSourceStack.getSender().sendMessage("No active bobber found with UUID: " + uuid.toString());
            return;
        }

    }

    private void handleCleanup(CommandSourceStack commandSourceStack) {
        int removedCount = 0;
        for (org.bukkit.World world : Bukkit.getWorlds()) {
            for (org.bukkit.entity.LivingEntity entity : world.getLivingEntities()) {
                if (entity instanceof org.bukkit.entity.Slime slime) {
                    if (slime.getPersistentDataContainer().has(me.rtx4090.multiRodTrigger.item.Key.BOUND_SLIME, org.bukkit.persistence.PersistentDataType.STRING)) {
                        String uuidStr = slime.getPersistentDataContainer().get(me.rtx4090.multiRodTrigger.item.Key.BOUND_SLIME, org.bukkit.persistence.PersistentDataType.STRING);
                        if (uuidStr == null) continue;

                        try {
                            UUID dataUuid = UUID.fromString(uuidStr);
                            if (!FishRodDataManager.getActiveByUUID().containsKey(dataUuid)) {
                                slime.remove();
                                removedCount++;
                            }
                        } catch (IllegalArgumentException e) {
                            slime.remove();
                            removedCount++;
                        }
                    }
                }
            }
        }
        commandSourceStack.getSender().sendMessage("Cleaned up " + removedCount + " orphaned slime entities.");
    } // ai coded


    private void handleReload(CommandSourceStack commandSourceStack, String[] args) {
        ActiveBobberLogger.setUp();
        ActiveBobberLogger.loadFromLog();
        commandSourceStack.getSender().sendMessage("MultiRodTrigger configuration reloaded.");
    }

    private void handleVersion(CommandSourceStack commandSourceStack) {
        commandSourceStack.getSender().sendMessage("MultiRodTrigger version " + MultiRodTrigger.getPlugin().getDescription().getVersion());
    }


}
