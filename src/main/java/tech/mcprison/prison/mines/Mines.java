package tech.mcprison.prison.mines;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.mines.events.StateChangeEvent;
import tech.mcprison.prison.modules.Module;
import tech.mcprison.prison.output.Output;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by DMP9 on 08/01/2017.
 */
public class Mines extends Module {
    private static Mines i = null;
    private static MinesState state;
    private MinesConfig config;
    private List<String> worlds;

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

    public List<String> getWorlds(){
        return worlds;
    }

    public void enable() {
        getLogger().logInfo("&b========================");
        getLogger().logInfo("&7      Prison Mines      ");
        getLogger().logInfo("&7 (C) The MC-Prison Team ");
        getLogger().logInfo("&b========================");
        i = this;
        getLogger().logInfo("Loading config...");
        config = new MinesConfig();
        File configFile = new File(getDataFolder(),"config.json");
        if (!configFile.exists()){
            try {
                config.toFile(configFile);
            } catch (IOException e) {
                Output.get().logError("Failed to create config",e);
            }
        }else{
            try {
                config = config.fromFile(configFile);
            } catch (IOException e) {
                Output.get().logError("Failed to load config",e);
            }
        }
        ListIterator<String> iterator = config.worlds.listIterator();
        worlds = new ArrayList<>();
        while (iterator.hasNext())
        {
            worlds.add(iterator.next().toLowerCase());
        }
        Prison.get().getCommandHandler().registerCommands(new MinesCommands());
        getLogger().logInfo("Loading mines...");
        mines = new MinesList().initialize();
        Prison.get().getPlatform().getScheduler().runTaskTimer(mines.getTimerTask(),20,20);

    }

    public void setState(MinesState state) {
        this.state = state;
        Prison.get().getEventBus().post(new StateChangeEvent(state));
    }

    public void disable() {
        setState(MinesState.DISPOSED);
        mines.forEach(Mine::save);
    }
}
