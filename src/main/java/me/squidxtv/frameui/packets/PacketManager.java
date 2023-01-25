package me.squidxtv.frameui.packets;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import me.squidxtv.frameui.FrameUI;
import me.squidxtv.frameui.core.properties.Direction;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Manager for Packet sending.
 */
public final class PacketManager {

    private static final int[] YAW = {0, 0, 180, 0, 90, 270};
    private static final int[] PITCH = {90, -90, 0, 0, 0, 0};

    private final @NotNull FrameUI plugin;
    private final @NotNull ProtocolManager manager;
    private final @NotNull BukkitScheduler scheduler;

    public PacketManager(FrameUI plugin) {
        this.plugin = plugin;
        this.manager = plugin.getProtocolManager();
        this.scheduler = Bukkit.getScheduler();
    }

    public void sendItemFramePacket(Collection<Player> players, ItemFramePacket packet) {
        scheduler.runTaskAsynchronously(plugin, () -> {
            for (Player player : players) {
                manager.sendServerPacket(player, packet.itemFrame());
                manager.sendServerPacket(player, packet.data());
            }
        });
    }

    /**
     * Creates Item Frame packet using given configuration.
     * @param map Map inside item frame
     * @param x x coordinate in world
     * @param y y coordinate in world
     * @param z z coordinate in world
     * @param invisible sets item frame itself invisible
     * @param direction direction of item frame
     * @return {@link ItemFramePacket}
     */
    public ItemFramePacket createItemFrame(ItemStack map, double x, double y, double z, boolean invisible, Direction direction) {
        if (map.getType() != Material.FILLED_MAP) {
            throw new IllegalArgumentException("ItemStack must be of type FILLED_MAP.");
        }

        PacketContainer itemFrame = manager.createPacket(PacketType.Play.Server.SPAWN_ENTITY);

        int entityID = UUID.randomUUID().hashCode();
        setItemFrameData(itemFrame, entityID, x, y, z, direction.getInternalDirection());

        PacketContainer entityData = manager.createPacket(PacketType.Play.Server.ENTITY_METADATA);
        setEntityData(entityData, entityID, map, invisible);

        return new ItemFramePacket(itemFrame, entityData, entityID);
    }

    /**
     * Sets the Item Frame data in the packet.
     * @param packet Packet
     * @param entityID entity ID
     * @param x x-position
     * @param y y-position
     * @param z z-position
     * @param direction direction of item frame
     */
    private static void setItemFrameData(PacketContainer packet, int entityID, double x, double y, double z, int direction) {
        packet.getIntegers().write(0, entityID);
        packet.getEntityTypeModifier().write(0, EntityType.ITEM_FRAME);
        packet.getUUIDs().write(0, UUID.randomUUID());

        packet.getDoubles().write(0, x);
        packet.getDoubles().write(1, y);
        packet.getDoubles().write(2, z);

        packet.getBytes().write(0, (byte) (PITCH[direction] * 256.0F / 360.0F));
        packet.getBytes().write(1, (byte) (YAW[direction] * 256.0F / 360.0F));
        packet.getIntegers().write(4, direction);
    }

    /**
     * Sets entity data in the packet.
     * @param entityData Packet
     * @param entityID entity ID
     * @param item item
     * @param invisible is invisible
     */
    private static void setEntityData(PacketContainer entityData, int entityID, ItemStack item, boolean invisible) {
        entityData.getIntegers().write(0, entityID);

        WrappedDataWatcher dataWatcher = new WrappedDataWatcher();
        if (invisible) {
            dataWatcher.setObject(0, WrappedDataWatcher.Registry.get(Byte.class), (byte) 0x20);
        }

        dataWatcher.setObject(8, WrappedDataWatcher.Registry.getItemStackSerializer(false), item);
        entityData.getWatchableCollectionModifier().write(0, dataWatcher.getWatchableObjects());
    }

    /**
     * Sends destroy packets to remove item frames from the client.
     * @param players players
     * @param entityIDs entity IDs
     */
    public void removeItemFrames(Collection<Player> players, int[][] entityIDs) {
        List<Integer> ids = new ArrayList<>();
        for (int[] row : entityIDs) {
            for (int i : row) {
                ids.add(i);
            }
        }

        PacketContainer destroy = manager.createPacket(PacketType.Play.Server.ENTITY_DESTROY);
        destroy.getIntLists().write(0, ids);

        for (Player player : players) {
            manager.sendServerPacket(player, destroy);
        }
    }
}
