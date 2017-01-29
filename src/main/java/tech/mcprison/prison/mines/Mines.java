package tech.mcprison.prison.mines;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.mines.events.StateChangeEvent;
import tech.mcprison.prison.mines.util.Miner;
import tech.mcprison.prison.modules.Module;
import tech.mcprison.prison.output.Output;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
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
    private List<Miner> players;

    public MinesConfig getConfig() {
        return config;
    }

    public MinesState getState() {
        return state;
    }
    public List<Miner> getPlayers() {return players;}
    public void addMiner(Miner miner){players.add(miner);}

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

    public List<String> getWorlds() {
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
        players = new ArrayList<>();
        File configFile = new File(getDataFolder(), "config.json");
        if (!configFile.exists()) {
            try {
                config.toFile(configFile);
            } catch (IOException e) {
                Output.get().logError("Failed to create config", e);
            }
        } else {
            try {
                config = config.fromFile(configFile);
            } catch (IOException e) {
                Output.get().logError("Failed to load config", e);
            }
        }
        ListIterator<String> iterator = config.worlds.listIterator();
        worlds = new ArrayList<>();
        while (iterator.hasNext()) {
            worlds.add(iterator.next().toLowerCase());
        }
        Prison.get().getCommandHandler().registerCommands(new MinesCommands());
        if (config.savePlayers){
            getLogger().logInfo("Loading players...");
            try {
                String json = new String(Files.readAllBytes(new File(getDataFolder(),"/players.json").toPath()));
                players = Prison.get().getGson().fromJson(json,players.getClass());
            } catch (IOException e) {
                getLogger().logError("Couldn't load players",e);
            }
        }
        getLogger().logInfo("Loading mines...");
        mines = new MinesList().initialize();
        Prison.get().getPlatform().getScheduler().runTaskTimer(mines.getTimerTask(), 20, 20);

    }

    public void setState(MinesState state) {
        this.state = state;
        Prison.get().getEventBus().post(new StateChangeEvent(state));
    }

    public void disable() {
        setState(MinesState.DISPOSED);
        mines.forEach(Mine::save);
        if (config.savePlayers){
            try {
                FileWriter fileWriter = new FileWriter(new File(getDataFolder(),"/players.json"));
                Prison.get().getGson().toJson(players,fileWriter);
                fileWriter.close();
            } catch (IOException e) {
                Output.get().logError("Couldn't save players.",e);
            }
            getLogger().logInfo("&aSaved players!");
        }
    }
}
