package ch.luca.hydroslide.chestshop.mysql.repository;

import ch.luca.hydroslide.chestshop.mysql.MySQL;
import ch.luca.hydroslide.chestshop.shop.Shop;
import ch.luca.hydroslide.chestshop.utils.ItemSerializer;
import ch.luca.hydroslide.chestshop.utils.LocationSerializer;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class ShopRepository {

    private MySQL mySQL;

    public ShopRepository( MySQL mySQL ) {
        this.mySQL = mySQL;

        // Init tables
        this.mySQL.updateAsync( "CREATE TABLE IF NOT EXISTS shops (sign_loc VARCHAR(100), chest_loc VARCHAR(100), owner VARCHAR(36), buy BOOLEAN, price INT(100), amount INT(100), item LONGTEXT, PRIMARY KEY(sign_loc, owner))" );
    }

    /**
     * Insert a shop
     *
     * @param shop to insert
     */
    public void insertShop( Shop shop ) {
        this.mySQL.updateAsync( "INSERT INTO shops (sign_loc, chest_loc, owner, buy, price, amount, item) VALUES (?,?,?,?,?,?,?)",
                LocationSerializer.serialize( shop.getSignLocation() ),
                LocationSerializer.serialize( shop.getChestLocation() ),
                shop.getOwner().toString(),
                shop.isBuyable(),
                shop.getPrice(),
                shop.getAmount(),
                ItemSerializer.serialize( shop.getItem() ) );
    }

    /**
     * Remove a shop
     *
     * @param location of the shop
     * @param owner    of the shop
     */
    public void removeShop( Location location, UUID owner ) {
        this.mySQL.updateAsync( "DELETE FROM shops WHERE sign_loc = ? AND owner = ?",
                LocationSerializer.serialize( location ),
                owner.toString() );
    }

    /**
     * Load all shops
     *
     * @param consumer with the list of shops
     */
    public void loadShops( Consumer<List<Shop>> consumer ) {
        this.mySQL.queryAsync( "SELECT * FROM shops", resultSet -> {
            List<Shop> shops = new ArrayList<>();
            try {
                if ( resultSet != null ) {
                    while ( resultSet.next() ) {
                        Location signLocation = LocationSerializer.deserialize( resultSet.getString( "sign_loc" ) );
                        UUID owner = UUID.fromString( resultSet.getString( "owner" ) );

                        if ( signLocation == null ) {
                            System.out.println( "[ChestShop] Cannot find location! (" + resultSet.getString( "sign_loc" ) + " | Owner: " + owner + ")" );
                            continue;
                        }

                        boolean buyable = resultSet.getBoolean( "buy" );
                        int price = resultSet.getInt( "price" );
                        int amount = resultSet.getInt( "amount" );
                        ItemStack item = ItemSerializer.deserialize( resultSet.getString( "item" ) );

                        if ( item == null ) {
                            System.out.println( "[ChestShop] Cannot find item! (Loc: " + resultSet.getString( "sign_loc" ) + " | Owner: " + owner + ")" );
                            continue;
                        }
                        Location chestLocation = LocationSerializer.deserialize( resultSet.getString( "chest_loc" ) );

                        Shop shop = new Shop( signLocation, chestLocation, owner, buyable, price, amount, item );
                        shops.add( shop );
                    }
                }
            } catch ( SQLException e ) {
                e.printStackTrace();
            }
            consumer.accept( shops );
        } );
    }
}
