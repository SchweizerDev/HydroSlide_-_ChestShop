package ch.luca.hydroslide.chestshop.listener;

import ch.luca.hydroslide.chestshop.ChestShop;
import ch.luca.hydroslide.chestshop.utils.floatingitem.FloatingItemManager;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveListener implements Listener {

    @SuppressWarnings("unused")
	private ChestShop chestShop;

    public PlayerMoveListener( ChestShop chestShop ) {
        this.chestShop = chestShop;
    }

    @EventHandler
    public void onMove( PlayerMoveEvent event ) {
        if ( this.isSameLocation( event.getFrom(), event.getTo() ) ) return;
        Player player = event.getPlayer();

        if ( player.getHealth() <= 0.0D ) return;

        Chunk oldChunk = event.getFrom().getChunk();
        Chunk newChunk = event.getTo().getChunk();

        // Check movement for floating items
        if ( this.hasChunkChanged( oldChunk, newChunk ) ) {
            FloatingItemManager.updatePlayerView( player, player.getLocation() );
        }
    }

    private boolean isSameLocation( Location from, Location to ) {
        return from.getWorld().equals( to.getWorld() ) &&
                from.getBlockX() == to.getBlockX() &&
                from.getBlockY() == to.getBlockY() &&
                from.getBlockZ() == to.getBlockZ();
    }

    private boolean hasChunkChanged( Chunk oldChunk, Chunk newChunk ) {
        return !oldChunk.getWorld().equals( newChunk.getWorld() )
                || oldChunk.getX() != newChunk.getX()
                || oldChunk.getZ() != newChunk.getZ();
    }
}
