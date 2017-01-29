package tech.mcprison.prison.mines.plugins;

import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;
import tech.mcprison.prison.Prison;
import tech.mcprison.prison.mines.Mines;

/**
 * Created by DMP9 on 22/01/2017.
 */
@Plugin( //
    id = "prison-mines", //
    name = "PrisonMines", //
    version = MinesSponge.Version, //
    dependencies = { //
        @Dependency(id = "prison-sponge") //
    }, //
    description = "A mines module for Prison.", //
    url = "https://mc-prison.tech", //
    authors = {"The MC-Prison Team"} //
) public class MinesSponge {

    static final String Version = "3.0.0-RC";

    @Listener public void onEnable(GameStartedServerEvent event) {
        Prison.get().getModuleManager().registerModule(new Mines(Version));
    }

}