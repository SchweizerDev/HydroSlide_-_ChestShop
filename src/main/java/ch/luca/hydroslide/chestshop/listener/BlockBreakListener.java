package ch.luca.hydroslide.chestshop.listener;

import ch.luca.hydroslide.chestshop.ChestShop;
import ch.luca.hydroslide.chestshop.shop.PreShop;
import ch.luca.hydroslide.chestshop.shop.Shop;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakListener implements Listener {

    private ChestShop chestShop;

    public BlockBreakListener( ChestShop chestShop ) {
        this.chestShop = chestShop;
    }

    @EventHandler
    public void onBlockBreak( BlockBreakEvent event ) {
        Player player = event.getPlayer();

        switch ( event.getBlock().getType() ) {
            case CHEST: {
                // Check if is in create process
                if ( SignChangeListener.getShopCreateCache().containsKey( player ) ) {
                    // Get PreShop
                    PreShop preShop = SignChangeListener.getShopCreateCache().get( player );

                    // Check if location is the same
                    if ( event.getBlock().getLocation().equals( preShop.getChestLocation() ) ) {
                        // Remove
                        SignChangeListener.getShopCreateCache().remove( player );

                        // Send message
                        player.sendMessage( this.chestShop.getLocaleConfig().getMessage( "message.sign.create.cancel" ) );
                        return;
                    }
                }
                // Get Shop
                Shop shop = Shop.getShopByChestLoc( event.getBlock().getLocation() );

                // Check if exists
                if ( shop != null ) {
                    event.setCancelled( true );

                    // Check if owner clicking
                    if ( shop.getOwner().equals( player.getUniqueId() ) ) {
                        player.sendMessage( this.chestShop.getLocaleConfig().getMessage( "message.chest.destroy" ) );
                        return;
                    }
                    player.sendMessage( this.chestShop.getLocaleConfig().getMessage( "message.chest.not-own-shop" ) );
                }
                return;
            }
            case ACACIA_SIGN:
            case SPRUCE_SIGN:
            case ACACIA_WALL_SIGN:
            case BIRCH_SIGN:
            case BIRCH_WALL_SIGN:
            case CRIMSON_SIGN:
            case CRIMSON_WALL_SIGN:
            case SPRUCE_WALL_SIGN:
            case DARK_OAK_SIGN:
            case DARK_OAK_WALL_SIGN:
            case JUNGLE_SIGN:
            case JUNGLE_WALL_SIGN:
            case OAK_SIGN:
            case OAK_WALL_SIGN:
            case WARPED_SIGN:
            case WARPED_WALL_SIGN: {
                // Check if is in create process
                if ( SignChangeListener.getShopCreateCache().containsKey( player ) ) {
                    PreShop preShop = SignChangeListener.getShopCreateCache().get( player );

                    // Check if the location is the same
                    if ( event.getBlock().getLocation().equals( preShop.getSignLocation() ) ) {
                        SignChangeListener.getShopCreateCache().remove( player );

                        // Send message
                        player.sendMessage( this.chestShop.getLocaleConfig().getMessage( "message.sign.create.cancel" ) );
                        return;
                    }
                }
                // Get Shop
                Shop shop = Shop.getShopBySignLoc( event.getBlock().getLocation() );

                // Check if exists
                if ( shop != null ) {
                    // Check if Owner clicking or has permissions
                    if ( !shop.getOwner().equals( player.getUniqueId() ) && !player.hasPermission( "chestshop.delete.other" ) ) {
                        event.setCancelled( true );
                        player.sendMessage( this.chestShop.getLocaleConfig().getMessage( "message.chest.not-own-shop" ) );
                        return;
                    }

                    // Remove shop
                    shop.remove();

                    // Send message
                    player.sendMessage( this.chestShop.getLocaleConfig().getMessage(
                            ( shop.getOwner().equals( player.getUniqueId() ) ? "message.sign.destroy" : "message.sign.destroy-other" ) ) );
                    return;
                }
            }
            default:
                return;
        }
    }
}
