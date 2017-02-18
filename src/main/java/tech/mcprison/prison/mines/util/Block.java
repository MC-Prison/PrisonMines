package tech.mcprison.prison.mines.util;

import tech.mcprison.prison.util.BlockType;

/**
 * Represents a block in a mine
 */
public class Block {
    /**
     * The {@link BlockType} represented by this {@link Block}
     */
    public BlockType type = BlockType.AIR;
    /**
     * The chance of this block appearing in it's associated mine
     */
    public int chance = 100;

    /**
     * Assigns the type and chance
     *
     * @param block
     * @param chance
     * @return
     */
    public Block create(BlockType block, int chance) {
        type = block;
        this.chance = chance;
        return this;
    }
}
