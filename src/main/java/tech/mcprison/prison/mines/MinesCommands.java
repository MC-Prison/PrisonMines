package tech.mcprison.prison.mines;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.commands.Arg;
import tech.mcprison.prison.commands.Command;
import tech.mcprison.prison.internal.CommandSender;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.mines.util.Block;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.selection.Selection;
import tech.mcprison.prison.util.BlockType;

import java.util.Objects;

/**
 * Created by DMP9 on 14/01/2017.
 */
public class MinesCommands {
    @Command(identifier = "mines", permissions = "prison.mines.gui")
    public void minesCommand(CommandSender sender) {
        Mines.get().getMines().createGUI().show((Player) sender);
    }

    @Command(identifier = "mines create", permissions = {"prison.mines.create", "prison.admin"})
    public void createCommand(CommandSender sender,
        @Arg(name = "mineName", description = "The name of the mine to be created") String name) {
        Selection selection = Prison.get().getSelectionManager().getSelection((Player) sender);
        if (!selection.isComplete()) {
            sender.sendMessage("&cYou need to select the mine boundaries first!");
            return;
        }
        if (!Objects.equals(selection.getMin().getWorld().getName(),
            selection.getMax().getWorld().getName())) {
            sender.sendMessage("&cCan't create mines with two different worlds!");
            return;
        }
        if (Mines.get().getMines().contains(name)) {
            sender.sendMessage("&cThat mine already exists!");
            return;
        }
        Mine mine = new Mine().setBounds(selection.asBounds()).setName(name);
        Mines.get().getMines().add(mine);
        sender.sendMessage("&aMine created successfully!");
    }

    @Command(identifier = "mines addblock", permissions = {"prison.mines.addblock", "prison.admin"})
    public void addBlockCommand(CommandSender sender, @Arg(name = "mineName") String mine,
        @Arg(name = "block", def = "AIR") String block, @Arg(name = "chance") double chance) {
        if (!Mines.get().getMines().contains(mine)) {
            sender.sendMessage("&cThat mine doesn't exist!");
            return;
        }
        BlockType blockType = BlockType.getBlock(block);
        if (blockType == null) {
            sender.sendMessage("&6" + block + "&c is not a block!");
            return;
        }
        if (Mines.get().getMines().get(mine).isInMine(blockType)) {
            sender.sendMessage("&cThat block has already been added!");
            return;
        }
        Mines.get().getMines().get(mine).getBlocks().add(new Block().create(blockType,chance));
        sender.sendMessage("&aAdded block &6" + blockType.getId().replaceAll("_", " ").toLowerCase()
            + "&a to mine &6" + mine);
    }

    @Command(identifier = "mines delblock", permissions = {"prison.mines.delblock", "prison.admin"})
    public void delBlockCommand(CommandSender sender, @Arg(name = "mineName") String mine,
        @Arg(name = "block", def = "AIR") String block) {
        if (!Mines.get().getMines().contains(mine)) {
            sender.sendMessage("&cThat mine doesn't exist!");
            return;
        }
        BlockType blockType = BlockType.getBlock(block);
        if (blockType == null) {
            sender.sendMessage("&6" + block + "&c is not a block!");
            return;
        }
        if (!Mines.get().getMines().get(mine).isInMine(blockType)) {
            sender.sendMessage("&cThat block isn't in the mine!");
            return;
        }
    }


    @Command(identifier = "mines delete", permissions = {"prison.mines.delete", "prison.admin"})
    public void deleteCommand(CommandSender sender,
        @Arg(name = "mineName", description = "The name of the mine to be deleted") String name) {
        if (!Mines.get().getMines().contains(name)) {
            sender.sendMessage("&cThat mine doesn't exist!");
            return;
        }
        Mines.get().getMines().remove(Mines.get().getMines().get(name));
        sender.sendMessage("&aMine deleted successfully!");
    }

    @Command(identifier = "mines info", permissions = {"prison.mines.info", "prison.admin"})
    public void infoCommand(CommandSender sender,
        @Arg(name = "mineName", description = "The name of the mine") String name) {
        if (!Mines.get().getMines().contains(name)) {
            sender.sendMessage("&cThat mine doesn't exist!");
            return;
        }
        Mine m = Mines.get().getMines().get(name);
        String title = "&b============ &7" + m.getName() + "&b ============";
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
        sender.sendMessage(String.format("%0" + (title.length() - 4) + "d", 0).replace("0", "="));
    }

    @Command(identifier = "mines reset", permissions = {"prison.mines.info", "prison.admin"})
    public void resetCommand(CommandSender sender,
        @Arg(name = "mineName", description = "The name of the mine to be reset") String name) {
        if (!Mines.get().getMines().contains(name)) {
            sender.sendMessage("&cThat mine doesn't exist!");
            return;
        }
        try {
            Mines.get().getMines().get(name).reset();
        }catch (Exception e){
            sender.sendMessage("&cCouldn't reset mine. Check the log for details.");
            Output.get().logError("Couldn't reset mine " + name,e);
        }
        sender.sendMessage("&aMine reset!");
    }


    @Command(identifier = "mines list", permissions = {"prison.mines.delete", "prison.admin"})
    public void listCommand(CommandSender sender) {
        sender.sendMessage("&b============ &7/mines list&b ============");
        for (Mine m : Mines.get().getMines()) {
            sender.sendMessage("&8" + m.getName());
        }
        sender.sendMessage("&b============ &7/mines list&b ============");
    }


    @Command(identifier = "mines redefine", permissions = {"prison.mines.identify", "prison.admin"})
    public void redefineCommand(CommandSender sender,
        @Arg(name = "mineName", description = "The name of the mine to be redefined") String name) {
        Selection selection = Prison.get().getSelectionManager().getSelection((Player) sender);
        if (!selection.isComplete()) {
            sender.sendMessage("&cYou need to select the mine boundaries first!");
            return;
        }
        if (!Objects.equals(selection.getMin().getWorld().getName(),
            selection.getMax().getWorld().getName())) {
            sender.sendMessage("&cCan't create mines with two different worlds!");
            return;
        }
        if (!Mines.get().getMines().contains(name)) {
            sender.sendMessage("&cThat mine doesn't exist!");
            return;
        }
        Mines.get().getMines().get(name).setBounds(selection.asBounds());
        sender.sendMessage("&aMine redefined successfully!");
    }
}
