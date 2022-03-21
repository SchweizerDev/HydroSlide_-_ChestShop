package ch.luca.hydroslide.chestshop.utils.floatingitem;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.*;
import net.minecraft.network.syncher.DataWatcher;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.EntityArmorStand;
import net.minecraft.world.entity.item.EntityItem;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_18_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_18_R1.inventory.CraftItemStack;
import org.bukkit.entity.ArmorStand;

public class FloatingPacket {

    /**
     * Create ArmorStand
     *
     * @param location of the ArmorStand
     * @param entityID of the ArmorStand
     * @return the packets
     */
    public static EntityArmorStand createArmorStand(Location location ) {
        EntityArmorStand entityArmorStand = new EntityArmorStand( ( (CraftWorld) location.getWorld() ).getHandle(), location.getX(), location.getY(), location.getZ());
        ArmorStand armorStand = ((ArmorStand)entityArmorStand.getBukkitEntity());
        armorStand.setCustomNameVisible(false);
        armorStand.setInvisible( true );
        armorStand.setGravity(true);
        return entityArmorStand;
    }

    /**
     * Create item
     *
     * @param location of the item
     * @param itemStack of the item
     * @return the item
     */
    public static EntityItem createItem(Location location, org.bukkit.inventory.ItemStack itemStack ) {
        return new EntityItem( ( (CraftWorld) location.getWorld() ).getHandle(), location.getX(), location.getY(), location.getZ(), CraftItemStack.asNMSCopy( itemStack ) );
    }

    /**
     * Spawn item
     *
     * @param entityItem to spawn
     * @return the packets
     */
    public static Packet itemSpawn( EntityItem entityItem ) {
        return new PacketPlayOutSpawnEntity( entityItem, 2 );
    }

    /**
     * Get item meta
     *
     * @param itemID of the meta
     * @param dataWatcher of the meta
     * @return the packets
     */
    public static Packet itemMeta( int itemID, DataWatcher dataWatcher ) {
        return new PacketPlayOutEntityMetadata( itemID, dataWatcher, true );
    }

    /**
     * Set item to ArmorStand
     *
     * @param itemEntity to set
     * @param armorStandEntity to set
     * @return the packets
     */
    public static Packet attachItemToArmorStand(Entity itemEntity, Entity armorStandEntity ) {
        PacketPlayOutAttachEntity packetPlayOutAttachEntity = new PacketPlayOutAttachEntity(itemEntity, armorStandEntity);

        return packetPlayOutAttachEntity;

    }

    /**
     * Destroy packets
     *
     * @param entityID to destroy
     * @return the packets
     */
    public static Packet destroyEntity( int entityID ) {
        return new PacketPlayOutEntityDestroy( entityID );
    }
}
