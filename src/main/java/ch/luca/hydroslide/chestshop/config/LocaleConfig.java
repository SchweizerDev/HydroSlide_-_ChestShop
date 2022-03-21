package ch.luca.hydroslide.chestshop.config;

import de.crafter75.configutil.Config;
import de.crafter75.configutil.Path;
import lombok.Getter;
import org.bukkit.ChatColor;

import java.io.File;
import java.lang.reflect.Field;

@Getter
public class LocaleConfig extends Config {

    @Path( "prefix" )
    private String prefix = "&6HydroGame &8»&7";

    @Path("no-perms")
    private String noPerms = "{prefix} &Für diesen Befehl fehlt dir die Berechtigung.";

    // Item needed Sign
    @Path( "sign.item-needed.line1" )
    private String signItemNeededLine1 = "&cRechtsklick";

    @Path( "sign.item-needed.line2" )
    private String signItemNeededLine2 = "&cmit einem";

    @Path( "sign.item-needed.line3" )
    private String signItemNeededLine3 = "&cItem auf";

    @Path( "sign.item-needed.line4" )
    private String signItemNeededLine4 = "&cdie Kiste!";

    // Sign create
    @Path( "sign.shop-created.line1" )
    private String signCreateLine1 = "&c{0}";

    @Path( "sign.shop-created.line2" )
    private String signCreateLine2 = "&b{0}";

    @Path( "sign.shop-created.line3" )
    private String signCreateLine3 = "&a{0}";

    @Path( "sign.shop-created.line4" )
    private String signCreateLine4 = "&8{0}";

    // Inventory
    @Path( "inventory.preview.title" )
    private String inventoryPreviewTitle = "&cItem Vorschau";

    @Path( "inventory.preview.lore-amount-more-64" )
    private String inventoryPreviewLoreAmountMore = "&7Anzahl: &e{0}";

    // Messages
    @Path( "message.sign.create.chest-not-found" )
    private String signCreateChestNotFound = "{prefix} &cEs wurde keine Kiste gefunden.";

    @Path("message.sign.create.already-shop")
    private String signCreateChestAlreadyShop = "{prefix} &cDiese Chest wird bereits von einem anderen Shop benutzt.";

    @Path( "message.sign.create.already-creating" )
    private String signCreateAlready = "{prefix} &7Du erstellst grade einen Shop. Benutze &e/abbrechen &7um den Vorgang abzubrechen.";

    @Path( "message.sign.create.item-needed-first" )
    private String signCreateItemNeededFirst = "{prefix} &7Klicke nun mit dem Item auf die Kiste. Klicke ";

    @Path( "message.sign.create.item-needed-click" )
    private String signCreateItemNeededClick = "&chier";

    @Path( "message.sign.create.item-needed-click-hover" )
    private String signCreateItemNeededClickHover = "&cKlicke hier, um den Vorgang abzubrechen";

    @Path( "message.sign.create.item-needed-last" )
    private String signCreateItemNeededLast = "&7, &7u&7m&7 d&7e&7n &7V&7o&7r&7g&7a&7n&7g&7 &7a&7b&7z&7u&7b&7r&7e&7c&7h&7e&7n&7.";

    @Path( "message.sign.create.no-item" )
    private String signCreateSignNoItem = "{prefix} &cDu kannst keine Luft anbieten.";

    @Path( "message.sign.create.sign-wrong" )
    private String signCreateSignWrong = "{prefix} &cDas Shop-Schild wurde nicht gefunden. Ist dies die richtige Kiste?";

    @Path("message.sign.create.success")
    private String signCreateSuccess = "{prefix} &aDein ChestShop wurde erfolgreich erstellt.";

    @Path("message.sign.create.cancel")
    private String signCreateCancel = "{prefix} &cShop-Erstellung abgebroche.";

    @Path("message.sign.destroy")
    private String signDestroy = "{prefix} &7Dein ChestShop wurde erfolgreich gelöscht.";

    @Path("message.sign.destroy-other")
    private String signDestroyOther = "{prefix} &7Der ChestShop von diesem Spieler wurde entfernt.";

    @Path( "message.chest.not-own-shop" )
    private String chestNotOwnShop = "{prefix} &cDieser Shop gehört nicht dir.";

    @Path( "message.chest.destroy" )
    private String chestDestroy = "{prefix} &7Wenn du den Shop entfernen willst, zerstöre das Schild!";

    @Path( "message.join.offline-money-earning" )
    private String offlineMoneyEarning = "{prefix} &7Du hast während du offline warst &6{0} Coins &7verdient.";

    @Path( "message.join.offline-item-earning" )
    private String offlineItemEarningMore = "{prefix} &7Während du offline warst, hast du folgende Items erhalten:";

    @Path( "message.join.offline-item-earning-entry" )
    private String offlineItemEarningMoreEntry = "{prefix} &8- &c&o{0}x {1}";

    @Path( "message.sign.click.buy.no-items" )
    private String signClickBuyNoItems = "{prefix} &7Der Shop hat nicht genügend Items.";

    @Path( "message.sign.click.buy.no-items-shop-owner" )
    private String signClickBuyNoItemsShopOwner = "{prefix} &cDein Shop &e{0} &7ist leer.";

    @Path( "message.sign.click.buy.no-money" )
    private String signClickBuyNoMoney = "{prefix} &cDu hast nicht genügend Coins.";

    @Path( "message.sign.click.buy.success-player" )
    private String signClickBuySuccessPlayer = "{prefix} &7Du hast von &e{0} &c&o{1}x {2} &7gekauft.";

    @Path( "message.sign.click.buy.success-player-coins" )
    private String signClickBuySuccessPlayerCoins = "{prefix} &c- &6{0} Coins";

    @Path( "message.sign.click.buy.success-owner" )
    private String signClickBuySuccessOwner = "{prefix} &e{0} &7hat dir &c&o{1}x {2} &7abgekauft.";

    @Path( "message.sign.click.buy.success-owner-coins" )
    private String signClickBuySuccessOwnerCoins = "{prefix} &a+ &6{0} Coins";

    @Path( "message.sign.click.buy.item-dropped" )
    private String signClickBuyItemDropped = "{prefix} &7Dein Inventar ist voll! Das Item &e{0} &7wurde auf den Boden gedroppt.";

    @Path("message.sign.click.sell.no-space")
    private String signClickSellNoSpace = "{prefix} &7Der Shop ist bereits voll.";

    @Path("message.sign.click.sell.no-space-shop-owner")
    private String signClickSellNoSpaceShopOwner = "{prefix} &7Dein Shop &e{0} &7ist voll.";

    @Path( "message.sign.click.sell.no-items" )
    private String signClickSellNoItems = "{prefix} &7Du hast nicht genügend Items.";

    @Path( "message.sign.click.sell.no-money" )
    private String signClickSellNoMoney = "{prefix} &7Der Shop Owner hat nicht genügend Coins.";

    @Path( "message.sign.click.sell.success-player" )
    private String signClickSellSuccessPlayer = "{prefix} &7Du hast an &e{0} &c&o{1}x {2} &7verkauft.";

    @Path( "message.sign.click.sell.success-player-coins" )
    private String signClickSellSuccessPlayerCoins = "{prefix} &a+ &6{0} Coins";

    @Path( "message.sign.click.sell.success-owner" )
    private String signClickSellSuccessOwner = "{prefix} &e{0} &7hat dir &c&o{1}x {2} &7verkauft.";

    @Path( "message.sign.click.sell.success-owner-coins" )
    private String signClickSellSuccessOwnerCoins = "{prefix} &c- &6{0} Coins";

    public LocaleConfig( File file ) {
        this.CONFIG_FILE = file;
    }

    public String getMessage( String key, Object... args ) {
        for ( Field field : this.getClass().getDeclaredFields() ) {
            if ( !field.isAnnotationPresent( Path.class ) ) {
                continue;
            }

            if ( field.getAnnotation( Path.class ).value().equalsIgnoreCase( key ) ) {
                try {
                    field.setAccessible( true );
                    String message = String.valueOf( field.get( this ) );

                    if ( message.contains( "{prefix}" ) ) {
                        message = message.replaceAll( "\\{prefix}", ChatColor.translateAlternateColorCodes( '&', this.prefix ) );
                    }

                    for ( int i = 0; i < args.length; i++ ) {
                        message = message.replaceAll( "\\{" + i + "}", "" + args[i] );
                    }

                    return ChatColor.translateAlternateColorCodes( '&', message );
                } catch ( IllegalAccessException e ) {
                    e.printStackTrace();
                }
            }
        }
        return "Error while translating '" + key + "'";
    }
}
