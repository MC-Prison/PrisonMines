package tech.mcprison.prison.mines;

import tech.mcprison.prison.store.AbstractJsonable;

/**
 * Created by DMP9 on 31/01/2017.
 */
public class MinesMessages extends AbstractJsonable<MinesMessages> {
    public String prefix = "&3Mines&7 /// &f";
    public String notAllowed = "&c&lHey!&r &bYou're not allowed to mine at this mine!";
    public String autosmeltEnable = "&aAutosmelt enabled!";
    public String autosmeltEnable_fail =
        "&cCouldn't enable autosmelt. Contact a server operator for details.";
    public String autosmeltDisable = "&aAutosmelt disabled!";
    public String autosmeltDisable_fail =
        "&cCouldn't disable autosmelt. Contact a server operator for details.";
    public String teleported = "&bTeleported to mine '&7%name%&b'";
    public String mineReset = "&aMine reset!";
    public String mineReset_fail = "&cCouldn't reset mine. Check the log for details.";
    public String mineCreated = "&aMine created successfully!";
    public String mineDeleted = "&aMine deleted successfully!";
    public String selectBoundaries = "&cYou need to select the mine boundaries first!";
    public String worldDiff = "&cCan't create mines across two different worlds!";
    public String mineExists = "&cThat mine already exists!";
    public String mineDoesntExist = "&cThat mine doesn't exist!";
    public String spawnpoint = "&aSpawnpoint set!";
    public String spawnpointSameWorldMine =
        "&cThe spawnpoint must be in the same world as the mine!";
    public String notABlock = "&6%block%&c is not a block!";
    public String blockAlreadyAdded = "&cThat block has already been added!";
    public String blockAdded = "&aAdded block &6%block%&a to mine &6%mine%";
    public String blockNotAdded = "&cThat block isn't in the mine!";
    public String blockDeleted = "&aRemoved block &6%block%&a from mine &6%mine%";
    public String mineRedefined = "&aMine redefined successfully!";
}
