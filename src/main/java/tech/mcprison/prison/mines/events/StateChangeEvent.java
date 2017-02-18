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
