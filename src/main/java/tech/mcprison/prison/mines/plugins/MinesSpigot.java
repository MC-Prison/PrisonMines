package tech.mcprison.prison.mines.plugins;

import org.bukkit.plugin.java.JavaPlugin;
import tech.mcprison.prison.Prison;
import tech.mcprison.prison.mines.Mines;

/**
 * A loadable spigot implementation of mines
 */
public class MinesSpigot extends JavaPlugin {
    public void onEnable() {
        Prison.get().getModuleManager().registerModule(new Mines(Mines.VERSION));
    }
}
