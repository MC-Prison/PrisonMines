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

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.internal.Player;

import java.util.Optional;
import java.util.UUID;

/**
 * Represents a player capable of using autosmelt and autoblock
 */
public class Miner {
    private String username = "";
    private String uuid = "";
    private boolean autosmelt = false;
    private boolean autoblock = false;

    /**
     * Gets the player associated with this {@link Miner}
     *
     * @return the player associated with this instance
     */
    public Optional<Player> getPlayer() {
        return Prison.get().getPlatform().getPlayer(UUID.fromString(uuid));
    }

    /**
     * Gets the miners autosmelt status
     *
     * @return true if this miner is using autosmelt, false otherwise
     */
    public boolean isUsingAutosmelt() {
        return autosmelt;
    }

    /**
     * Gets the miners autoblock status
     *
     * @return true if this miner is using autoblock, false otherwise
     */
    public boolean isUsingAutoblock() {
        return autoblock;
    }

    /**
     * Creates a {@link Miner} instance from a {@link Player}
     *
     * @param player the player to create an instance for
     * @return the new instance
     */
    public static Miner fromPlayer(Player player) {
        Miner miner = new Miner();
        miner.username = player.getName();
        miner.uuid = player.getUUID().toString();
        return miner;
    }

    /**
     * Changes this miners autosmelt status to true
     */
    public void enableAutosmelt() {
        autosmelt = true;
    }

    /**
     * Changes this miners autoblock status to true
     */
    public void enableAutoblock() {
        autoblock = true;
    }

    /**
     * Changes this miners autosmelt status to false
     */
    public void disableAutosmelt() {
        autosmelt = false;
    }

    /**
     * Changes this miners autoblock status to false
     */
    public void disableAutoblock() {
        autoblock = false;
    }
}
