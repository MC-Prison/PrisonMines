package tech.mcprison.prison.mines.plugins;

import org.bukkit.plugin.java.JavaPlugin;
import tech.mcprison.prison.Prison;
import tech.mcprison.prison.mines.Mines;

/**
 * Created by DMP9 on 22/01/2017.
 */
public class MinesSpigot extends JavaPlugin {
    public void onEnable() {
        Prison.get().getModuleManager().registerModule(new Mines(getDescription().getVersion()));
    }
}
