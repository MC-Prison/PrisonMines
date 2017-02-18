package tech.mcprison.prison.mines;

/**
 * A filter usable with certain methods in {@link MinesList}
 */
public interface MinesFilter {
    /**
     * Used in {@link MinesList#select(MinesFilter)}
     * <p>
     * Check if this {@link MinesFilter} accepts the {@link Mine}. If it does, continue operating.
     * @param c the mine in question
     * @return true if accepted, false otherwise
     */
    boolean accept(Mine c);

    /**
     * Used in {@link MinesList#forEach(MinesFilter)}
     * <p>
     * Execute methods related to the specified {@link Mine}.
     * @param c the mine to execute on
     */
    void action(Mine c);
}
