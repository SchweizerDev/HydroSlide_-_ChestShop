package ch.luca.hydroslide.chestshop.mysql.repository;

import ch.luca.hydroslide.chestshop.mysql.MySQL;

import java.sql.SQLException;
import java.util.UUID;
import java.util.function.Consumer;

public class PlayerCoinsRepository {

    private MySQL mySQL;

    public PlayerCoinsRepository( MySQL mySQL ) {
        this.mySQL = mySQL;
    }

    /**
     * Get the Coins of a player
     *
     * @param uniqueId of the player
     * @param consumer with the coins
     */
    public void getCoins( UUID uniqueId, Consumer<Integer> consumer ) {
        this.mySQL.queryAsync( "SELECT amount FROM Coins WHERE uuid = ?", resultSet -> {
            try {
                if ( resultSet != null && resultSet.next() ) {
                    consumer.accept( resultSet.getInt( "amount" ) );
                    return;
                }
            } catch ( SQLException e ) {
                e.printStackTrace();
            }
            consumer.accept( -1 );
        }, uniqueId.toString() );
    }

    /**
     * Check if a player has the Coins
     *
     * @param uniqueId of the player
     * @param coins    to check
     * @param consumer with the result
     */
    public void hasCoins( UUID uniqueId, int coins, Consumer<Boolean> consumer ) {
        this.getCoins( uniqueId, playerCoins -> {
            if ( playerCoins == -1 ) {
                consumer.accept( false );
                return;
            }
            consumer.accept( playerCoins >= coins );
        } );
    }

    /**
     * Add Coins to a player
     *
     * @param uniqueId of the player
     * @param coins    to add
     */
    public void addCoins( UUID uniqueId, int coins ) {
        this.mySQL.updateAsync( "UPDATE Coins SET amount = amount + ? WHERE uuid = ?", coins, uniqueId.toString() );
    }

    /**
     * Remove Coins from a player
     *
     * @param uniqueId of the player
     * @param coins    to remove
     */
    public void removeCoins( UUID uniqueId, int coins ) {
        this.mySQL.updateAsync( "UPDATE Coins SET amount = amount - ? WHERE uuid = ?", coins, uniqueId.toString() );
    }
}
