package tech.mcprison.prison.mines;

import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.mines.util.Miner;
import tech.mcprison.prison.store.AbstractJsonable;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Represents the Mines configuration file.
 */
public class MinesConfig extends AbstractJsonable<MinesConfig> {
    /**
     * True if randomized blocks for mines should be cached for faster resets.
     * False otherwise
     */
    public boolean asyncReset = true;
    /**
     * True if reset warnings an reset broadcasts should be enabled.
     * False otherwise
     */
    public boolean resetMessages = true;
    /**
     * True if broadcasts should only be enabled in the worlds specified in the <i>worlds</i> list.
     * False otherwise.
     *
     * @see MinesConfig#worlds
     */
    public boolean multiworld = false;
    /**
     * True if {@link Miner}s should be saved.
     * False otherwise.
     */
    public boolean savePlayers = false;
    /**
     * True if only blocks that are air should be replaced.
     * False otherwise
     */
    public boolean fillMode = false;
    /**
     * The duration between mine resets in seconds.
     */
    public int aliveTime = 600;
    /**
     * The title of the Mines GUI
     *
     * @see MinesList#createGUI(Player)
     */
    public String guiName = "&4Prison";
    /**
     * The worlds that reset messages should be broadcasted to.
     * Ignored if multiworld is disabled.
     *
     * @see MinesConfig#multiworld
     */
    public ArrayList<String> worlds =
        new ArrayList<>(Arrays.<String>asList(new String[] {"plots", "mines"}));
    /**
     * The time between mine reset warnings.
     * Ignored if resetMessages is disabled.
     *
     * @see MinesConfig#resetMessages
     */
    public ArrayList<Integer> resetWarningTimes =
        new ArrayList<>(Arrays.<Integer>asList(new Integer[] {600, 300, 60}));
    /**
     * The message broadcasted when all mines have been reset.
     * Ignored if resetMessages is disabled.
     *
     * @see MinesConfig#resetMessages
     */
    public String resetMessage = "&b[&5Prison&b] &2Mines have been reset!";
    /**
     * The message broadcasted at the specified times.
     * Ignored if resetMessages is disabled.
     *
     * @see MinesConfig#resetMessages
     * @see MinesConfig#resetWarningTimes
     */
    public String resetWarning = "&b[&5Prison&b] &2Mines are going to reset in &6%mins% &2mins";
}
