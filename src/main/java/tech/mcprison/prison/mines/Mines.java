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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import tech.mcprison.prison.Prison;
import tech.mcprison.prison.localization.LocaleManager;
import tech.mcprison.prison.mines.events.StateChangeEvent;
import tech.mcprison.prison.mines.util.Miner;
import tech.mcprison.prison.modules.Module;
import tech.mcprison.prison.output.Output;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * The Prison 3 Mines Module
 *
 * @author The MC-Prison Team
 */
public class Mines extends Module {

    /*
     * Fields & Constants
     */

    private static Mines i = null;
    private static MinesState state;
    private MinesConfig config;
    private List<String> worlds;
    private List<Miner> players;
    private LocaleManager localeManager;
    private Gson gson;

    /*
     * Constructor
     */

    public Mines(String version) {
        super("Mines", version, 1);
    }

    /*
     * Methods
     */

    public void enable() {
        i = this;

        initGson();
        initConfig();
        localeManager = new LocaleManager(this);

        initWorlds();
        initPlayers();
        initMines();

        Prison.get().getCommandHandler().registerCommands(new MinesCommands());

    }

    private void initGson() {
        gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
    }

    private void initConfig() {
        config = new MinesConfig();

        File configFile = new File(getDataFolder(), "config.json");

        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
                String json = gson.toJson(config);
                Files.write(configFile.toPath(), json.getBytes());
            } catch (IOException e) {
                Output.get().logError("Failed to create config", e);
                getStatus().toFailed("Failed to create config");
            }
        } else {
            try {
                String json = new String(Files.readAllBytes(configFile.toPath()));
                config = gson.fromJson(json, MinesConfig.class);
            } catch (IOException e) {
                Output.get().logError("Failed to load config", e);
                getStatus().toFailed("Failed to load config");
            }
        }
    }

    private void initWorlds() {
        ListIterator<String> iterator = config.worlds.listIterator();
        worlds = new ArrayList<>();
        while (iterator.hasNext()) {
            worlds.add(iterator.next().toLowerCase());
        }
    }

    private void initPlayers() {
        players = new ArrayList<>();

        if (config.savePlayers) {
            try {
                String json = new String(
                    Files.readAllBytes(new File(getDataFolder(), "/players.json").toPath()));
                players = gson.fromJson(json, players.getClass());
            } catch (IOException e) {
                getLogger().logError("Couldn't load players", e);
                getStatus().toFailed("Failed to load players");
            }
        }
    }

    private void initMines() {
        mines = new MinesList().initialize();
        new MinesListener().init();
        Prison.get().getPlatform().getScheduler().runTaskTimer(mines.getTimerTask(), 20, 20);
    }


    public void disable() {
        setState(MinesState.DISPOSED);
        mines.save();
        if (config.savePlayers) {
            try {
                String json = gson.toJson(players);
                Files.write(new File(getDataFolder(), "players.json").toPath(), json.getBytes());
            } catch (IOException e) {
                Output.get().logError("Couldn't save players.", e);
            }
            getLogger().logInfo("&aSaved players!");
        }
    }


    /*
     * Getters & Setters
     */

    public MinesConfig getConfig() {
        return config;
    }

    public MinesState getState() {
        return state;
    }

    public List<Miner> getPlayers() {
        return players;
    }

    public void addMiner(Miner miner) {
        players.add(miner);
    }

    public static Mines get() {
        return i;
    }

    private MinesList mines;

    public MinesList getMines() {
        return mines;
    }

    public LocaleManager getMinesMessages() {
        return localeManager;
    }

    public List<String> getWorlds() {
        return worlds;
    }

    public void setState(MinesState state) {
        Mines.state = state;
        Prison.get().getEventBus().post(new StateChangeEvent(state));
    }

}
