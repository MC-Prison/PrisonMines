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

package tech.mcprison.prison.mines.util;

import tech.mcprison.prison.Output;
import tech.mcprison.prison.internal.CommandSender;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.mines.Mines;

import java.util.Optional;

/**
 * A class full of handy static methods
 */
public class MinesUtil {
    /**
     * Represents the different levels of severity in logging.
     * <p>
     * <b>Copied from API 0.2-SNAPSHOT</b>
     * </p>
     * @author Dylan M. Perks
     * @since API 0.2/Mines ALPHA
     */
    public enum LogLevel {
        /**
         * Information severity.
         */
        INFO, /**
         * Warning severity.
         */
        WARNING, /**
         * Error severity.
         */
        ERROR;
    }

    /**
     * Adds the mines prefix to a message
     *
     * @param message the message to add the prefix to
     * @return the new message
     */
    @Deprecated public static String addPrefix(String message) {
        return Mines.get().getMinesMessages().prefix + message;
    }

    /**
     * Send a message to a {@link CommandSender}
     */
    public void sendMessage(CommandSender sender, String message, LogLevel level, Object... args) {
        String prefix = level == LogLevel.INFO ?
            Output.get().INFO_PREFIX :
            level == LogLevel.WARNING ? Output.get().WARNING_PREFIX : Output.get().ERROR_PREFIX;
        sender.sendMessage(prefix + String.format(message, args));
    }

    /**
     * Gets a {@link Miner} for a player
     *
     * @param player the player to get a miner for
     * @return an optional of the miner if a miner has been created for a player, {@link Optional#empty()} otherwise
     */
    public static Optional<Miner> getMiner(Player player) {
        for (Miner miner : Mines.get().getPlayers()) {
            if (miner.getPlayer().get().getUUID().toString()
                .equalsIgnoreCase(player.getUUID().toString())) {
                return Optional.of(miner);
            }
        }
        return Optional.empty();
    }

    /**
     * Checks to see if a {@link Miner} is present for the given {@link Player}
     *
     * @param player the player in question
     * @return {@link Optional#isPresent()} on the miner optional
     */
    public static boolean isPlayerMiner(Player player) {
        return getMiner(player).isPresent();
    }

    /**
     * Checks to see if a {@link Player} is using autosmelt
     *
     * @param player the player in question
     * @return true if the player is using autosmelt
     */
    public static boolean usingAutosmelt(Player player) {
        if (!isPlayerMiner(player)) {
            return false;
        }
        for (Miner miner : Mines.get().getPlayers()) {
            if (miner.getPlayer().get().getUUID().toString()
                .equalsIgnoreCase(player.getUUID().toString()) && miner.isUsingAutosmelt()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks to see if a {@link Player} is using autoblock
     *
     * @param player the player in question
     * @return true if the player is using autoblock
     */
    public static boolean usingAutoblock(Player player) {
        if (!isPlayerMiner(player)) {
            return false;
        }
        for (Miner miner : Mines.get().getPlayers()) {
            if (miner.getPlayer().get().getUUID().toString()
                .equalsIgnoreCase(player.getUUID().toString()) && miner.isUsingAutoblock()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Enables autosmelt for a player
     *
     * @param player the player to enable autosmelt for
     * @throws Exception if, for whatever reason, even after creation of the associated {@link Miner} the optional is still empty
     */
    public static void enableAutosmelt(Player player) throws Exception {
        if (!isPlayerMiner(player)) {
            Mines.get().addMiner(Miner.fromPlayer(player));
        }
        Optional<Miner> miner = getMiner(player);
        if (!miner.isPresent()) {
            throw new Exception("Couldn't register the player as a miner");
        }
        miner.get().enableAutosmelt();
    }

    /**
     * Enables autoblock for a player
     *
     * @param player the player to enable autoblock for
     * @throws Exception if, for whatever reason, even after creation of the associated {@link Miner} the optional is still empty
     */
    public static void enableAutoblock(Player player) throws Exception {
        if (!isPlayerMiner(player)) {
            Mines.get().addMiner(Miner.fromPlayer(player));
        }
        Optional<Miner> miner = getMiner(player);
        if (!miner.isPresent()) {
            throw new Exception("Couldn't register the player as a miner");
        }
        miner.get().enableAutoblock();
    }

    /**
     * Disables autosmelt for a player
     *
     * @param player the player to disable autosmelt for
     * @throws Exception if, for whatever reason, even after creation of the associated {@link Miner} the optional is still empty
     */
    public static void disableAutosmelt(Player player) throws Exception {
        if (!isPlayerMiner(player)) {
            Mines.get().addMiner(Miner.fromPlayer(player));
        }
        Optional<Miner> miner = getMiner(player);
        if (!miner.isPresent()) {
            throw new Exception("Couldn't register the player as a miner");
        }
        miner.get().disableAutosmelt();
    }

    /**
     * Disables autoblock for a player
     *
     * @param player the player to disable autoblock for
     * @throws Exception if, for whatever reason, even after creation of the associated {@link Miner} the optional is still empty
     */
    public static void disableAutoblock(Player player) throws Exception {
        if (!isPlayerMiner(player)) {
            Mines.get().addMiner(Miner.fromPlayer(player));
        }
        Optional<Miner> miner = getMiner(player);
        if (!miner.isPresent()) {
            throw new Exception("Couldn't register the player as a miner");
        }
        miner.get().disableAutoblock();
    }
}
