package ch.luca.hydroslide.chestshop.listener;

import ch.luca.hydroslide.chestshop.ChestShop;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClickListener implements Listener {

    private String previewTitle;

    private ChestShop chestShop;

    public InventoryClickListener( ChestShop chestShop ) {
        this.chestShop = chestShop;

        previewTitle = this.chestShop.getLocaleConfig().getMessage( "inventory.preview.title" );
    }

    @EventHandler
    public void onInventoryClick( InventoryClickEvent event ) {
        @SuppressWarnings("unused")
		Player player = (Player) event.getWhoClicked();

        if ( event.getCurrentItem() == null ) return;
        if ( event.getInventory() == null ) return;

        // Check if preview inventory and cancel
        if ( event.getView().getTitle().equals( this.previewTitle ) ) {
            event.setCancelled( true );
        }
    }
}
