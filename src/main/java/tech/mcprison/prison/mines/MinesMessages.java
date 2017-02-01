package tech.mcprison.prison.mines;

import tech.mcprison.prison.store.AbstractJsonable;

/**
 * Created by DMP9 on 31/01/2017.
 */
public class MinesMessages extends AbstractJsonable<MinesMessages> {
    public String prefix = "&3Mines&7 /// &f";
    public String notAllowed = "&c&lHey!&r &bYou're not allowed to mine at this mine!";
    public String teleported = "&bTeleported to mine '&7%name%&b'";
    public String mineReset = "&aMine reset!";
    public String mineReset_fail = "&cCouldn't reset mine. Check the log for details.";
}
