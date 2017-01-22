package tech.mcprison.prison.mines;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.mines.events.StateChangeEvent;
import tech.mcprison.prison.modules.Module;
import tech.mcprison.prison.output.Output;

/**
 * Created by DMP9 on 08/01/2017.
 */
public class Mines extends Module {
    private static Mines i = null;
    private static MinesState state;
    private MinesConfig config;

    public MinesConfig getConfig() {
        return config;
    }

    public MinesState getState() {
        return state;
    }

    public static Mines get() {
        return i;
    }

    private MinesList mines;

    public MinesList getMines() {
        return mines;
    }

    public Mines(String version) {
        super("Mines", version, 1);
    }

    public void enable() {
        Output.get().logInfo("&bEnabling &7Prison Mines&b...");
        Prison.get().getCommandHandler().registerCommands(new MinesCommands());
        mines = new MinesList().initialize();
    }

    public void setState(MinesState state) {
        this.state = state;
        Prison.get().getEventBus().post(new StateChangeEvent(state));
    }

    public void disable() {
        setState(MinesState.DISPOSED);
    }
}
