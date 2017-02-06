package tech.mcprison.prison.mines;

import tech.mcprison.prison.Output;
import tech.mcprison.prison.Prison;
import tech.mcprison.prison.commands.Arg;
import tech.mcprison.prison.commands.Command;
import tech.mcprison.prison.internal.CommandSender;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.mines.util.Block;
import tech.mcprison.prison.mines.util.MinesUtil;
import tech.mcprison.prison.selection.Selection;
import tech.mcprison.prison.util.BlockType;

import java.util.Objects;

/**
 * Created by DMP9 on 14/01/2017.
 */
public class MinesCommands {
    @Command(identifier = "autosmelt", permissions = "prison.mines.autosmelt")
    public void autosmeltCommand(CommandSender sender) {
        if (MinesUtil.usingAutosmelt((Player) sender)) {
            try {
                MinesUtil.disableAutosmelt((Player) sender);
            } catch (Exception e) {
                Output.get().logError("Couldn't disable autosmelt for player " + sender.getName());
                sender.sendMessage(
                    MinesUtil.addPrefix(Mines.get().getMinesMessages().autosmeltDisable_fail));
            }
        } else {
            try {
                MinesUtil.enableAutosmelt((Player) sender);
            } catch (Exception e) {
                Output.get().logError("Couldn't enable autosmelt for player " + sender.getName());
                sender.sendMessage(
                    MinesUtil.addPrefix(Mines.get().getMinesMessages().autosmeltEnable_fail));
            }
        }
        sender.sendMessage(MinesUtil.usingAutosmelt((Player) sender) ?
            MinesUtil.addPrefix(Mines.get().getMinesMessages().autosmeltEnable) :
            MinesUtil.addPrefix(Mines.get().getMinesMessages().autosmeltDisable));
    }

    @Command(identifier = "mines", permissions = "prison.mines.gui")
    public void minesCommand(CommandSender sender) {
        Mines.get().getMines().createGUI((Player) sender).show((Player) sender);
    }

    @Command(identifier = "mines create", permissions = {"prison.mines.create", "prison.admin"})
    public void createCommand(CommandSender sender,
        @Arg(name = "mineName", description = "The name of the mine to be created") String name) {
        Selection selection = Prison.get().getSelectionManager().getSelection((Player) sender);
        if (!selection.isComplete()) {
            sender
                .sendMessage(MinesUtil.addPrefix(Mines.get().getMinesMessages().selectBoundaries));
            return;
        }
        if (!Objects.equals(selection.getMin().getWorld().getName(),
            selection.getMax().getWorld().getName())) {
            sender.sendMessage(MinesUtil.addPrefix(Mines.get().getMinesMessages().worldDiff));
            return;
        }
        if (Mines.get().getMines().contains(name)) {
            sender.sendMessage(MinesUtil.addPrefix(Mines.get().getMinesMessages().mineExists));
            return;
        }
        Mine mine = new Mine().setBounds(selection.asBounds()).setName(name);
        Mines.get().getMines().add(mine);
        sender.sendMessage(MinesUtil.addPrefix(Mines.get().getMinesMessages().mineCreated));
    }

    @Command(identifier = "mines spawnpoint", permissions = {"prison.mines.spawnpoint",
        "prison.admin"}) public void spawnpointCommand(CommandSender sender,
        @Arg(name = "mineName", description = "The name of the mine to assign the spawnpoint to")
            String name) {
        if (!Mines.get().getMines().contains(name)) {
            sender.sendMessage(MinesUtil.addPrefix(Mines.get().getMinesMessages().mineDoesntExist));
            return;
        }
        if (!((Player) sender).getLocation().getWorld().getName()
            .equalsIgnoreCase(Mines.get().getMines().get(name).getWorld().getName())) {
            sender.sendMessage(
                MinesUtil.addPrefix(Mines.get().getMinesMessages().spawnpointSameWorldMine));
        }
        Mines.get().getMines().get(name).setSpawn(((Player) sender).getLocation());
        sender.sendMessage(MinesUtil.addPrefix(Mines.get().getMinesMessages().spawnpoint));
    }

    @Command(identifier = "mines addblock", permissions = {"prison.mines.addblock",
        "prison.admin"}, onlyPlayers = false, description = "Adds a block to a mine")
    public void addBlockCommand(CommandSender sender, @Arg(name = "mineName") String mine,
        @Arg(name = "block", def = "AIR") String block, @Arg(name = "chance") int chance) {
        if (!Mines.get().getMines().contains(mine)) {
            sender.sendMessage(MinesUtil.addPrefix(Mines.get().getMinesMessages().mineDoesntExist));
            return;
        }
        BlockType blockType = BlockType.getBlock(block);
        if (blockType == null) {
            sender.sendMessage(MinesUtil
                .addPrefix(Mines.get().getMinesMessages().notABlock.replaceAll("%block%", block)));
            return;
        }
        if (Mines.get().getMines().get(mine).isInMine(blockType)) {
            sender
                .sendMessage(MinesUtil.addPrefix(Mines.get().getMinesMessages().blockAlreadyAdded));
            return;
        }
        Mines.get().getMines().get(mine).getBlocks().add(new Block().create(blockType, chance));
        sender.sendMessage(MinesUtil.addPrefix(Mines.get().getMinesMessages().blockAdded
            .replaceAll("%block%",
                blockType.getId().replaceAll("_", " ").replaceAll("minecraft:", "").toLowerCase())
            .replaceAll("%mine%", Mines.get().getMines().get(mine).getName())));
        Mines.get().getMines().clearCache();

    }

    @Command(identifier = "mines delblock", permissions = {"prison.mines.delblock",
        "prison.admin"}, onlyPlayers = false, description = "Deletes a block from a mine")
    public void delBlockCommand(CommandSender sender, @Arg(name = "mineName") String mine,
        @Arg(name = "block", def = "AIR") String block) {
        if (!Mines.get().getMines().contains(mine)) {
            sender.sendMessage(MinesUtil.addPrefix(Mines.get().getMinesMessages().mineDoesntExist));
            return;
        }
        BlockType blockType = BlockType.getBlock(block);
        if (blockType == null) {
            sender.sendMessage(MinesUtil
                .addPrefix(Mines.get().getMinesMessages().notABlock.replaceAll("%block%", block)));
            return;
        }
        if (!Mines.get().getMines().get(mine).isInMine(blockType)) {
            sender.sendMessage(MinesUtil.addPrefix(Mines.get().getMinesMessages().blockNotAdded));
            return;
        }
        Mines.get().getMines().get(mine).getBlocks().removeIf(x -> x.type == blockType);
        sender.sendMessage(MinesUtil.addPrefix(Mines.get().getMinesMessages().blockDeleted
            .replaceAll("%block%",
                blockType.getId().replaceAll("_", " ").replaceAll("minecraft:", "").toLowerCase())
            .replaceAll("%mine%", Mines.get().getMines().get(mine).getName())));
        Mines.get().getMines().clearCache();
    }


    @Command(identifier = "mines delete", permissions = {"prison.mines.delete",
        "prison.admin"}, onlyPlayers = false, description = "Deletes a mine")
    public void deleteCommand(CommandSender sender,
        @Arg(name = "mineName", description = "The name of the mine to be deleted") String name) {
        if (!Mines.get().getMines().contains(name)) {
            sender.sendMessage(MinesUtil.addPrefix(Mines.get().getMinesMessages().mineDoesntExist));
            return;
        }
        Mines.get().getMines().remove(Mines.get().getMines().get(name));
        sender.sendMessage(MinesUtil.addPrefix(Mines.get().getMinesMessages().mineDeleted));
    }

    @Command(identifier = "mines info", permissions = {"prison.mines.info",
        "prison.admin"}, onlyPlayers = false, description = "Lists basic information about a mine")
    public void infoCommand(CommandSender sender,
        @Arg(name = "mineName", description = "The name of the mine") String name) {
        if (!Mines.get().getMines().contains(name)) {
            sender.sendMessage(MinesUtil.addPrefix(Mines.get().getMinesMessages().mineDoesntExist));
            return;
        }
        Mine m = Mines.get().getMines().get(name);
        String title = "&b============ &d" + m.getName() + "&b ============";
        sender.sendMessage(title);
        if (m.getWorld() == null) {
            sender.sendMessage("&bWorld: &cis no longer present");
        } else {
            sender.sendMessage("&bWorld: &7" + m.getWorld().getName());
        }
        sender.sendMessage(
            "&bX1: &7" + m.getBounds().getMin().getBlockX() + " &bY1: &7" + m.getBounds().getMin()
                .getBlockY() + "&b Z1: &7" + m.getBounds().getMin().getBlockZ() + "&b X2: &7" + m
                .getBounds().getMax().getBlockX() + "&b Y2: &7" + m.getBounds().getMax().getBlockY()
                + "&b Z2: &7" + m.getBounds().getMax().getBlockZ());
        sender.sendMessage(
            "&bSize: &7" + m.getBounds().getWidth() + "&8x&b" + m.getBounds().getLength() + " &8(&7"
                + m.getBounds().getHeight() + " &bblocks deep&8)");
        if (m.hasSpawn()) {
            sender.sendMessage(
                "&bSpawnpoint: X: &7" + m.getSpawn().get().getX() + "&bY: &7" + m.getSpawn().get()
                    .getY() + "&bZ: &7" + m.getSpawn().get().getZ());
        } else {
            sender.sendMessage("&bSpawnpoint: &cnot set");
        }
        sender.sendMessage(String.format("%0" + (title.length() - 7) + "d", 0).replace("0", "="));
    }

    @Command(identifier = "mines reset", permissions = {"prison.mines.reset", "prison.admin"})
    public void resetCommand(CommandSender sender,
        @Arg(name = "mineName", description = "The name of the mine to be reset") String name) {
        if (!Mines.get().getMines().contains(name)) {
            sender.sendMessage(MinesUtil.addPrefix(Mines.get().getMinesMessages().mineDoesntExist));
            return;
        }
        try {
            Mines.get().getMines().get(name).reset();
        } catch (Exception e) {
            sender.sendMessage(MinesUtil.addPrefix(Mines.get().getMinesMessages().mineReset_fail));
            Output.get().logError("Couldn't reset mine " + name, e);
        }
        sender.sendMessage(MinesUtil.addPrefix(Mines.get().getMinesMessages().mineReset));
    }


    @Command(identifier = "mines list", permissions = {"prison.mines.list",
        "prison.admin"}, onlyPlayers = false) public void listCommand(CommandSender sender) {
        sender.sendMessage("&b============ &d/mines list&b ============");
        for (Mine m : Mines.get().getMines()) {
            sender.sendMessage("&8" + m.getName());
        }
        sender.sendMessage("&b============ &d/mines list&b ============");
    }


    @Command(identifier = "mines redefine", permissions = {"prison.mines.redefine", "prison.admin"})
    public void redefineCommand(CommandSender sender,
        @Arg(name = "mineName", description = "The name of the mine to be redefined") String name) {
        Selection selection = Prison.get().getSelectionManager().getSelection((Player) sender);
        if (!selection.isComplete()) {
            sender
                .sendMessage(MinesUtil.addPrefix(Mines.get().getMinesMessages().selectBoundaries));
            return;
        }
        if (!Objects.equals(selection.getMin().getWorld().getName(),
            selection.getMax().getWorld().getName())) {
            sender.sendMessage(MinesUtil.addPrefix(Mines.get().getMinesMessages().worldDiff));
            return;
        }
        if (!Mines.get().getMines().contains(name)) {
            sender.sendMessage(MinesUtil.addPrefix(Mines.get().getMinesMessages().mineDoesntExist));
            return;
        }
        Mines.get().getMines().get(name).setBounds(selection.asBounds());
        sender.sendMessage(MinesUtil.addPrefix(Mines.get().getMinesMessages().mineRedefined));
        Mines.get().getMines().clearCache();
    }
}
