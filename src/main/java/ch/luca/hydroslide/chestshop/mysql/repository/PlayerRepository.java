package ch.luca.hydroslide.chestshop.mysql.repository;

import ch.luca.hydroslide.chestshop.mysql.MySQL;
import lombok.Getter;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class PlayerRepository {

    private MySQL mySQL;

    public PlayerRepository( MySQL mySQL ) {
        this.mySQL = mySQL;

        // Init tables
        this.mySQL.updateAsync( "CREATE TABLE IF NOT EXISTS shops_uuidname_cache (uuid VARCHAR(36), name VARCHAR(36), PRIMARY KEY(uuid))" );

        this.mySQL.updateAsync( "CREATE TABLE IF NOT EXISTS shops_player_earnings (uuid VARCHAR(36), money INT(100), items LONGTEXT, PRIMARY KEY(uuid))" );
    }

    /**
     * Get the name from a uuid
     *
     * @param uniqueId of the player
     * @param consumer with the name
     */
    public void getName( UUID uniqueId, Consumer<String> consumer ) {
        this.mySQL.queryAsync( "SELECT name FROM shops_uuidname_cache WHERE uuid = ?", resultSet -> {
            try {
                if ( resultSet != null && resultSet.next() ) {
                    consumer.accept( resultSet.getString( "name" ) );
                    return;
                }
            } catch ( SQLException e ) {
                e.printStackTrace();
            }
            consumer.accept( null );
        }, uniqueId.toString() );
    }

    /**
     * Update the name of a uuid
     *
     * @param uniqueId of the player
     * @param name     of the player
     */
    public void updateName( UUID uniqueId, String name ) {
        this.mySQL.updateAsync( "REPLACE INTO shops_uuidname_cache (uuid, name) VALUES (?,?)",
                uniqueId.toString(), name );
    }

    /**
     * Add money earning to a player
     *
     * @param uniqueId of the player
     * @param money    to add
     */
    public void addOfflineMoneyEarning( UUID uniqueId, int money ) {
        this.mySQL.queryAsync( "SELECT * FROM shops_player_earnings WHERE uuid = ?", resultSet -> {
            try {
                if ( resultSet.next() ) {
                    PlayerRepository.this.mySQL.updateAsync( "UPDATE shops_player_earnings SET money = money + ? WHERE uuid = ?",
                            money, uniqueId.toString() );
                    return;
                }
                PlayerRepository.this.mySQL.updateAsync( "INSERT INTO shops_player_earnings (uuid, money, items) VALUES (?,?,?)",
                        uniqueId.toString(), money, null );
            } catch ( SQLException e ) {
                e.printStackTrace();
            }
        }, uniqueId.toString() );
    }

    /**
     * Add item earning to a player
     *
     * @param uniqueId     of the player
     * @param materialName to add
     * @param amount       to add
     */
    public void addOfflineItemsEarning( UUID uniqueId, String materialName, int amount ) {
        this.mySQL.queryAsync( "SELECT * FROM shops_player_earnings WHERE uuid = ?", resultSet -> {
            try {
                if ( resultSet.next() ) {
                    String items = resultSet.getString( "items" );
                    if ( items == null ) {
                        items = materialName + "," + amount;
                    } else {
                        items += ";" + materialName + "," + amount;
                    }

                    PlayerRepository.this.mySQL.updateAsync( "UPDATE shops_player_earnings SET items = ? WHERE uuid = ?",
                            items, uniqueId.toString() );
                    return;
                }
                PlayerRepository.this.mySQL.updateAsync( "INSERT INTO shops_player_earnings (uuid, money, items) VALUES (?,?,?)",
                        uniqueId.toString(), 0, materialName + "," + amount );
            } catch ( SQLException e ) {
                e.printStackTrace();
            }
        }, uniqueId.toString() );
    }

    /**
     * Get the earnings from a player
     *
     * @param uniqueId of the player
     * @param consumer with the result
     */
    public void getOfflineEarnings( UUID uniqueId, Consumer<PlayerOfflineEarnings> consumer ) {
        this.mySQL.queryAsync( "SELECT * FROM shops_player_earnings WHERE uuid = ?", resultSet -> {
            try {
                if ( resultSet != null && resultSet.next() ) {
                    int money = resultSet.getInt( "money" );
                    String items = resultSet.getString( "items" );

                    Map<String, Integer> itemMap = new HashMap<>();

                    if ( items != null ) {
                        if ( items.contains( ";" ) ) {
                            for ( String split : items.split( ";" ) ) {
                                String[] entry = split.split( "," );

                                String materialName = entry[0];
                                int amountToAdd = Integer.parseInt( entry[1] );

                                if ( !itemMap.containsKey( materialName ) ) {
                                    itemMap.put( materialName, amountToAdd );
                                    continue;
                                }
                                int amount = itemMap.get( materialName );
                                amount += amountToAdd;
                                itemMap.put( materialName, amount );
                            }
                        } else {
                            String[] entry = items.split( "," );

                            String materialName = entry[0];
                            int amount = Integer.parseInt( entry[1] );

                            itemMap.put( materialName, amount );
                        }
                    }

                    // Remove from mysql
                    this.mySQL.updateAsync( "DELETE FROM shops_player_earnings WHERE uuid = ?", uniqueId.toString() );

                    consumer.accept( new PlayerOfflineEarnings( money, itemMap ) );
                    return;
                }
            } catch ( SQLException e ) {
                e.printStackTrace();
            }
            consumer.accept( null );
        }, uniqueId.toString() );
    }

    public class PlayerOfflineEarnings {

        @Getter
        private int money;

        @Getter
        private Map<String, Integer> items;

        public PlayerOfflineEarnings( int money, Map<String, Integer> items ) {
            this.money = money;
            this.items = items;
        }
    }
}
