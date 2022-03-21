package ch.luca.hydroslide.chestshop.command;

import ch.luca.hydroslide.chestshop.ChestShop;
import ch.luca.hydroslide.chestshop.shop.Shop;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AdminShopCommand implements CommandExecutor {

    private ChestShop chestShop;

    public AdminShopCommand( ChestShop chestShop ) {
        this.chestShop = chestShop;
    }

    @Override
    public boolean onCommand( CommandSender commandSender, Command command, String label, String[] args ) {
        Player player = (Player) commandSender;
        if ( !player.hasPermission( "chestshop.command.adminshop" ) ) {
            player.sendMessage( this.chestShop.getLocaleConfig().getMessage( "no-perms" ) );
            return true;
        }
        for ( Shop shop : Shop.getShops() ) {
            this.chestShop.getPlayerRepository().getName( shop.getOwner(), name -> {
                player.sendMessage( this.chestShop.getLocaleConfig().getMessage( "prefix" ) + " §e" + name + "§7: §c" + shop.getAmount() + "§7x §a" + shop.getItem().getType().name() + " §7für §a" + shop.getPrice() + " Coins. §7(Loc: "
                        + shop.getSignLocation().getBlockX() + "|" + shop.getSignLocation().getBlockY() + "|" + shop.getSignLocation().getBlockZ() + "|" + shop.getSignLocation().getWorld().getName() + ")" );
            } );
        }
        return true;
    }
}
