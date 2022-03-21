package ch.luca.hydroslide.chestshop;

import ch.luca.hydroslide.chestshop.command.AdminShopCommand;
import ch.luca.hydroslide.chestshop.command.ChestShopCommand;
import ch.luca.hydroslide.chestshop.config.LocaleConfig;
import ch.luca.hydroslide.chestshop.config.MainConfig;
import ch.luca.hydroslide.chestshop.listener.*;
import ch.luca.hydroslide.chestshop.mysql.MySQL;
import ch.luca.hydroslide.chestshop.mysql.repository.PlayerCoinsRepository;
import ch.luca.hydroslide.chestshop.mysql.repository.PlayerRepository;
import ch.luca.hydroslide.chestshop.mysql.repository.ShopRepository;
import ch.luca.hydroslide.chestshop.shop.Shop;
import com.github.unldenis.hologram.HologramPool;
import de.crafter75.configutil.InvalidConfigurationException;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Iterator;

public class ChestShop extends JavaPlugin {

    @Getter
    private static ChestShop instance;

    @Getter
    private MainConfig mainConfig;

    @Getter
    private LocaleConfig localeConfig;

    @Getter
    private MySQL shopSql, coinsSql;

    @Getter
    private ShopRepository shopRepository;

    @Getter
    private PlayerRepository playerRepository;

    @Getter
    private PlayerCoinsRepository playerCoinsRepository;

    @Getter
    private HologramPool hologramPool;

    @Override
    public void onEnable() {
        instance = this;

        // Init configs
        this.loadConfigs();

        // Init mysql connections
        this.shopSql = new MySQL( this.mainConfig.getMySQLShopHost(), this.mainConfig.getMySQLShopDatabase(),
                this.mainConfig.getMySQLShopUser(), this.mainConfig.getMySQLShopPassword(),
                this.mainConfig.getMySQLShopPort() );

        this.coinsSql = new MySQL( this.mainConfig.getMySQLCoinsHost(), this.mainConfig.getMySQLSCoinsDatabase(),
                this.mainConfig.getMySQLCoinsUser(), this.mainConfig.getMySQLCoinsPassword(),
                this.mainConfig.getMySQLCoinsPort() );

        // Init repositories
        this.shopRepository = new ShopRepository( this.shopSql );
        this.playerRepository = new PlayerRepository( this.shopSql );
        this.playerCoinsRepository = new PlayerCoinsRepository( this.coinsSql );

        // Register commands
        this.registerCommands();

        // Register listener
        this.registerListener();
        
        this.hologramPool = new HologramPool(this, 70);

        Bukkit.getScheduler().runTaskLater( this, () -> {
            System.out.println( "[ChestShop] Load shops..." );

            // Load shops
            this.shopRepository.loadShops( shops -> Bukkit.getScheduler().runTask( this, () -> {
                Shop.setShops( shops );
                System.out.println( "[ChestShop] Loaded " + shops.size() + " shops!" );

                Iterator<Shop> shopIterator = Shop.getShops().iterator();
                while ( shopIterator.hasNext() ) {
                    Shop shop = shopIterator.next();

                    if ( !shop.existsSignAndChest() ) {
                        continue;
                    }
                    shop.updateSign();
                    shop.spawnFloatingItem();
                }
            } ) );
        }, 20L );

        Bukkit.getConsoleSender().sendMessage("§6HydroGame §8» §6ChestShop §7wurde §aerfolgreich §7gestartet.");
        Bukkit.getConsoleSender().sendMessage("§6HydroGame §8» §7Datenbanken, Locations und Config wurden geladen.");
        Bukkit.getConsoleSender().sendMessage("§6HydroGame §8» §7Minecraft-Version: §c1.18.1");
        Bukkit.getConsoleSender().sendMessage("§6HydroGame §8» §7Plugin-Version: §c1.0.0");
        Bukkit.getConsoleSender().sendMessage("§6HydroGame §8» §7Autoren: §eThorsten & Luca");
        Bukkit.getConsoleSender().sendMessage("§6HydroGame §8» §4§lDeveloped for HydroSlide.eu");
        Bukkit.getConsoleSender().sendMessage("§6HydroGame §8» §eSourcecode is copyright protected!");
    }

    @Override
    public void onDisable() {
    }

    /**
     * Register listener
     */
    private void registerListener() {
        PluginManager pluginManager = this.getServer().getPluginManager();

        pluginManager.registerEvents( new BlockBreakListener( this ), this );
        pluginManager.registerEvents( new InventoryClickListener( this ), this );
        pluginManager.registerEvents( new PlayerInteractListener( this ), this );
        pluginManager.registerEvents( new PlayerJoinListener( this ), this );
        pluginManager.registerEvents( new PlayerMoveListener( this ), this );
        pluginManager.registerEvents( new PlayerQuitListener( this ), this );
        pluginManager.registerEvents( new PlayerTeleportListener( this ), this );
        pluginManager.registerEvents( new SignChangeListener( this ), this );
    }

    /**
     * Register commands
     */
    private void registerCommands() {
        this.getCommand( "chestshop" ).setExecutor( new ChestShopCommand( this ) );
        this.getCommand( "adminshop" ).setExecutor( new AdminShopCommand( this ) );
    }

    /**
     * Load configs
     */
    private void loadConfigs() {
        if ( !this.getDataFolder().exists() ) {
            this.getDataFolder().mkdir();
        }
        try {
            // Init main config
            this.mainConfig = new MainConfig( new File( this.getDataFolder(), "config.yml" ) );
            this.mainConfig.init();

            // Init locale config
            this.localeConfig = new LocaleConfig( new File( this.getDataFolder(), "messages.yml" ) );
            this.localeConfig.init();
        } catch ( InvalidConfigurationException e ) {
            e.printStackTrace();
        }
    }
}