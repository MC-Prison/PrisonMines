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

package tech.mcprison.prison.mines.plugins;

import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;
import tech.mcprison.prison.Prison;
import tech.mcprison.prison.mines.PrisonMines;

/**
 * A loadable sponge implementation of mines
 */
@Plugin( //
    id = "prison-mines", //
    name = "PrisonMines", //
    version = MinesSponge.VERSION, //
    dependencies = { //
        @Dependency(id = "prison-sponge") //
    }, //
    description = "A mines module for Prison.", //
    url = "https://mc-prison.tech", //
    authors = {"The MC-Prison Team"} //
) public class MinesSponge {

    static final String VERSION = "3.0.0-SNAPSHOT";

    @Listener public void onEnable(GameStartedServerEvent event) {
        Prison.get().getModuleManager().registerModule(new PrisonMines(VERSION));
    }

}