package ch.luca.hydroslide.chestshop.listener;

import ch.luca.hydroslide.chestshop.ChestShop;
import ch.luca.hydroslide.chestshop.utils.floatingitem.FloatingItemManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Map;

public class PlayerJoinListener implements Listener {

    private ChestShop chestShop;

    public PlayerJoinListener( ChestShop chestShop ) {
        this.chestShop = chestShop;
    }

    @EventHandler
    public void onJoin( PlayerJoinEvent event ) {
        Player player = event.getPlayer();

        // Update name
        this.chestShop.getPlayerRepository().updateName( player.getUniqueId(), player.getName() );

        // Show floating items
        FloatingItemManager.handlePlayerJoin( player );

        // Get player offline earnings
        this.chestShop.getPlayerRepository().getOfflineEarnings( player.getUniqueId(), playerOfflineEarnings -> {
            if ( playerOfflineEarnings != null ) {
                // Check if player earned money
                if ( playerOfflineEarnings.getMoney() > 0 ) {
                    // Send info about the money
                    player.sendMessage( this.chestShop.getLocaleConfig().getMessage( "message.join.offline-money-earning",
                            playerOfflineEarnings.getMoney() ) );
                }

                // Check if player earned items
                if ( playerOfflineEarnings.getItems().size() > 0 ) {
                    // Send info abount items
                    player.sendMessage( this.chestShop.getLocaleConfig().getMessage( "message.join.offline-item-earning" ) );
                    for ( Map.Entry<String, Integer> entry : playerOfflineEarnings.getItems().entrySet() ) {
                        player.sendMessage( this.chestShop.getLocaleConfig().getMessage( "message.join.offline-item-earning-entry",
                                entry.getValue(), entry.getKey() ) );
                    }
                }
            }
        } );
    }
}
