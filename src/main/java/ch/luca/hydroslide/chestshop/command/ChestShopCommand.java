package ch.luca.hydroslide.chestshop.command;

import ch.luca.hydroslide.chestshop.ChestShop;
import ch.luca.hydroslide.chestshop.listener.SignChangeListener;
import ch.luca.hydroslide.chestshop.shop.PreShop;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChestShopCommand implements CommandExecutor {

    private ChestShop chestShop;

    public ChestShopCommand( ChestShop chestShop ) {
        this.chestShop = chestShop;
    }

    @Override
    public boolean onCommand( CommandSender commandSender, Command command, String label, String[] args ) {
        Player player = (Player) commandSender;
        if ( args.length == 1 ) {
            if ( args[0].equalsIgnoreCase( "abbrechen" ) ) {
                if ( SignChangeListener.getShopCreateCache().containsKey( player ) ) {
                    PreShop preShop = SignChangeListener.getShopCreateCache().get( player );
                    // Remove sign
                    preShop.getSignLocation().getBlock().breakNaturally();
                    // Remove from cache
                    SignChangeListener.getShopCreateCache().remove( player );
                    // Send message
                    player.sendMessage( this.chestShop.getLocaleConfig().getMessage( "message.sign.create.cancel" ) );
                }
            }
        }
        return true;
    }
}
