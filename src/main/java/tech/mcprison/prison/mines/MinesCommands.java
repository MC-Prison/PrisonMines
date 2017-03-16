/*
 * Prison is a Minecraft plugin for the prison game mode.
 * Copyright (C) 2017 The Prison Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package tech.mcprison.prison.mines;

import tech.mcprison.prison.localization.Localizable;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.Prison;
import tech.mcprison.prison.commands.Arg;
import tech.mcprison.prison.commands.Command;
import tech.mcprison.prison.internal.CommandSender;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.mines.util.Block;
import tech.mcprison.prison.mines.util.MinesUtil;
import tech.mcprison.prison.selection.Selection;
import tech.mcprison.prison.util.BlockType;
import tech.mcprison.prison.util.Text;

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
                Mines.get().getMinesMessages().getLocalizable("autosmelt_disable").sendTo(sender);
            } catch (Exception e) {
                Output.get()
                    .logError("Couldn't disable autosmelt for player " + sender.getName(), e);
            }
        } else {
            try {
                MinesUtil.enableAutosmelt((Player) sender);
                Mines.get().getMinesMessages().getLocalizable("autosmelt_enable").sendTo(sender);
            } catch (Exception e) {
                Output.get()
                    .logError("Couldn't enable autosmelt for player " + sender.getName(), e);
            }
        }
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
            Mines.get().getMinesMessages().getLocalizable("select_bounds").sendTo(sender,
                Localizable.Level.ERROR);
            return;
        }
        if (!Objects.equals(selection.getMin().getWorld().getName(),
            selection.getMax().getWorld().getName())) {
            Mines.get().getMinesMessages().getLocalizable("world_diff").sendTo(sender,
                Localizable.Level.ERROR);
            return;
        }
        if (Mines.get().getMines().stream().anyMatch(mine -> mine.getName().equalsIgnoreCase(name))) {
            Mines.get().getMinesMessages().getLocalizable("mine_exists").sendTo(sender,
                Localizable.Level.ERROR);
            return;
        }
        Mine mine = new Mine().setBounds(selection.asBounds()).setName(name);
        Mines.get().getMines().add(mine);
        Mines.get().getMinesMessages().getLocalizable("mine_created").sendTo(sender);
    }

    @Command(identifier = "mines spawnpoint", permissions = {"prison.mines.spawnpoint",
        "prison.admin"}) public void spawnpointCommand(CommandSender sender,
        @Arg(name = "mineName", description = "The name of the mine to assign the spawnpoint to")
            String name) {
        if (!Mines.get().getMines().contains(name)) {
            Mines.get().getMinesMessages().getLocalizable("mine_does_not_exist").sendTo(sender);
            return;
        }
        if (!Mines.get().getMines().get(name).getWorld().isPresent()) {
            Mines.get().getMinesMessages().getLocalizable("missing_world").sendTo(sender);
            return;
        }
        if (!((Player) sender).getLocation().getWorld().getName()
            .equalsIgnoreCase(Mines.get().getMines().get(name).getWorld().get().getName())) {
            Mines.get().getMinesMessages().getLocalizable("spawnpoint_same_world").sendTo(sender);
            return;
        }
        Mines.get().getMines().get(name).setSpawn(((Player) sender).getLocation());
        Mines.get().getMinesMessages().getLocalizable("spawn_set").sendTo(sender);
    }

    @Command(identifier = "mines addblock", permissions = {"prison.mines.addblock",
        "prison.admin"}, onlyPlayers = false, description = "Adds a block to a mine")
    public void addBlockCommand(CommandSender sender, @Arg(name = "mineName") String mine,
        @Arg(name = "block", def = "AIR") String block, @Arg(name = "chance") int chance) {
        if (!Mines.get().getMines().contains(mine)) {
            Mines.get().getMinesMessages().getLocalizable("mine_does_not_exist").sendTo(sender);
            return;
        }
        BlockType blockType = BlockType.getBlock(block);
        if (blockType == null) {
            Mines.get().getMinesMessages().getLocalizable("not_a_block").withReplacements(block).sendTo(sender);
            return;
        }
        if (Mines.get().getMines().get(mine).isInMine(blockType)) {
            Mines.get().getMinesMessages().getLocalizable("block_already_added").sendTo(sender);
            return;
        }
        Mines.get().getMines().get(mine).getBlocks().add(new Block().create(blockType, chance));
        Mines.get().getMinesMessages().getLocalizable("block_added").withReplacements(block, mine).sendTo(sender);
        Mines.get().getMines().clearCache();

    }

    @Command(identifier = "mines delblock", permissions = {"prison.mines.delblock",
        "prison.admin"}, onlyPlayers = false, description = "Deletes a block from a mine")
    public void delBlockCommand(CommandSender sender, @Arg(name = "mineName") String mine,
        @Arg(name = "block", def = "AIR") String block) {
        if (!Mines.get().getMines().contains(mine)) {
            Mines.get().getMinesMessages().getLocalizable("mine_does_not_exist").sendTo(sender);
            return;
        }
        BlockType blockType = BlockType.getBlock(block);
        if (blockType == null) {
            Mines.get().getMinesMessages().getLocalizable("not_a_block").withReplacements(block).sendTo(sender);
            return;
        }
        if (!Mines.get().getMines().get(mine).isInMine(blockType)) {
            Mines.get().getMinesMessages().getLocalizable("block_not_removed").sendTo(sender);
            return;
        }
        Mines.get().getMines().get(mine).getBlocks().removeIf(x -> x.type == blockType);
        Mines.get().getMinesMessages().getLocalizable("block_deleted").withReplacements(block, mine).sendTo(sender);
        Mines.get().getMines().clearCache();
    }


    @Command(identifier = "mines delete", permissions = {"prison.mines.delete",
        "prison.admin"}, onlyPlayers = false, description = "Deletes a mine")
    public void deleteCommand(CommandSender sender,
        @Arg(name = "mineName", description = "The name of the mine to be deleted") String name) {
        if (!Mines.get().getMines().contains(name)) {
            Mines.get().getMinesMessages().getLocalizable("mine_does_not_exist").sendTo(sender);
            return;
        }
        Mines.get().getMines().remove(Mines.get().getMines().get(name));
        Mines.get().getMinesMessages().getLocalizable("mine_deleted").sendTo(sender);
    }

    @Command(identifier = "mines info", permissions = {"prison.mines.info",
        "prison.admin"}, onlyPlayers = false, description = "Lists basic information about a mine")
    public void infoCommand(CommandSender sender,
        @Arg(name = "mineName", description = "The name of the mine") String name) {
        if (!Mines.get().getMines().contains(name)) {
            Mines.get().getMinesMessages().getLocalizable("mine_does_not_exist").sendTo(sender);
            return;
        }
        Mine m = Mines.get().getMines().get(name);
        sender.sendMessage(Text.titleize(m.getName()));
        if (!m.getWorld().isPresent()) {
            sender.sendMessage("&bWorld: &c&lmissing");
        } else {
            sender.sendMessage("&bWorld: &7" + m.getWorld().get().getName());
        }
        sender.sendMessage(m.getBounds().getMin().toCoordinates() + " to " + m.getBounds().getMax().toCoordinates());
        sender.sendMessage(
            "&bSize: &7" + m.getBounds().getWidth() + "&8x&b" + m.getBounds().getLength() + " &8(&7"
                + m.getBounds().getHeight() + " &bblocks deep&8)");
        if (m.hasSpawn()) {
            sender.sendMessage(
                "&bSpawnpoint: " + m.getSpawn().get().toCoordinates());
        } else {
            sender.sendMessage("&bSpawnpoint: &cnot set");
        }
    }

    @Command(identifier = "mines reset", permissions = {"prison.mines.reset", "prison.admin"})
    public void resetCommand(CommandSender sender,
        @Arg(name = "mineName", description = "The name of the mine to be reset") String name) {
        if (!Mines.get().getMines().contains(name)) {
            Mines.get().getMinesMessages().getLocalizable("mine_does_not_exist").sendTo(sender);
            return;
        }
        try {
            Mines.get().getMines().get(name).reset();
        } catch (Exception e) {
            Mines.get().getMinesMessages().getLocalizable("mine_reset_fail").sendTo(sender);
            Output.get().logError("Couldn't reset mine " + name, e);
        }
        Mines.get().getMinesMessages().getLocalizable("mine_reset").sendTo(sender);
    }


    @Command(identifier = "mines list", permissions = {"prison.mines.list",
        "prison.admin"}, onlyPlayers = false) public void listCommand(CommandSender sender) {
        sender.sendMessage(Text.titleize("/mines list"));
        for (Mine m : Mines.get().getMines()) {
            sender.sendMessage("&8" + m.getName());
        }
    }


    @Command(identifier = "mines redefine", permissions = {"prison.mines.redefine", "prison.admin"})
    public void redefineCommand(CommandSender sender,
        @Arg(name = "mineName", description = "The name of the mine to be redefined") String name) {
        Selection selection = Prison.get().getSelectionManager().getSelection((Player) sender);
        if (!selection.isComplete()) {
            Mines.get().getMinesMessages().getLocalizable("select_bounds").sendTo(sender);
            return;
        }
        if (!Objects.equals(selection.getMin().getWorld().getName(),
            selection.getMax().getWorld().getName())) {
            Mines.get().getMinesMessages().getLocalizable("world_diff").sendTo(sender);
            return;
        }
        if (!Mines.get().getMines().contains(name)) {
            Mines.get().getMinesMessages().getLocalizable("mine_does_not_exist").sendTo(sender);
            return;
        }
        Mines.get().getMines().get(name).setBounds(selection.asBounds());
        Mines.get().getMinesMessages().getLocalizable("mine_redefined").sendTo(sender);
        Mines.get().getMines().clearCache();
    }
}
