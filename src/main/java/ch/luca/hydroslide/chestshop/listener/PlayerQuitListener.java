package ch.luca.hydroslide.chestshop.listener;

import ch.luca.hydroslide.chestshop.ChestShop;
import ch.luca.hydroslide.chestshop.utils.floatingitem.FloatingItemManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    @SuppressWarnings("unused")
	private ChestShop chestShop;

    public PlayerQuitListener( ChestShop chestShop ) {
        this.chestShop = chestShop;
    }

    @EventHandler
    public void onQuit( PlayerQuitEvent event ) {
        Player player = event.getPlayer();

        // Remove floating items for player
        FloatingItemManager.handlePlayerQuit( player );

        // Remove from shop create cache
        SignChangeListener.getShopCreateCache().remove( player );
    }
}
