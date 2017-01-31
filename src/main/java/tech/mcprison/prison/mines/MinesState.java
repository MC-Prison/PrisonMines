package tech.mcprison.prison.mines;

/**
 * Represents a state of the Mines module
 */
public enum MinesState {
    /**
     * The mines module has been disabled.
     */
    DISPOSED, /**
     * The mines module is loaded and working
     */
    INITIALIZED, /**
     * The mines module is loading.
     */
    INITIALIZING
}
