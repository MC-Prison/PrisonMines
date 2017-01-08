package tech.mcprison.prison.mines;

import tech.mcprison.prison.modules.Module;
import tech.mcprison.prison.output.Output;

/**
 * Created by DMP9 on 08/01/2017.
 */
public class Mines extends Module {
    private static Mines i = null;
    private MinesConfig config;

    public MinesConfig getConfig() {
        return config;
    }

    public static Mines get() {
        return i;
    }

    private MinesList mines;

    public MinesList getCells() {
        return mines;
    }

    private static String version = "Unregistered-Snapshot";
    private boolean initialized = false;

    public boolean getInitialized() {
        return initialized;
    }

    public Mines(String version) {
        super("Mines", version, 30);
    }

    public void enable() {
        Output.get().logInfo("&bEnabling &ePrison Mines&b...");
    }
}
