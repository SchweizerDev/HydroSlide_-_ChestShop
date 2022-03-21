package ch.luca.hydroslide.chestshop.utils.floatingitem;

import ch.luca.hydroslide.chestshop.ChestShop;
import com.github.unldenis.hologram.Hologram;
import lombok.Getter;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.network.PlayerConnection;
import net.minecraft.world.entity.decoration.EntityArmorStand;
import net.minecraft.world.entity.item.EntityItem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FloatingItem {

    /*private Location location;

    @Getter
    private List<UUID> loadedPlayers;

    private Packet[] spawnPackets, despawnPackets;*/

    private Hologram hologram;

    public FloatingItem( ItemStack itemStack, Location location ) {
     //   this.location = location;
       // this.loadedPlayers = new ArrayList<>();

        this.hologram = Hologram.builder()
                .location(location)
                .addLine(itemStack)
                .build(ChestShop.getInstance().getHologramPool());

        //int armorStandId = FloatingItemManager.getRandomEntityID();
        //int itemId = FloatingItemManager.getRandomEntityID();
/*
        EntityItem entityItem = FloatingPacket.createItem( this.location, itemStack );
        EntityArmorStand entityArmorStand = FloatingPacket.createArmorStand(this.location);
        this.spawnPackets = new Packet[] {
                new PacketPlayOutSpawnEntityLiving(entityArmorStand),
                FloatingPacket.itemSpawn( entityItem ),
                FloatingPacket.itemMeta( entityItem.getBukkitEntity().getEntityId(), entityItem.ai() ),
                FloatingPacket.attachItemToArmorStand( entityItem, entityArmorStand )
        };

        this.despawnPackets = new Packet[] {
                FloatingPacket.destroyEntity( entityItem.getBukkitEntity().getEntityId() ),
                FloatingPacket.destroyEntity(entityArmorStand.getBukkitEntity().getEntityId() )
        };

        for ( Player player : Bukkit.getOnlinePlayers() ) {
            this.createForPlayer( player );
        }*/
    }

    /**
     * Remove floating item
     */
    public void remove() {
        if(this.hologram == null) {
            return;
        }
        ChestShop.getInstance().getHologramPool().remove(this.hologram);
        this.hologram = null;
        /*for ( Player player : Bukkit.getOnlinePlayers() ) {
            this.removeForPlayer( player );
        }*/
    }


    /**
     * Create floating item for a player
     *
     * @param player for creating
     */
      /*public void createForPlayer( Player player ) {
        FloatingItemManager.addFloatingItemToPlayer( player, this );
        this.checkSending( player, player.getLocation() );
    }

    /**
     * Remove floating item from a player
     *
     * @param player for removing
     */
      /*public void removeForPlayer( Player player ) {
        FloatingItemManager.removeFloatingItemFromPlayer( player, this );
        this.removeFromPlayer( player );
    }

    /**
     * Send create floating item packets to a player
     *
     * @param player to send
     */
      /*private void sendToPlayer( Player player ) {
        if ( this.isPlayerLoaded( player ) ) return;
        this.loadedPlayers.add( player.getUniqueId() );

        PlayerConnection playerConnection = ( (CraftPlayer) player ).getHandle().b;

        for ( Packet packet : this.spawnPackets ) {
            playerConnection.a(packet);
        }
    }

    /**
     * Send remove floating item packets to a player
     *
     * @param player to send
     */
     /* private void removeFromPlayer( Player player ) {
        if ( !this.isPlayerLoaded( player ) ) return;
        PlayerConnection playerConnection = ( (CraftPlayer) player ).getHandle().b;

        for ( Packet packet : this.despawnPackets ) {
            playerConnection.a( packet );
        }
        this.loadedPlayers.remove( player.getUniqueId() );
    }

    /**
     * Check if the player is in range of the floating item
     *
     * @param player for checking
     * @param to     location for checking
     */
     /* public void checkSending( Player player, Location to ) {
        if ( this.isInRange( to ) ) {
            this.sendToPlayer( player );
            return;
        }
        this.removeFromPlayer( player );
    }

    /**
     * Check if the player is loaded
     *
     * @param player to check
     * @return if the player is loaded
     */
      /*public boolean isPlayerLoaded( Player player ) {
        return this.loadedPlayers.contains( player.getUniqueId() );
    }

    /**
     * Check if the location is in range of the floating item
     *
     * @param location to check
     * @return if the location is in range
     */
      /*public boolean isInRange( Location location ) {
        return this.location.getWorld().equals( location.getWorld() ) && ( this.location.distance( location ) <= 48D );
    }*/
}
