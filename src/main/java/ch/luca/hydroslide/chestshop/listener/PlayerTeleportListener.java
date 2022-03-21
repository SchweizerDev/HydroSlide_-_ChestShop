package ch.luca.hydroslide.chestshop.listener;

import ch.luca.hydroslide.chestshop.ChestShop;
import ch.luca.hydroslide.chestshop.utils.floatingitem.FloatingItemManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class PlayerTeleportListener implements Listener {

    @SuppressWarnings("unused")
	private ChestShop chestShop;

    public PlayerTeleportListener( ChestShop chestShop ) {
        this.chestShop = chestShop;
    }

    @EventHandler
    public void onTeleport( PlayerTeleportEvent event ) {
        FloatingItemManager.updatePlayerView( event.getPlayer(), event.getTo() );
    }
}
