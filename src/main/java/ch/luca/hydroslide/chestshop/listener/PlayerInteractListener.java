package ch.luca.hydroslide.chestshop.listener;

import ch.luca.hydroslide.chestshop.ChestShop;
import ch.luca.hydroslide.chestshop.shop.PreShop;
import ch.luca.hydroslide.chestshop.shop.Shop;
import ch.luca.hydroslide.chestshop.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class PlayerInteractListener implements Listener {

    private ChestShop chestShop;

    public PlayerInteractListener( ChestShop chestShop ) {
        this.chestShop = chestShop;
    }

    @EventHandler
    public void onInteract( PlayerInteractEvent event ) {
        Player player = event.getPlayer();

        if ( event.getClickedBlock() == null ) return;

        if ( !event.getAction().equals( Action.RIGHT_CLICK_BLOCK )
                && !event.getAction().equals( Action.LEFT_CLICK_BLOCK ) ) {
            return;
        }

        switch ( event.getClickedBlock().getType() ) {
            case CHEST: {
                if ( event.getAction().equals( Action.RIGHT_CLICK_BLOCK ) ) {
                    // Check if player in create cache
                    if ( !SignChangeListener.getShopCreateCache().containsKey( player ) ) {
                        // Get Shop bei Chest Location
                        Shop shop = Shop.getShopByChestLoc( event.getClickedBlock().getLocation() );

                        // Check if null
                        if ( shop == null ) {
                            return;
                        }

                        // Check if the Player is the Owner
                        if ( !shop.getOwner().equals( player.getUniqueId() ) && !player.hasPermission( "chestshop.view.other" ) ) {
                            // Send message and cancel event
                            player.sendMessage( this.chestShop.getLocaleConfig().getMessage( "message.chest.not-own-shop" ) );
                            event.setCancelled( true );
                        }
                        return;
                    }
                    // Get PreShop & Sign, Chest Locations
                    PreShop preShop = SignChangeListener.getShopCreateCache().get( player );
                    @SuppressWarnings("unused")
					Location chestLocation = event.getClickedBlock().getLocation();
                    Location signLocation = preShop.getSignLocation();

                    // Check if player has an Item in the Hand
                    if ( player.getItemInHand() == null || player.getItemInHand().getType().equals( Material.AIR ) ) {
                        player.sendMessage( this.chestShop.getLocaleConfig().getMessage( "message.sign.create.no-item" ) );
                        return;
                    }

                    // Check if Location is a Sign
                    if ( ( !( signLocation.getBlock().getState() instanceof Sign ) || !preShop.getSignLocation().equals( signLocation ) ) ) {
                        player.sendMessage( this.chestShop.getLocaleConfig().getMessage( "message.sign.create.sign-wrong" ) );
                        return;
                    }
                    // Cancel event
                    event.setCancelled( true );

                    // Remove from create cache
                    SignChangeListener.getShopCreateCache().remove( player );

                    // Create new Shop
                    Shop shop = preShop.toShop( new ItemBuilder( player.getItemInHand().clone() ).amount( preShop.getAmount() ).build() );
                    shop.spawnFloatingItem();
                    shop.updateSign();

                    // Cache & insert
                    Shop.getShops().add( shop );
                    this.chestShop.getShopRepository().insertShop( shop );

                    player.sendMessage( this.chestShop.getLocaleConfig().getMessage( "message.sign.create.success" ) );
                    return;
                }
                // Get Shop bei Chest Location
                Shop shop = Shop.getShopByChestLoc( event.getClickedBlock().getLocation() );

                // Check if null
                if ( shop == null ) {
                    return;
                }

                event.setCancelled( true );
                player.openInventory( shop.getPreviewInventory() );
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
                // Check if right clicking
                if ( event.getAction().equals( Action.LEFT_CLICK_BLOCK ) ) return;

                // Get Shop
                Shop shop = Shop.getShopBySignLoc( event.getClickedBlock().getLocation() );

                // Check if the Shop exists
                if ( shop == null ) {
                    return;
                }

                // Check if the owner click the Shop
                if ( shop.getOwner().equals( player.getUniqueId() ) ) {
                    player.openInventory( shop.getChestInventory() );
                    return;
                }

                Inventory shopInventory = shop.getChestInventory();

                // Check if Shop is a Buyable shop
                if ( shop.isBuyable() ) {
                    int amount = shop.getShopItemsSize( shopInventory );

                    // Check if amount lower than shop amount
                    if ( amount < shop.getAmount() ) {
                        player.sendMessage( this.chestShop.getLocaleConfig().getMessage( "message.sign.click.buy.no-items" ) );

                        // Get owner and check if exists
                        Player owner = Bukkit.getPlayer( shop.getOwner() );
                        if ( owner != null ) {
                            owner.sendMessage( this.chestShop.getLocaleConfig().getMessage( "message.sign.click.buy.no-items-shop-owner", shop.getItem().getType().name() ) );
                        }
                        return;
                    }

                    // Check if player has the Coins
                    this.chestShop.getPlayerCoinsRepository().hasCoins( player.getUniqueId(), shop.getPrice(), canBuy -> {
                        if ( !canBuy ) {
                            player.sendMessage( this.chestShop.getLocaleConfig().getMessage( "message.sign.click.buy.no-money" ) );
                            return;
                        }
                        // Remove & add Coins
                        this.chestShop.getPlayerCoinsRepository().removeCoins( player.getUniqueId(), shop.getPrice() );
                        this.chestShop.getPlayerCoinsRepository().addCoins( shop.getOwner(), shop.getPrice() );

                        // Remove items from shop and add it to the player
                        List<ItemStack> playerItems = new ArrayList<>();

                        int itemSize = shop.getAmount();
                        for ( ItemStack itemStack : shop.getShopItems( shopInventory ) ) {

                            int removed = itemSize - itemStack.getAmount();

                            if ( removed > 0 ) {
                                itemSize -= itemStack.getAmount();
                                playerItems.add( itemStack );
                                shopInventory.removeItem( itemStack );
                                continue;
                            }

                            if ( removed < 0 ) {
                                int newAmount = itemStack.getAmount() - itemSize;
                                playerItems.add( new ItemBuilder( itemStack.clone() ).amount( itemSize ).build() );
                                itemStack.setAmount( newAmount );
                                break;
                            }

                            playerItems.add( itemStack );
                            shopInventory.removeItem( itemStack );
                            break;
                        }

                        // Get name from uuid and send message
                        this.getName( shop.getOwner(), name -> {
                            player.sendMessage( this.chestShop.getLocaleConfig().getMessage( "message.sign.click.buy.success-player",
                                    name, shop.getAmount(), shop.getItem().getType().name(), shop.getPrice() ) );
                            player.sendMessage( this.chestShop.getLocaleConfig().getMessage( "message.sign.click.buy.success-player-coins",
                                    shop.getPrice() ) );
                        } );

                        // Give items to the player
                        this.giveItems( player, playerItems );

                        // Get Owner and check if online
                        Player owner = Bukkit.getPlayer( shop.getOwner() );
                        if ( owner != null ) {
                            // Send message
                            owner.sendMessage( this.chestShop.getLocaleConfig().getMessage( "message.sign.click.buy.success-owner",
                                    player.getName(), shop.getAmount(), shop.getItem().getType().name(), shop.getPrice() ) );
                            owner.sendMessage( this.chestShop.getLocaleConfig().getMessage( "message.sign.click.buy.success-owner-coins",
                                    shop.getPrice() ) );
                        } else {
                            // Add to offline earnings
                            this.chestShop.getPlayerRepository().addOfflineMoneyEarning( shop.getOwner(), shop.getPrice() );
                        }
                    } );
                    return;
                }

                // Get needed slots for the items
                int needed = shop.getAmount() / 64;
                if ( needed < 1 ) {
                    needed = 1;
                }

                int freeSpots = 0;
                for ( ItemStack itemStack : shopInventory.getContents() ) {
                    if ( itemStack != null ) continue;
                    freeSpots++;
                }

                // Check if the needed slots lower than the spots in the inventory
                if ( freeSpots < needed ) {
                    // Send message
                    player.sendMessage( this.chestShop.getLocaleConfig().getMessage( "message.sign.click.sell.no-space" ) );

                    // Get owner and send message when online
                    Player owner = Bukkit.getPlayer( shop.getOwner() );
                    if ( owner != null ) {
                        owner.sendMessage( this.chestShop.getLocaleConfig().getMessage( "message.sign.click.sell.no-space-shop-owner", shop.getItem().getType().name() ) );
                    }
                    return;
                }

                // Get the item amount size from the player inventory
                int amount = shop.getShopItemsSize( player.getInventory() );

                // Check if lower than the shop amount
                if ( amount < shop.getAmount() ) {
                    player.sendMessage( this.chestShop.getLocaleConfig().getMessage( "message.sign.click.sell.no-items" ) );
                    return;
                }

                // Check if the owner has the coins
                this.chestShop.getPlayerCoinsRepository().hasCoins( shop.getOwner(), shop.getPrice(), canSell -> {
                    if ( !canSell ) {
                        player.sendMessage( this.chestShop.getLocaleConfig().getMessage( "message.sign.click.sell.no-money" ) );
                        return;
                    }
                    // Remove & add coins
                    this.chestShop.getPlayerCoinsRepository().removeCoins( shop.getOwner(), shop.getPrice() );
                    this.chestShop.getPlayerCoinsRepository().addCoins( player.getUniqueId(), shop.getPrice() );

                    // Remove items from player inventory and add to shop
                    int itemSize = shop.getAmount();
                    for ( ItemStack itemStack : shop.getShopItems( player.getInventory() ) ) {

                        int removed = itemSize - itemStack.getAmount();

                        if ( removed > 0 ) {
                            itemSize -= itemStack.getAmount();
                            shopInventory.addItem( itemStack );
                            player.getInventory().removeItem( itemStack );
                            continue;
                        }

                        if ( removed < 0 ) {
                            int newAmount = itemStack.getAmount() - itemSize;
                            shopInventory.addItem( new ItemBuilder( itemStack.clone() ).amount( itemSize ).build() );
                            itemStack.setAmount( newAmount );
                            break;
                        }

                        shopInventory.addItem( itemStack );
                        player.getInventory().removeItem( itemStack );
                        break;
                    }

                    // Get name and send message
                    this.getName( shop.getOwner(), name -> {
                        player.sendMessage( this.chestShop.getLocaleConfig().getMessage( "message.sign.click.sell.success-player",
                                name, shop.getAmount(), shop.getItem().getType().name(), shop.getPrice() ) );
                        player.sendMessage( this.chestShop.getLocaleConfig().getMessage( "message.sign.click.sell.success-player-coins",
                                shop.getPrice() ) );
                    } );

                    // Get owner and check if online
                    Player owner = Bukkit.getPlayer( shop.getOwner() );
                    if ( owner != null ) {
                        // Send message
                        owner.sendMessage( this.chestShop.getLocaleConfig().getMessage( "message.sign.click.sell.success-owner",
                                player.getName(), shop.getAmount(), shop.getItem().getType().name(), shop.getPrice() ) );
                        owner.sendMessage( this.chestShop.getLocaleConfig().getMessage( "message.sign.click.sell.success-owner-coins",
                                shop.getPrice() ) );
                    } else {
                        // Add to offline earnings
                        this.chestShop.getPlayerRepository().addOfflineItemsEarning( shop.getOwner(),
                                shop.getItem().getType().name(), shop.getAmount() );
                    }
                } );
                return;
            }
            default:
                return;
        }
    }

    /**
     * Give items to the player and check if the inventory is not full
     *
     * @param player to give the items
     * @param items  to give
     */
    private void giveItems( Player player, List<ItemStack> items ) {
        for ( ItemStack itemStack : items ) {
            int freeSpots = 0;
            for ( ItemStack oldItem : player.getInventory().getContents() ) {
                if ( oldItem != null ) continue;
                freeSpots++;
            }

            if ( freeSpots > 0 ) {
                player.getInventory().addItem( itemStack );
                continue;
            }
            player.getWorld().dropItem( player.getLocation(), itemStack );
            player.sendMessage( this.chestShop.getLocaleConfig().getMessage( "message.sign.click.buy.item-dropped" ) );
        }

    }

    /**
     * Get the name from an uuid
     *
     * @param uuid     of the player
     * @param consumer with the name of the player
     */
    private void getName( UUID uuid, Consumer<String> consumer ) {
        Player player = Bukkit.getPlayer( uuid );
        if ( player != null ) {
            consumer.accept( player.getName() );
            return;
        }
        this.chestShop.getPlayerRepository().getName( uuid, name -> {
            if ( name == null ) {
                name = "Unbekannt";
            }
            consumer.accept( name );
        } );
    }
}
