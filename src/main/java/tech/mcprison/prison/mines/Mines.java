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

import tech.mcprison.prison.Output;
import tech.mcprison.prison.Prison;
import tech.mcprison.prison.mines.events.StateChangeEvent;
import tech.mcprison.prison.mines.util.Miner;
import tech.mcprison.prison.modules.Module;

import java.io.File;
import java.io.FileWriter;
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
    /**
     * The Jenkins build associated with this commit/release
     */
    public static final String JENKINS_BUILD = "111";
    /**
     * If this version is a release or a Development Build
     */
    public static final boolean DEVELOPMENT_BUILD = false;
    /**
     * The version of Mines
     */
    public static final String VERSION = "3.0.0";

    private static Mines i = null;
    private static MinesState state;
    private MinesConfig config;
    private List<String> worlds;
    private List<Miner> players;
    private MinesMessages messages;

    /**
     * Gets the Mines configuration
     *
     * @return the config
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

    public MinesMessages getMinesMessages() {
        return messages;
    }

    public Mines(String version) {
        super("Mines", version, 1);
    }

    public List<String> getWorlds() {
        return worlds;
    }

    public void enable() {
        getLogger().logInfo("&b========================");
        getLogger().logInfo("&d      Prison Mines      ");
        getLogger().logInfo("&d (C) The MC-Prison Team ");
        getLogger().logInfo("&b========================");
        i = this;
        if (DEVELOPMENT_BUILD) {
            getLogger().logInfo(
                "You are using a Mines development build (Jenkins Build #" + JENKINS_BUILD + ")");
        } else {
            getLogger()
                .logInfo("You are using Mines v" + getVersion() + " (#" + JENKINS_BUILD + ")");
        }
        getLogger().logInfo("Loading config...");
        config = new MinesConfig();
        messages = new MinesMessages();
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
        File msgFile = new File(getDataFolder(), "messages.json");
        if (!msgFile.exists()) {
            try {
                config.toFile(msgFile);
            } catch (IOException e) {
                Output.get().logError("Failed to create messages", e);
            }
        } else {
            try {
                config = config.fromFile(msgFile);
            } catch (IOException e) {
                Output.get().logError("Failed to load messages", e);
            }
        }
        ListIterator<String> iterator = config.worlds.listIterator();
        worlds = new ArrayList<>();
        while (iterator.hasNext()) {
            worlds.add(iterator.next().toLowerCase());
        }
        Prison.get().getCommandHandler().registerCommands(new MinesCommands());
        if (config.savePlayers) {
            getLogger().logInfo("Loading players...");
            try {
                String json = new String(
                    Files.readAllBytes(new File(getDataFolder(), "/players.json").toPath()));
                players = Prison.get().getGson().fromJson(json, players.getClass());
            } catch (IOException e) {
                getLogger().logError("Couldn't load players", e);
            }
        }
        getLogger().logInfo("Loading mines...");
        mines = new MinesList().initialize();
        new MinesListener().init();
        Prison.get().getPlatform().getScheduler().runTaskTimer(mines.getTimerTask(), 20, 20);

    }

    public void setState(MinesState state) {
        this.state = state;
        Prison.get().getEventBus().post(new StateChangeEvent(state));
    }

    public void disable() {
        setState(MinesState.DISPOSED);
        mines.forEach(Mine::save);
        if (config.savePlayers) {
            try {
                FileWriter fileWriter = new FileWriter(new File(getDataFolder(), "/players.json"));
                Prison.get().getGson().toJson(players, fileWriter);
                fileWriter.close();
            } catch (IOException e) {
                Output.get().logError("Couldn't save players.", e);
            }
            getLogger().logInfo("&aSaved players!");
        }
    }
}
