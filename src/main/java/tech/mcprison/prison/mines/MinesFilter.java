package tech.mcprison.prison.mines;

/**
 * Created by DMP9 on 09/01/2017.
 */
public interface MinesFilter {
    boolean accept(Mine c);
    void action(Mine c);
}
