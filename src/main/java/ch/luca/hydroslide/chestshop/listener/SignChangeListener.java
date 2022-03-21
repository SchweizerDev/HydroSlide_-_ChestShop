package ch.luca.hydroslide.chestshop.listener;

import ch.luca.hydroslide.chestshop.ChestShop;
import ch.luca.hydroslide.chestshop.config.LocaleConfig;
import ch.luca.hydroslide.chestshop.shop.PreShop;
import ch.luca.hydroslide.chestshop.shop.Shop;
import ch.luca.hydroslide.chestshop.utils.MathUtil;
import lombok.Getter;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

import java.util.HashMap;
import java.util.Map;

public class SignChangeListener implements Listener {

    @Getter
    private static Map<Player, PreShop> shopCreateCache = new HashMap<>();

    private ChestShop chestShop;

    public SignChangeListener( ChestShop chestShop ) {
        this.chestShop = chestShop;
    }

    @EventHandler
    public void onSignChange( SignChangeEvent event ) {
        Player player = event.getPlayer();

        // Check if is a chest shop
        if ( !this.isShop( player, event.getLines() ) ) {
            return;
        }

        if ( shopCreateCache.containsKey( player ) ) {
            player.sendMessage( this.chestShop.getLocaleConfig().getMessage( "message.sign.create.already-creating" ) );
            event.getBlock().breakNaturally();
            return;
        }

        Location chestLocation = this.getChestLocation( event.getBlock().getLocation() );

        // Check if Chest exists
        if ( chestLocation == null ) {
            // Send player message
            player.sendMessage( this.chestShop.getLocaleConfig().getMessage( "message.sign.create.chest-not-found" ) );

            // Break sign
            event.getBlock().breakNaturally();
            return;
        }

        for ( Shop shop : Shop.getShops() ) {
            if ( shop.getChestLocation().equals( chestLocation ) ) {
                // Send player message
                player.sendMessage( this.chestShop.getLocaleConfig().getMessage( "message.sign.create.already-shop" ) );

                // Break sign
                event.getBlock().breakNaturally();
                return;
            }
        }

        // Create PreShop and store in cache
        PreShop preShop = new PreShop( event.getBlock().getLocation(), chestLocation, player.getUniqueId(), event.getLine( 1 ).equalsIgnoreCase( "kaufen" ),
                Integer.parseInt( event.getLine( 2 ) ), Integer.parseInt( event.getLine( 3 ) ) );
        shopCreateCache.put( player, preShop );

        // Send message
        TextComponent firstText = new TextComponent( this.chestShop.getLocaleConfig().getMessage( "message.sign.create.item-needed-first" ) );
        TextComponent lastText = new TextComponent( this.chestShop.getLocaleConfig().getMessage( "message.sign.create.item-needed-last" ) );
        TextComponent click = new TextComponent( this.chestShop.getLocaleConfig().getMessage( "message.sign.create.item-needed-click" ) );
        click.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder( this.chestShop.getLocaleConfig().getMessage( "message.sign.create.item-needed-click-hover" ) ).create() ) );
        click.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/chestshop abbrechen" ) );

        player.spigot().sendMessage( firstText, click, lastText );

        // Change sign layout
        LocaleConfig localeConfig = this.chestShop.getLocaleConfig();
        event.setLine( 0, localeConfig.getMessage( "sign.item-needed.line1" ) );
        event.setLine( 1, localeConfig.getMessage( "sign.item-needed.line2" ) );
        event.setLine( 2, localeConfig.getMessage( "sign.item-needed.line3" ) );
        event.setLine( 3, localeConfig.getMessage( "sign.item-needed.line4" ) );
    }

    /**
     * Check if the player is creating a shop sign
     *
     * @param player of the sign creator
     * @param lines  of the sign
     * @return if the player is creating an shop sign
     */
    private boolean isShop( Player player, String[] lines ) {
        return lines[0].equalsIgnoreCase( player.getName() )
                && ( lines[1].equalsIgnoreCase( "kaufen" ) || lines[1].equalsIgnoreCase( "verkaufen" ) )
                && MathUtil.isInt( lines[2] )
                && MathUtil.isInt( lines[3] );
    }

    /**
     * Get the chest location of a sign location
     *
     * @param signLocation of the shop
     * @return the chest location. the result can be null if the chest not exists
     */
    private Location getChestLocation( Location signLocation ) {
        Location under = signLocation.clone().subtract( 0.0D, 1.0D, 0.0D );

        // Check if chest under the sign
        if ( under.getBlock().getState() instanceof Chest ) {
            return under;
        }

        // Check if the sign hanging on the chest
        Sign blockSign = (Sign) signLocation.getBlock().getState();

        WallSign wallSign = (WallSign) signLocation.getBlock().getState().getBlockData();
        Block blockAttached = signLocation.getBlock().getRelative(wallSign.getFacing().getOppositeFace());
        if ( blockAttached.getState() instanceof Chest ) {
            return blockAttached.getLocation();
        }

        // Chest not found
        return null;
    }
}
