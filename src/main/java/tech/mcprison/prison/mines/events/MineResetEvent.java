package tech.mcprison.prison.mines.events;

import tech.mcprison.prison.internal.events.Cancelable;
import tech.mcprison.prison.mines.Mine;

/**
 * Represents an event called when a mine is being reset
 */
public class MineResetEvent implements Cancelable {

    private Mine mine;
    private boolean canceled = false; // false by default

    public MineResetEvent(Mine mine) {
        this.mine = mine;
    }

    /**
     * Gets the mine associated with this event
     *
     * @return the mine associated with this event
     */
    public Mine getMine() {
        return mine;
    }

    /**
     * Checks to see if this event has been canceled
     *
     * @return true if this event has been canceled, false otherwise
     */
    public boolean isCanceled() {
        return canceled;
    }

    /**
     * Sets the canceled status of this event
     *
     * @param canceled the new canceled status of this event
     */
    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }
}
