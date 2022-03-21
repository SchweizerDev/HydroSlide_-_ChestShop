package ch.luca.hydroslide.chestshop.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class LocationSerializer {

    public static String serialize( Location input ) {
        return input.getWorld().getName() + ":" + input.getBlockX() + ":" + input.getBlockY() + ":" + input.getBlockZ();
    }

    public static Location deserialize( String input ) {
        String[] split = input.split( ":" );
        if ( split.length != 4 ) {
            return null;
        }

        World world = Bukkit.getWorld( split[0] );
        if ( world == null ) {
            return null;
        }

        double x = Double.parseDouble( split[1] );
        double y = Double.parseDouble( split[2] );
        double z = Double.parseDouble( split[3] );

        return new Location( world, x, y, z );
    }
}
