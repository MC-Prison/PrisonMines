package tech.mcprison.prison.mines;

import tech.mcprison.prison.store.AbstractJsonable;

/**
 * Represents the messages sent to the players, customizable by the end-user.
 */
public class MinesMessages extends AbstractJsonable<MinesMessages> {
    /**
     * The chat prefix to use in all messages
     */
    public String prefix = "&3Mines &7» &f";
    /**
     * The message sent to the player when they're not allowed to mine in a mine
     */
    public String notAllowed = "&c&lHey!&r &bYou're not allowed to mine at this mine!";
    /**
     * The messag sent to the player when they enable autosmelt
     */
    public String autosmeltEnable = "&aAutosmelt enabled!";
    /**
     * The message sent to the player when enabling autosmelt fails
     */
    public String autosmeltEnable_fail =
        "&cCouldn't enable autosmelt. Contact a server operator for details.";
    /**
     * The message sent to the player when they disable autosmelt
     */
    public String autosmeltDisable = "&aAutosmelt disabled!";
    /**
     * The message sent to the player when disabling autosmelt fails
     */
    public String autosmeltDisable_fail =
        "&cCouldn't disable autosmelt. Contact a server operator for details.";
    /**
     * The message sent to the player when they teleport to a mine
     * <ul>
     * <li><b>%name%</b> is replaced with the name of the mine</li>
     * </ul>
     */
    public String teleported = "&bTeleported to mine '&7%name%&b'";
    /**
     * The message sent to the player when they reset a mine
     */
    public String mineReset = "&aMine reset!";
    /**
     * The message sent to the player when resetting a mine fails
     */
    public String mineReset_fail = "&cCouldn't reset mine. Check the log for details.";
    /**
     * The message sent to the player when they create a mine
     */
    public String mineCreated = "&aMine created successfully!";
    /**
     * The message sent to the player when they delete a mine
     */
    public String mineDeleted = "&aMine deleted successfully!";
    /**
     * The message sent to the player when they try to create/redefine a mine,
     * but hasn't selected the boundaries
     */
    public String selectBoundaries = "&cYou need to select the mine boundaries first!";
    /**
     * The message sent to the player when they try to create a mine with
     * positions in different worlds
     */
    public String worldDiff = "&cCan't create mines across two different worlds!";
    /**
     * The message sent to the player when they try to create a mine but
     * a mine with the same name already exists
     */
    public String mineExists = "&cThat mine already exists!";
    /**
     * The message sent to the player when the mine name used in a command
     * doesn't exist
     */
    public String mineDoesntExist = "&cThat mine doesn't exist!";
    /**
     * The message sent to the player when they define a spawnpoint for a
     * mine.
     */
    public String spawnpoint = "&aSpawnpoint set!";
    /**
     * The message sent to the player when they try to define a spawnpoint
     * for a mine, but fail because they need to be in the same world as
     * the mine.
     */
    public String spawnpointSameWorldMine =
        "&cThe spawnpoint must be in the same world as the mine!";
    /**
     * The message sent to the player when a block name they are referencing
     * can't be found in the {@link tech.mcprison.prison.util.ItemManager}
     * <ul>
     * <li><b>%block%</b> is replaced with the block in question</li>
     * </ul>
     */
    public String notABlock = "&6%block%&c is not a block!";
    /**
     * The message sent to the player when they try to add a block to a mine,
     * but the block has already been added
     */
    public String blockAlreadyAdded = "&cThat block has already been added!";
    /**
     * The message sent to the player when they have added a block to a mine
     * <ul>
     * <li><b>%block%</b> is replaced with the block added</li>
     * <li><b>%mine%</b> is replaced with the name of the mine</li>
     * </ul>
     */
    public String blockAdded = "&aAdded block &6%block%&a to mine &6%mine%";
    /**
     * The message sent to the player when they try to remove a block from
     * a mine, but the block in question is not in the mine.
     */
    public String blockNotAdded = "&cThat block isn't in the mine!";
    /**
     * The message sent to the player when they have removed a block from a mine
     * <ul>
     * <li><b>%block%</b> is replaced with the block added</li>
     * <li><b>%mine%</b> is replaced with the name of the mine</li>
     * </ul>
     */
    public String blockDeleted = "&aRemoved block &6%block%&a from mine &6%mine%";
    /**
     * The message sent to the player when they have redefined a mine
     */
    public String mineRedefined = "&aMine redefined successfully!";
    /**
     * The message sent to the player when the world a mine is using is missing
     */
    public String worldMissing = "&cThe world this mine was created in can't be found.";
}
