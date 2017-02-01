package tech.mcprison.prison.mines;

import com.google.common.eventbus.Subscribe;
import tech.mcprison.prison.Prison;
import tech.mcprison.prison.block.Block;
import tech.mcprison.prison.events.BlockBreakEvent;
import tech.mcprison.prison.internal.ItemStack;
import tech.mcprison.prison.mines.util.MinesUtil;
import tech.mcprison.prison.util.BlockType;
import tech.mcprison.prison.util.Gamemode;

import java.util.List;

/**
 * Represents a class the Mines module uses to listen for events. There is no external use for this class.
 */
public class MinesListener {
    /**
     * Initializes the mines listener
     */
    public void init() {
        Prison.get().getEventBus().register(this);
    }

    /**
     * Called when a block is broken.
     * @param event the event passed by prison-core
     */
    @Subscribe public void onBlockBreak(BlockBreakEvent event) {
        if (!Mines.get().getMines().allowedToMine(event.getPlayer(), event.getBlockLocation())) {
            event.getPlayer().sendMessage("&c&lHey!&r &bYou're not allowed to mine at this mine!");
            event.setCanceled(true);
            return;
        }
        if (Mines.get().getMines().isInMine(event.getBlockLocation()) && event.getPlayer().getGamemode() == Gamemode.SURVIVAL) {
            event.setCanceled(true);
            Block block = event.getBlockLocation().getBlockAt();
            List<ItemStack> drops = block.getDrops();
            if (MinesUtil.usingAutosmelt(event.getPlayer())) {
                for (ItemStack drop : drops) {
                    if (drop.getMaterial() == BlockType.GOLD_ORE) {
                        drops.set(drops.indexOf(drop),
                            new ItemStack("Gold Ingot", drop.getAmount(), BlockType.GOLD_INGOT,
                                drop.getLore().toArray(new String[drop.getLore().size()])));
                    }
                    if (drop.getMaterial() == BlockType.IRON_ORE) {
                        drops.set(drops.indexOf(drop),
                            new ItemStack("Iron Ingot", drop.getAmount(), BlockType.IRON_INGOT,
                                drop.getLore().toArray(new String[drop.getLore().size()])));
                    }
                }
            }
            drops.forEach(x -> event.getPlayer().give(x));
            block.setType(BlockType.AIR);
        }
    }
}