package ch.luca.hydroslide.chestshop.utils.floatingitem;

import ch.luca.hydroslide.chestshop.shop.Shop;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

public class FloatingItemManager {

    @Getter
    private static Map<UUID, List<FloatingItem>> playerFloatingItems = new HashMap<>();

    private static Random random = new Random();

    /**
     * Add floating item to player
     *
     * @param player       to add
     * @param floatingItem to add
     */
    public static void addFloatingItemToPlayer( Player player, FloatingItem floatingItem ) {
        if ( !playerFloatingItems.containsKey( player.getUniqueId() ) ) {
            playerFloatingItems.put( player.getUniqueId(), new ArrayList<>() );
        }
        List<FloatingItem> floatingItems = playerFloatingItems.get( player.getUniqueId() );
        floatingItems.add( floatingItem );
        playerFloatingItems.put( player.getUniqueId(), floatingItems );
    }

    /**
     * Remove floating item to player
     *
     * @param player       to remove
     * @param floatingItem to remove
     */
    public static void removeFloatingItemFromPlayer( Player player, FloatingItem floatingItem ) {
        if ( !playerFloatingItems.containsKey( player.getUniqueId() ) ) return;
        List<FloatingItem> floatingItems = playerFloatingItems.get( player.getUniqueId() );
        floatingItems.remove( floatingItem );

        if ( floatingItems.size() == 0 ) {
            playerFloatingItems.remove( player.getUniqueId() );
        } else {
            playerFloatingItems.put( player.getUniqueId(), floatingItems );
        }
    }

    /**
     * Called when the player joins
     *
     * @param player to join
     */
    public static void handlePlayerJoin( Player player ) {
        /*for ( Shop shop : Shop.getShops() ) {
            if ( shop.getFloatingItem() == null ) continue;
            shop.getFloatingItem().createForPlayer( player );
        }*/
    }

    /**
     * Called when the player leaves
     *
     * @param player to leave
     */
    public static void handlePlayerQuit( Player player ) {
        /*if ( playerFloatingItems.containsKey( player.getUniqueId() ) ) {
            for ( FloatingItem floatingItem : playerFloatingItems.get( player.getUniqueId() ) ) {
                floatingItem.getLoadedPlayers().remove( player.getUniqueId() );
            }
            playerFloatingItems.remove( player.getUniqueId() );
        }*/
    }

    /**
     * Update view for a player
     *
     * @param player to update
     * @param to     location of the player
     */
    public static void updatePlayerView( Player player, Location to ) {
        /*if ( !playerFloatingItems.containsKey( player.getUniqueId() ) ) return;
        for ( FloatingItem floatingItem : playerFloatingItems.get( player.getUniqueId() ) ) {
            floatingItem.checkSending( player, to );
        }*/
    }

    /**
     * Get random entity id
     *
     * @return the random entity id
     */
    public static int getRandomEntityID() {
        return random.nextInt( 100000 );
    }
}
