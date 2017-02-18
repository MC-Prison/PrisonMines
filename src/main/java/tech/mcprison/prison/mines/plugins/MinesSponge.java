package tech.mcprison.prison.mines.plugins;

import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;
import tech.mcprison.prison.Prison;
import tech.mcprison.prison.mines.Mines;

/**
 * A loadable sponge implementation of mines
 */
@Plugin( //
    id = "prison-mines", //
    name = "PrisonMines", //
    version = Mines.VERSION, //
    dependencies = { //
        @Dependency(id = "prison-sponge") //
    }, //
    description = "A mines module for Prison.", //
    url = "https://mc-prison.tech", //
    authors = {"The MC-Prison Team"} //
) public class MinesSponge {

    @Listener public void onEnable(GameStartedServerEvent event) {
        Prison.get().getModuleManager().registerModule(new Mines(Mines.VERSION));
    }

}