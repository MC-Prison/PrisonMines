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

package tech.mcprison.prison.mines.events;

import tech.mcprison.prison.mines.MinesState;

/**
 * Represents an event called when the state of the Mines module has changed
 */
public class StateChangeEvent {

    private MinesState state;

    public StateChangeEvent(MinesState state) {
        this.state = state;
    }

    /**
     * Gets the state of the mines module
     *
     * @return the state of the mines module
     */
    public MinesState getState() {
        return state;
    }
}
