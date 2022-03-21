package ch.luca.hydroslide.chestshop.config;

import de.crafter75.configutil.Config;
import de.crafter75.configutil.Path;
import lombok.Getter;

import java.io.File;

@Getter
public class MainConfig extends Config {

    // MySQL for Shop
    @Path("mysql.shop.host")
    private String mySQLShopHost = "localhost";

    @Path("mysql.shop.database")
    private String mySQLShopDatabase = "hydrogame";

    @Path("mysql.shop.user")
    private String mySQLShopUser = "root";

    @Path("mysql.shop.password")
    private String mySQLShopPassword = "wqfj9X3v9cPMUrfS";

    @Path("mysql.shop.port")
    private int mySQLShopPort = 3306;

    // MySQL for Coins
    @Path("mysql.coins.host")
    private String mySQLCoinsHost = "localhost";

    @Path("mysql.coins.database")
    private String mySQLSCoinsDatabase = "bungeecord";

    @Path("mysql.coins.user")
    private String mySQLCoinsUser = "root";

    @Path("mysql.coins.password")
    private String mySQLCoinsPassword = "wqfj9X3v9cPMUrfS";

    @Path("mysql.coins.port")
    private int mySQLCoinsPort = 3306;

    public MainConfig( File file ) {
        this.CONFIG_FILE = file;
    }
}
