package ch.luca.hydroslide.chestshop.shop;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class PreShop {

    @Getter
    private Location signLocation, chestLocation;

    @Getter
    private UUID owner;

    @Getter
    private boolean buyable;

    @Getter
    private int price;

    @Getter
    private int amount;

    public PreShop( Location location, Location chestLocation, UUID owner, boolean buyable, int price, int amount ) {
        this.signLocation = location;
        this.chestLocation = chestLocation;
        this.owner = owner;
        this.buyable = buyable;
        this.price = price;
        this.amount = amount;
    }

    public Shop toShop( ItemStack itemStack ) {
        return new Shop( this.signLocation, this.chestLocation, this.owner, this.buyable, this.price, this.amount, itemStack );
    }
}
