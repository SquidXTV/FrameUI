package me.squidxtv.frameui.core.map;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import me.squidxtv.frameui.api.FrameAPI;
import me.squidxtv.frameui.core.math.Direction;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.logging.Logger;

public class VirtualMap extends AbstractMap {

    private static final @NotNull Logger LOGGER = Objects.requireNonNull(Bukkit.getServicesManager().load(FrameAPI.class)).getLogger();

    private final @NotNull Location location;
    private final @NotNull Direction direction;
    private final @NotNull Packet packet;

    public VirtualMap(@NotNull World world, @NotNull Location location, @NotNull Direction direction) {
        super(world);
        this.location = location;
        this.direction = direction;
        this.packet = new Packet();
        packet.data.setInvisible(true);
    }

    @Override
    public void update(@NotNull Color pixel, int x, int y) {
        renderer.setPixel(pixel, x, y);
    }

    public @NotNull Location getLocation() {
        return location;
    }

    public @NotNull Direction getDirection() {
        return direction;
    }

    public @NotNull Packet getPacket() {
        return packet;
    }

    public class Packet {

        /**
         * An array representing the yaw of each direction of the item frame.
         */
        private static final byte[] YAW;

        /**
         * An array representing the pitch of each direction of the item frame.
         */
        private static final byte[] PITCH;

        /**
         * The ProtocolManager instance used for sending packets to players.
         */
        private static final @NotNull ProtocolManager PROTOCOL_MANAGER = ProtocolLibrary.getProtocolManager();

        static {
            YAW = new byte[6];
            PITCH = new byte[6];

            int[] yawInRadiant  = {0, 0, 180, 0, 90, 270};
            for (int i = 0; i < yawInRadiant.length; i++) {
                YAW[i] = (byte) (yawInRadiant[i] * 256.0F / 360.0F);
            }

            int[] pitchInRadiant = {90, -90, 0, 0, 0, 0};
            for (int i = 0; i < pitchInRadiant.length; i++) {
                PITCH[i] = (byte) (pitchInRadiant[i] * 256.0F / 360.0F);
            }
        }


        private final @NotNull ItemFramePacket frame;
        private final @NotNull ItemFrameDataPacket data;
        private final int entityId;

        public Packet() {
            this.entityId = UUID.randomUUID().hashCode();
            this.frame = new ItemFramePacket(entityId);
            this.data = new ItemFrameDataPacket(entityId);

            frame.setLocation(location);
            frame.setDirection(direction);
            data.setMap(item);
        }

        public @NotNull ItemFramePacket getFrame() {
            return frame;
        }

        public @NotNull ItemFrameDataPacket getData() {
            return data;
        }

        public int getEntityId() {
            return entityId;
        }

        public void send(@NotNull Collection<Player> players) {
            if (item.getType() != Material.FILLED_MAP) {
                LOGGER.fine("Item type is not FILLED_MAP. Cancel packet send.");
                return;
            }

            if (item.getItemMeta() == null) {
                LOGGER.fine("Item meta is null. Cancel packet send.");
                return;
            }

            if (!(item.getItemMeta() instanceof MapMeta meta)) {
                LOGGER.fine("Item meta is not an instance of MapMeta. Cancel packet send.");
                return;
            }

            MapView view = meta.getMapView();
            if (view == null) {
                LOGGER.fine("Map view is null. Cancel packet send.");
                return;
            }

            for (Player player : players) {
                PROTOCOL_MANAGER.sendServerPacket(player, frame);
                PROTOCOL_MANAGER.sendServerPacket(player, data);
                player.sendMap(view);
            }
        }

        public void send(@NotNull Player player) {
            send(List.of(player));
        }

        public void destroy(@NotNull Collection<Player> players) {
            PacketContainer destroy = PROTOCOL_MANAGER.createPacket(PacketType.Play.Server.ENTITY_DESTROY);
            destroy.getIntLists().write(0, List.of(entityId));
            for (Player player : players) {
                PROTOCOL_MANAGER.sendServerPacket(player, destroy);
            }
        }

        public void destroy(@NotNull Player player) {
            destroy(List.of(player));
        }
        
        public static void destroy(@NotNull VirtualMap[] maps, @NotNull Collection<Player> players) {
            List<Integer> ids = Arrays.stream(maps).map(virtualMap -> virtualMap.packet.entityId).toList();
            PacketContainer destroy = PROTOCOL_MANAGER.createPacket(PacketType.Play.Server.ENTITY_DESTROY);
            destroy.getIntegers().write(0, ids.size());
            destroy.getIntLists().write(0, ids);
            for (Player player : players) {
                PROTOCOL_MANAGER.sendServerPacket(player, destroy);
            }
        }

        public static void destroy(@NotNull VirtualMap[] maps, @NotNull Player player) {
            destroy(maps, List.of(player));
        }
        
        public static class ItemFramePacket extends PacketContainer {

            public ItemFramePacket(int entityId) {
                super(PacketType.Play.Server.SPAWN_ENTITY);
                getModifier().writeDefaults();

                getIntegers().write(0, entityId);
                getEntityTypeModifier().write(0, EntityType.ITEM_FRAME);
                getUUIDs().write(0, UUID.randomUUID());
            }

            public void setLocation(@NotNull Location location) {
                setX(location.getX());
                setY(location.getY());
                setZ(location.getZ());
            }

            public void setX(double x) {
                getDoubles().write(0, x);
            }

            public void setY(double y) {
                getDoubles().write(1, y);
            }

            public void setZ(double z) {
                getDoubles().write(2, z);
            }

            public void setDirection(@NotNull Direction direction) {
                int index = direction.getPacketDirection();
                getBytes().write(0, PITCH[index]);
                getBytes().write(1, YAW[index]);
                getIntegers().write(4, index);
            }
        }

        public static class ItemFrameDataPacket extends PacketContainer {

            public ItemFrameDataPacket(int entityId) {
                super(PacketType.Play.Server.ENTITY_METADATA);
                getModifier().writeDefaults();

                getIntegers().write(0, entityId);
            }

            public void setInvisible(boolean invisible) {
                WrappedDataWatcher dataWatcher = new WrappedDataWatcher(getWatchableCollectionModifier().read(0));

                if (invisible) {
                    dataWatcher.setObject(0, WrappedDataWatcher.Registry.get(Byte.class), (byte) 0x20);
                } else {
                    dataWatcher.setObject(0, WrappedDataWatcher.Registry.get(Byte.class), (byte) 0);
                }

                getWatchableCollectionModifier().write(0, dataWatcher.getWatchableObjects());
            }

            public void setMap(@NotNull ItemStack map) {
                if (map.getType() != Material.FILLED_MAP) {
                    throw new IllegalArgumentException("ItemStack must be of type FILLED_MAP.");
                }

                WrappedDataWatcher dataWatcher = new WrappedDataWatcher(getWatchableCollectionModifier().read(0));
                dataWatcher.setObject(8, WrappedDataWatcher.Registry.getItemStackSerializer(false), map);
                getWatchableCollectionModifier().write(0, dataWatcher.getWatchableObjects());
            }
        }
        
    }

}
