package ch.luca.hydroslide.chestshop.shop;

import ch.luca.hydroslide.chestshop.ChestShop;
import ch.luca.hydroslide.chestshop.config.LocaleConfig;
import ch.luca.hydroslide.chestshop.utils.ItemBuilder;
import ch.luca.hydroslide.chestshop.utils.floatingitem.FloatingItem;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Shop extends PreShop {

    @Getter
    @Setter
    private static List<Shop> shops = new ArrayList<>();

    @Getter
    private ItemStack item;

    @Getter
    private FloatingItem floatingItem;

    @Getter
    private Inventory previewInventory;

    public Shop( Location signLocation, Location chestLocation, UUID owner, boolean buyable, int price, int amount, ItemStack item ) {
        super( signLocation, chestLocation, owner, buyable, price, amount );
        this.item = item;

        // Prepare preview inventory
        this.preparePreviewInventory();
    }

    /**
     * Check if the sign & chest exists of the shop
     *
     * @return if the sign & chest exists
     */
    public boolean existsSignAndChest() {
        if ( !( this.getSignLocation().getBlock().getState() instanceof Sign ) ) {
            System.out.println( "[ChestShop] Location is not a Sign! (" + this.getSignLocation().toString() + " | Owner: " + this.getOwner() + ")" );
            this.remove();
            return false;
        }
        if ( !( this.getChestLocation().getBlock().getState() instanceof Chest ) ) {
            System.out.println( "[ChestShop] Location is not a Chest! (" + this.getChestLocation().toString() + " | Owner: " + this.getOwner() + ")" );
            this.remove();
            return false;
        }
        return true;
    }

    /**
     * Spawn the floating item
     */
    public void spawnFloatingItem() {
        this.floatingItem = new FloatingItem( new ItemBuilder( this.item.clone() ).amount( 1 ).build(), this.getChestLocation().clone().subtract( 0.0D, 0.7D, 0.0D ).add( 0.5D, 0.0D, 0.5D ) ); //.subtract( 0.5D, 1.7D, 0.5D ).add( 1.0D, 0.0D, 1.0D )
    }

    /**
     * Update the sign with the name, type, price & amount of the shop
     */
    public void updateSign() {
        LocaleConfig localeConfig = ChestShop.getInstance().getLocaleConfig();
        Sign sign = (Sign) this.getSignLocation().getBlock().getState();

        ChestShop.getInstance().getPlayerRepository().getName( this.getOwner(), name -> Bukkit.getScheduler().runTask( ChestShop.getInstance(), () -> {
            if ( name != null ) {
                sign.setLine( 0, localeConfig.getMessage( "sign.shop-created.line1", name ) );
                sign.setLine( 1, localeConfig.getMessage( "sign.shop-created.line2", ( this.isBuyable() ? "Kaufen" : "Verkaufen" ) ) );
                sign.setLine( 2, localeConfig.getMessage( "sign.shop-created.line3", this.getPrice() ) );
                sign.setLine( 3, localeConfig.getMessage( "sign.shop-created.line4", this.getAmount() ) );
                sign.update( true );
            }
        } ) );
    }

    /**
     * Get the chest inventory of the shop
     *
     * @return the chest inventory
     */
    public Inventory getChestInventory() {
        return ( (Chest) this.getChestLocation().getBlock().getState() ).getInventory();
    }

    /**
     * Get the shop items from an inventory
     *
     * @param inventory of the shop items
     * @return the shop items as list
     */
    public List<ItemStack> getShopItems( Inventory inventory ) {
        List<ItemStack> items = new ArrayList<>();
        for ( ItemStack itemStack : inventory.getContents() ) {
            if ( itemStack == null || itemStack.getType().equals( Material.AIR ) ) continue;
            if ( !this.isShopItem( itemStack ) ) continue;
            items.add( itemStack );
        }
        return items;
    }

    /**
     * Get the shop items as amount
     *
     * @param inventory of the shop items
     * @return the amount of the shop items
     */
    public int getShopItemsSize( Inventory inventory ) {
        int amount = 0;
        for ( ItemStack itemStack : inventory.getContents() ) {
            if ( itemStack == null || itemStack.getType().equals( Material.AIR ) ) continue;
            if ( !this.isShopItem( itemStack ) ) continue;
            amount += itemStack.getAmount();
        }
        return amount;
    }

    /**
     * Check if the item is the original with same item meta
     *
     * @param itemStack to check
     * @return if the item is the same
     */
    private boolean isShopItem( ItemStack itemStack ) {
        return new ItemBuilder( this.item.clone() ).amount( itemStack.getAmount() ).build().equals( itemStack );
    }

    /**
     * Remove the shop from sql & cache
     */
    public void remove() {
        ChestShop.getInstance().getShopRepository().removeShop( this.getSignLocation(), this.getOwner() );
        if ( this.floatingItem != null ) {
            this.floatingItem.remove();
        }
        shops.remove( this );
    }

    /**
     * Prepare the preview inventory
     */
    private void preparePreviewInventory() {
        ItemStack previewItem;
        if ( this.getAmount() > 64 ) {
            List<String> lore = this.item.getItemMeta().getLore();
            List<String> newLore = new ArrayList<>();

            newLore.add( ChestShop.getInstance().getLocaleConfig().getMessage( "inventory.preview.lore-amount-more-64", this.getAmount() ) );

            if ( lore != null && !lore.isEmpty() ) {
                newLore.addAll( lore );
            }
            previewItem = new ItemBuilder( this.item.clone() ).amount( this.getAmount() ).lore( newLore ).build();
        } else {
            previewItem = new ItemBuilder( this.item.clone() ).amount( this.getAmount() ).build();
        }

        ItemStack glass = new ItemBuilder( Material.BLACK_STAINED_GLASS_PANE, 1, 0 ).name( "§e" ).build();

        this.previewInventory = Bukkit.createInventory( null, InventoryType.HOPPER, ChestShop.getInstance().getLocaleConfig().getMessage( "inventory.preview.title" ) );
        this.previewInventory.setItem( 0, glass );
        this.previewInventory.setItem( 1, glass );
        this.previewInventory.setItem( 2, previewItem );
        this.previewInventory.setItem( 3, glass );
        this.previewInventory.setItem( 4, glass );
    }

    /**
     * Get a shop by the sign location
     *
     * @param signLocation of the shop
     * @return the shop. the result can be null if the shop not exists
     */
    public static Shop getShopBySignLoc( Location signLocation ) {
        for ( Shop shop : shops ) {
            if ( shop.getSignLocation().equals( signLocation ) ) {
                return shop;
            }
        }
        return null;
    }

    /**
     * Get a shop by the chest location
     *
     * @param chestLocation of the shop
     * @return the shop. the result can be null if the shop not exists
     */
    public static Shop getShopByChestLoc( Location chestLocation ) {
        for ( Shop shop : shops ) {
            if ( shop.getChestLocation().equals( chestLocation ) ) {
                return shop;
            }
        }
        return null;
    }
}
