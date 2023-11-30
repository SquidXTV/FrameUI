package me.squidxtv.frameui.core.map;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.player.PlayerManager;
import com.github.retrooper.packetevents.manager.protocol.ProtocolManager;
import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerDestroyEntities;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityMetadata;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSpawnEntity;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import io.github.retrooper.packetevents.util.SpigotReflectionUtil;
import me.squidxtv.frameui.api.FrameAPI;
import me.squidxtv.frameui.core.math.Direction;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VirtualMap extends AbstractMap {

    private static final @NotNull Logger LOGGER = Objects.requireNonNull(Bukkit.getServicesManager().load(FrameAPI.class)).getLogger();

    private final @NotNull Packet packet;

    public VirtualMap(@NotNull World world, @NotNull Location location, @NotNull Direction direction) {
        super(world);
        this.packet = new Packet(item, location, direction);
    }

    @Override
    public void update(@NotNull Color pixel, int x, int y) {
        renderer.setPixel(pixel, x, y);
    }

    public @NotNull Packet getPacket() {
        return packet;
    }

    public static class Packet {

        /**
         * The ProtocolManager instance used for sending packets to players.
         */
        private static final @NotNull ProtocolManager PROTOCOL_MANAGER = PacketEvents.getAPI().getProtocolManager();

        private static final @NotNull PlayerManager PLAYER_MANAGER = PacketEvents.getAPI().getPlayerManager();

        private final @NotNull WrapperPlayServerSpawnEntity frame;
        private final @NotNull WrapperPlayServerEntityMetadata metadata;

        private final int entityId;
        private final @NotNull ItemStack map;

        public Packet(@NotNull ItemStack map, @NotNull Location location, @NotNull Direction direction) {
            this.entityId = SpigotReflectionUtil.generateEntityId();
            this.map = map;
            this.frame = new ItemFrameSpawnPacket(entityId, location, direction);
            this.metadata = new ItemFrameMetadataPacket(entityId, map, false);
        }

        public @NotNull WrapperPlayServerSpawnEntity getSpawnEntity() {
            return frame;
        }

        public @NotNull WrapperPlayServerEntityMetadata getMetadata() {
            return metadata;
        }

        public int getEntityId() {
            return entityId;
        }

        public void send(@NotNull Collection<Player> players) {
            if (map.getType() != Material.FILLED_MAP) {
                LOGGER.fine("Item type is not FILLED_MAP. Cancel packet send.");
                return;
            }

            if (map.getItemMeta() == null) {
                LOGGER.fine("Item meta is null. Cancel packet send.");
                return;
            }

            if (!(map.getItemMeta() instanceof MapMeta meta)) {
                LOGGER.fine("Item meta is not an instance of MapMeta. Cancel packet send.");
                return;
            }

            MapView view = meta.getMapView();
            if (view == null) {
                LOGGER.fine("Map view is null. Cancel packet send.");
                return;
            }

            for (Player player : players) {
                PLAYER_MANAGER.sendPacket(player, frame);
                PLAYER_MANAGER.sendPacket(player, metadata);
                player.sendMap(view);
            }
            LOGGER.log(Level.INFO, "Map ({0}, {1}) got send.", new Object[]{entityId, frame.getPosition()});
        }

        public void send(@NotNull Player player) {
            send(List.of(player));
        }

        public void destroy(@NotNull Collection<Player> players) {
            WrapperPlayServerDestroyEntities destroy = new WrapperPlayServerDestroyEntities(entityId);
            for (Player player : players) {
                PROTOCOL_MANAGER.sendPacket(PLAYER_MANAGER.getChannel(player), destroy);
            }
        }

        public void destroy(@NotNull Player player) {
            destroy(List.of(player));
        }

        public static void destroy(@NotNull VirtualMap[] maps, @NotNull Collection<Player> players) {
            int[] ids = Arrays.stream(maps).mapToInt(virtualMap -> virtualMap.packet.entityId).toArray();
            WrapperPlayServerDestroyEntities destroy = new WrapperPlayServerDestroyEntities(ids);
            for (Player player : players) {
                PROTOCOL_MANAGER.sendPacket(PLAYER_MANAGER.getChannel(player), destroy);
            }
        }

        public static void destroy(@NotNull VirtualMap[] maps, @NotNull Player player) {
            destroy(maps, List.of(player));
        }

        public static class ItemFrameSpawnPacket extends WrapperPlayServerSpawnEntity {

            public ItemFrameSpawnPacket(int entityID, @NotNull Location loc, @NotNull Direction direction) {
                super(entityID,
                        Optional.empty(),
                        EntityTypes.ITEM_FRAME,
                        new Vector3d(loc.getX(), loc.getY(), loc.getZ()),
                        direction.getPitch(),
                        direction.getYaw(),
                        0,
                        direction.getPacketValue(),
                        Optional.empty());
            }

        }

        public static class ItemFrameMetadataPacket extends WrapperPlayServerEntityMetadata {

            public ItemFrameMetadataPacket(int entityID, @NotNull ItemStack map, boolean invisible) {
                super(entityID, List.of(setInvisible(invisible), setMap(map)));
            }


            private static EntityData setInvisible(boolean invisible) {
                return new EntityData(0, EntityDataTypes.BYTE, (byte) (invisible ? 0x20 : 0));
            }

            private static EntityData setMap(@NotNull ItemStack map) {
                return new EntityData(8, EntityDataTypes.ITEMSTACK, SpigotConversionUtil.fromBukkitItemStack(map));
            }

        }
        
    }

}
