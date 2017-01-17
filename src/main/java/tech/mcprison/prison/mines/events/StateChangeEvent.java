package tech.mcprison.prison.mines.events;

import tech.mcprison.prison.mines.MinesState;

/**
 * Created by DMP9 on 15/01/2017.
 */
public class StateChangeEvent {

    public boolean canceled;
    public MinesState state;

    public StateChangeEvent(MinesState state){
        this.state = state;
    }

    public boolean isCanceled() {
        return canceled;
    }

    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }
}
