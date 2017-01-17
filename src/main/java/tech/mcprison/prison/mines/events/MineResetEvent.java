package tech.mcprison.prison.mines.events;

import tech.mcprison.prison.events.Cancelable;
import tech.mcprison.prison.mines.Mine;

/**
 * Created by DMP9 on 15/01/2017.
 */
public class MineResetEvent implements Cancelable {

    private Mine mine;
    private boolean canceled = false; // false by default

    public MineResetEvent(Mine mine) {
        this.mine = mine;
    }

    public Mine getMine() {
        return mine;
    }

    public boolean isCanceled() {
        return canceled;
    }

    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }
}
