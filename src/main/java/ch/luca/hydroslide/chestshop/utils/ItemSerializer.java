package ch.luca.hydroslide.chestshop.utils;

import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class ItemSerializer {

    public static String serialize( ItemStack input ) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            BukkitObjectOutputStream bukkitOut = new BukkitObjectOutputStream( out );
            bukkitOut.writeObject( input );
            bukkitOut.close();
            return Base64Coder.encodeLines( out.toByteArray() );
        } catch ( Exception ignored ) {
        }
        return null;
    }

    public static ItemStack deserialize( String input ) {
        ByteArrayInputStream in = new ByteArrayInputStream( Base64Coder.decodeLines( input ) );
        try {
            BukkitObjectInputStream bukkitIn = new BukkitObjectInputStream( in );
            ItemStack result = (ItemStack) bukkitIn.readObject();
            bukkitIn.close();
            return result;
        } catch ( Exception ignored ) {
        }
        return null;
    }
}
