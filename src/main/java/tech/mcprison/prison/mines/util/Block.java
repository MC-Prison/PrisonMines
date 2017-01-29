package tech.mcprison.prison.mines.util;

import tech.mcprison.prison.util.BlockType;

/**
 * Created by DMP9 on 25/01/2017.
 */
public class Block {
    public BlockType type = BlockType.AIR;
    public int chance = 100;

    public Block create(BlockType block, int chance) {
        type = block;
        this.chance = chance;
        return this;
    }
}
