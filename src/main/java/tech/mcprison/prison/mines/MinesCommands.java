package tech.mcprison.prison.mines;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.commands.Command;
import tech.mcprison.prison.internal.CommandSender;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.selection.Selection;

/**
 * Created by DMP9 on 14/01/2017.
 */
public class MinesCommands {
    //@Command(identifier = "mines",permissions = "prison.mines.gui")
    //public void minesCommand(CommandSender sender){
    //    Mines.get().getMines().createGUI().show((Player)sender);
    //}

    @Command(identifier = "mines create",permissions = { "prison.mines.create", "prison.admin"})
    public void createCommand(CommandSender sender) {
        Selection selection = Prison.get().getSelectionManager().getSelection((Player)sender);
        if (!selection.isComplete()){
            sender.sendMessage("&bYou need to select the mine boundaries first.");
            return;
        }
        Mine mine = new Mine();

    }
}
