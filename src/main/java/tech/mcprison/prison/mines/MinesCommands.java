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

import org.apache.commons.lang3.StringUtils;
import tech.mcprison.prison.Prison;
import tech.mcprison.prison.commands.Arg;
import tech.mcprison.prison.commands.Command;
import tech.mcprison.prison.internal.CommandSender;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.localization.Localizable;
import tech.mcprison.prison.mines.util.Block;
import tech.mcprison.prison.mines.util.MinesUtil;
import tech.mcprison.prison.output.BulletedListComponent;
import tech.mcprison.prison.output.ChatDisplay;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.selection.Selection;
import tech.mcprison.prison.util.BlockType;

import java.util.Objects;

/**
 * @author Dylan M. Perks
 */
public class MinesCommands {

    private boolean performCheckMineExists(CommandSender sender, String name) {
        if (!Mines.get().getMines().contains(name)) {
            Mines.get().getMinesMessages().getLocalizable("mine_does_not_exist").sendTo(sender);
            return false;
        }
        return true;
    }

    @Command(identifier = "autosmelt", permissions = {"prison.mines.user",
        "prison.mines.autosmelt"}) public void autosmeltCommand(CommandSender sender) {
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

    @Command(identifier = "mines", onlyPlayers = false)
    public void minesCommand(CommandSender sender) {
        if (sender.hasPermission("prison.mines.admin")) {
            sender.dispatchCommand("mines help");
        } else {
            sender.dispatchCommand("mines gui");
        }
    }

    @Command(identifier = "mines gui", permissions = {"prison.mines.user", "prison.mines.gui"})
    public void minesGuiCommand(Player sender) {
        Mines.get().getMines().createGUI(sender).show(sender);
    }

    @Command(identifier = "mines create", permissions = {"prison.mines.create",
        "prison.mines.admin"})
    public void createCommand(CommandSender sender, @Arg(name = "mineName") String name) {

        Selection selection = Prison.get().getSelectionManager().getSelection((Player) sender);
        if (!selection.isComplete()) {
            Mines.get().getMinesMessages().getLocalizable("select_bounds")
                .sendTo(sender, Localizable.Level.ERROR);
            return;
        }

        if (!selection.getMin().getWorld().getName()
            .equalsIgnoreCase(selection.getMax().getWorld().getName())) {
            Mines.get().getMinesMessages().getLocalizable("world_diff")
                .sendTo(sender, Localizable.Level.ERROR);
            return;
        }

        if (Mines.get().getMines().stream()
            .anyMatch(mine -> mine.getName().equalsIgnoreCase(name))) {
            Mines.get().getMinesMessages().getLocalizable("mine_exists")
                .sendTo(sender, Localizable.Level.ERROR);
            return;
        }

        Mine mine = new Mine().setBounds(selection.asBounds()).setName(name);
        Mines.get().getMines().add(mine);
        Mines.get().getMinesMessages().getLocalizable("mine_created").sendTo(sender);
    }

    @Command(identifier = "mines spawnpoint", permissions = {"prison.mines.spawnpoint",
        "prison.mines.admin"})
    public void spawnpointCommand(CommandSender sender, @Arg(name = "mineName") String name) {

        if (!performCheckMineExists(sender, name)) {
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

    @Command(identifier = "mines block add", permissions = {"prison.mines.addblock",
        "prison.mines.admin"}, onlyPlayers = false, description = "Adds a block to a mine")
    public void addBlockCommand(CommandSender sender, @Arg(name = "mineName") String mine,
        @Arg(name = "block") String block, @Arg(name = "chance") double chance) {
        if (!performCheckMineExists(sender, mine)) {
            return;
        }

        BlockType blockType = BlockType.getBlock(block);
        if (blockType == null) {
            Mines.get().getMinesMessages().getLocalizable("not_a_block").withReplacements(block)
                .sendTo(sender);
            return;
        }

        if (Mines.get().getMines().get(mine).isInMine(blockType)) {
            Mines.get().getMinesMessages().getLocalizable("block_already_added").sendTo(sender);
            return;
        }

        final double[] totalComp = {chance};
        Mines.get().getMines().get(mine).getBlocks()
            .forEach(block1 -> totalComp[0] += block1.chance);
        if (totalComp[0] > 100) {
            Mines.get().getMinesMessages().getLocalizable("mine_full")
                .sendTo(sender, Localizable.Level.ERROR);
            return;
        }

        Mines.get().getMines().get(mine).getBlocks().add(new Block().create(blockType, chance));
        Mines.get().getMinesMessages().getLocalizable("block_added").withReplacements(block, mine)
            .sendTo(sender);
        Mines.get().getMines().clearCache();
    }

    @Command(identifier = "mines block set", permissions = {"priosn.mines.setblock",
        "prison.mines.admin"}, onlyPlayers = false, description = "Changes the percentage of a block in a mine")
    public void setBlockCommand(CommandSender sender, @Arg(name = "mineName") String mine,
        @Arg(name = "block") String block, @Arg(name = "chance") double chance) {
        if (!performCheckMineExists(sender, mine)) {
            return;
        }

        BlockType blockType = BlockType.getBlock(block);
        if (blockType == null) {
            Mines.get().getMinesMessages().getLocalizable("not_a_block").withReplacements(block)
                .sendTo(sender);
            return;
        }

        if (!Mines.get().getMines().get(mine).isInMine(blockType)) {
            Mines.get().getMinesMessages().getLocalizable("block_not_removed").sendTo(sender);
            return;
        }

        final double[] totalComp = {chance};
        Mines.get().getMines().get(mine).getBlocks().forEach(block1 -> {
            if (block1.type == blockType) {
                totalComp[0] -= block1.chance;
            } else {
                totalComp[0] += block1.chance;
            }
        });
        if (totalComp[0] > 100) {
            Mines.get().getMinesMessages().getLocalizable("mine_full")
                .sendTo(sender, Localizable.Level.ERROR);
            return;
        }

        for (Block blockObject : Mines.get().getMines().get(mine).getBlocks()) {
            if (blockObject.type == blockType) {
                blockObject.chance = chance;
            }
        }

        Mines.get().getMinesMessages().getLocalizable("block_set").withReplacements(block, mine)
            .sendTo(sender);
        Mines.get().getMines().clearCache();

    }

    @Command(identifier = "mines block remove", permissions = {"prison.mines.delblock",
        "prison.mines.admin"}, onlyPlayers = false, description = "Deletes a block from a mine")
    public void delBlockCommand(CommandSender sender, @Arg(name = "mineName") String mine,
        @Arg(name = "block", def = "AIR") String block) {

        if (!performCheckMineExists(sender, mine)) {
            return;
        }

        BlockType blockType = BlockType.getBlock(block);
        if (blockType == null) {
            Mines.get().getMinesMessages().getLocalizable("not_a_block").withReplacements(block)
                .sendTo(sender);
            return;
        }

        if (!Mines.get().getMines().get(mine).isInMine(blockType)) {
            Mines.get().getMinesMessages().getLocalizable("block_not_removed").sendTo(sender);
            return;
        }

        Mines.get().getMines().get(mine).getBlocks().removeIf(x -> x.type == blockType);
        Mines.get().getMinesMessages().getLocalizable("block_deleted").withReplacements(block, mine)
            .sendTo(sender);
        Mines.get().getMines().clearCache();
    }


    @Command(identifier = "mines delete", permissions = {"prison.mines.delete",
        "prison.mines.admin"}, onlyPlayers = false, description = "Deletes a mine")
    public void deleteCommand(CommandSender sender, @Arg(name = "mineName") String name) {
        if (!performCheckMineExists(sender, name)) {
            return;
        }

        Mines.get().getMines().remove(Mines.get().getMines().get(name));
        Mines.get().getMinesMessages().getLocalizable("mine_deleted").sendTo(sender);
    }

    @Command(identifier = "mines info", permissions = {"prison.mines.info",
        "prison.mines.admin"}, onlyPlayers = false, description = "Lists basic information about a mine")
    public void infoCommand(CommandSender sender, @Arg(name = "mineName") String name) {
        if (!performCheckMineExists(sender, name)) {
            return;
        }

        Mine m = Mines.get().getMines().get(name);

        ChatDisplay chatDisplay = new ChatDisplay(m.getName());

        String worldName = m.getWorld().isPresent() ? m.getWorld().get().getName() : "&cmissing";
        chatDisplay.text("&3World: &7%s", worldName);

        String minCoords = m.getBounds().getMin().toBlockCoordinates();
        String maxCoords = m.getBounds().getMax().toBlockCoordinates();
        chatDisplay.text("&3Bounds: &7%s &8to &7%s", minCoords, maxCoords);

        chatDisplay.text("&3Size: &7%d&8x&7%d&8x&7%d", Math.round(m.getBounds().getWidth()),
            Math.round(m.getBounds().getHeight()), Math.round(m.getBounds().getLength()));

        String spawnPoint =
            m.getSpawn().isPresent() ? m.getSpawn().get().toBlockCoordinates() : "&cnot set";
        chatDisplay.text("&3Spawnpoint: &7%s", spawnPoint);

        chatDisplay.text("&3Blocks:");
        BulletedListComponent.BulletedListBuilder builder =
            new BulletedListComponent.BulletedListBuilder();

        int totalChance = 0;
        for (Block block : m.getBlocks()) {
            totalChance += Math.round(block.chance);

            String blockName =
                StringUtils.capitalize(block.type.name().replaceAll("_", " ").toLowerCase());
            String percent = Math.round(block.chance) + "%%";
            builder.add("&7%s - %s", percent, blockName);
        }

        if (totalChance < 100) {
            builder.add("&e%s - Air", (100 - totalChance) + "%%");
        }

        chatDisplay.addComponent(builder.build());

        chatDisplay.send(sender);
    }

    @Command(identifier = "mines reset", permissions = {"prison.mines.reset", "prison.mines.admin"})
    public void resetCommand(CommandSender sender, @Arg(name = "mineName") String name) {

        if (!performCheckMineExists(sender, name)) {
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
        "prison.mines.admin"}, onlyPlayers = false) public void listCommand(CommandSender sender) {
        ChatDisplay display = new ChatDisplay("Mines");
        BulletedListComponent.BulletedListBuilder builder =
            new BulletedListComponent.BulletedListBuilder();

        for (Mine m : Mines.get().getMines()) {
            builder.add("&7" + m.getName());
        }
        display.addComponent(builder.build());
        display.send(sender);
    }


    @Command(identifier = "mines redefine", permissions = {"prison.mines.redefine",
        "prison.mines.admin"})
    public void redefineCommand(CommandSender sender, @Arg(name = "mineName") String name) {

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

        if (!performCheckMineExists(sender, name)) {
            return;
        }

        Mines.get().getMines().get(name).setBounds(selection.asBounds());
        Mines.get().getMinesMessages().getLocalizable("mine_redefined").sendTo(sender);
        Mines.get().getMines().clearCache();
    }

    @Command(identifier = "mines wand", permissions = {"prison.mines.wand", "prison.mines.admin"})
    public void wandCommand(Player sender) {
        Prison.get().getSelectionManager().bestowSelectionTool(sender);
    }

}
