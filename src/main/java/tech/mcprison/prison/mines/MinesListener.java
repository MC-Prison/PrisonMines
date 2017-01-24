package tech.mcprison.prison.mines;

import com.google.common.eventbus.Subscribe;
import tech.mcprison.prison.Prison;
import tech.mcprison.prison.events.BlockBreakEvent;

/**
 * Created by DMP9 on 23/01/2017.
 */
public class MinesListener {
    public void init(){
        Prison.get().getEventBus().register(this);}
    @Subscribe public void onBlockBreak(BlockBreakEvent event){
        if (!Mines.get().getMines().allowedToMine(event.getPlayer(),event.getBlockLocation())){
            event.getPlayer().sendMessage("&c&lHey!&r &bYou're not allowed to mine at this mine!");
            event.setCanceled(true);
        }
    }

}
