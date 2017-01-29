package tech.mcprison.prison.mines;

import tech.mcprison.prison.store.AbstractJsonable;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by DMP9 on 08/01/2017.
 */
public class MinesConfig extends AbstractJsonable<MinesConfig> {
    public boolean asyncReset = true;
    public boolean resetMessages = true;
    public boolean multiworld = false;
    public boolean savePlayers = false;
    public int aliveTime = 600;
    public String guiName = "&4Prison";
    public ArrayList<String> worlds =
        new ArrayList<>(Arrays.<String>asList(new String[] {"plots", "mines"}));
    public ArrayList<Integer> resetWarningTimes =
        new ArrayList<>(Arrays.<Integer>asList(new Integer[] {600, 300, 60}));
    public String resetMessage = "&b[&5Prison&b] &2Mines have been reset!";
    public String resetWarning = "&b[&5Prison&b] &2Mines are going to reset in &6%mins% &2mins";
}
