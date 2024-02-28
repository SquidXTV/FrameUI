package me.squidxtv.frameui.core.itemframe;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.player.PlayerManager;
import com.github.retrooper.packetevents.manager.protocol.ProtocolManager;
import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerDestroyEntities;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityMetadata;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSpawnEntity;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import io.github.retrooper.packetevents.util.SpigotReflectionUtil;
import me.squidxtv.frameui.api.FrameAPI;
import me.squidxtv.frameui.core.map.VirtualMap;
import me.squidxtv.frameui.core.math.Direction;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.logging.Logger;

public class VirtualItemFrame extends AbstractItemFrame<VirtualMap> {

    private static final ProtocolManager PROTOCOL_MANAGER = PacketEvents.getAPI().getProtocolManager();
    private static final @NotNull PlayerManager PLAYER_MANAGER = PacketEvents.getAPI().getPlayerManager();
    private static final @NotNull Logger LOGGER = Objects.requireNonNull(Bukkit.getServicesManager().load(FrameAPI.class)).getLogger();

    private final int entityId = SpigotReflectionUtil.generateEntityId();
    private final SpawnPacket spawn;
    private final MetadataPacket metadata;

    private boolean invisible = true;

    public VirtualItemFrame(@NotNull World world, @NotNull Location location, @NotNull Direction direction) {
        super(location, new VirtualMap(world));
        spawn = new SpawnPacket(entityId, location, direction);
        metadata = new MetadataPacket(entityId, map.getAsItemStack(), invisible);
    }

    public void send(@NotNull Collection<Player> players) {
        for (Player player : players) {
            PLAYER_MANAGER.sendPacket(player, spawn);
            PLAYER_MANAGER.sendPacket(player, metadata);
            PLAYER_MANAGER.sendPacket(player, map.getDataPacket());
        }
    }

    public void update(Collection<Player> players) {
        if (!map.isChanged()) {
            return;
        }

        for (Player player : players) {
            PLAYER_MANAGER.sendPacket(player, map.getDataPacket());
        }
    }

    public void update(Player player) {
        update(List.of(player));
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

    public int getEntityId() {
        return entityId;
    }

    public @NotNull SpawnPacket getSpawn() {
        return spawn;
    }

    public @NotNull MetadataPacket getMetadata() {
        return metadata;
    }

    public boolean isInvisible() {
        return invisible;
    }

    public void setInvisible(boolean invisible) {
        this.invisible = invisible;
        for (EntityData data : metadata.getEntityMetadata()) {
            if (data.getIndex() == 0) {
                data.setValue((byte) (invisible ? 0x20 : 0));
            }
        }
    }

    public void setLocation(@NotNull Location location) {
        Vector3d pos = new Vector3d(location.getX(), location.getY(), location.getZ());
        spawn.setPosition(pos);
    }

    public void setDirection(@NotNull Direction direction) {
        spawn.setPitch(direction.getPitch());
        spawn.setYaw(direction.getYaw());
        spawn.setData(direction.getPacketValue());
    }

    @Override
    public void setMap(@NotNull VirtualMap map) {
        super.setMap(map);
        for (EntityData data : metadata.getEntityMetadata()) {
            if (data.getIndex() == 8) {
                data.setValue(SpigotConversionUtil.fromBukkitItemStack(map.getAsItemStack()));
            }
        }
    }


    public static class SpawnPacket extends WrapperPlayServerSpawnEntity {

        public SpawnPacket(int entityID, @NotNull Location loc, @NotNull Direction direction) {
            super(entityID,
                    Optional.of(UUID.randomUUID()),
                    EntityTypes.ITEM_FRAME,
                    new Vector3d(loc.getX(), loc.getY(), loc.getZ()),
                    direction.getPitch(),
                    direction.getYaw(),
                    0,
                    direction.getPacketValue(),
                    Optional.empty());
        }

    }

    public static class MetadataPacket extends WrapperPlayServerEntityMetadata {

        public MetadataPacket(int entityID, @NotNull ItemStack map, boolean invisible) {
            super(entityID, List.of(convertToEntityData(map), convertToEntityData(invisible)));

        }

        private static @NotNull EntityData convertToEntityData(boolean invisible) {
            return new EntityData(0, EntityDataTypes.BYTE, (byte) (invisible ? 0x20 : 0));
        }

        private static @NotNull EntityData convertToEntityData(@NotNull ItemStack map) {
            return new EntityData(8, EntityDataTypes.ITEMSTACK, SpigotConversionUtil.fromBukkitItemStack(map));
        }

    }


    public static void destroy(@NotNull VirtualItemFrame[] frames, @NotNull Collection<Player> players) {
        int[] ids = Arrays.stream(frames).mapToInt(VirtualItemFrame::getEntityId).toArray();
        WrapperPlayServerDestroyEntities destroy = new WrapperPlayServerDestroyEntities(ids);
        for (Player player : players) {
//            PROTOCOL_MANAGER.sendPacket(PLAYER_MANAGER.getChannel(player), destroy);
            PLAYER_MANAGER.sendPacket(player, destroy);
        }
    }

    public static void destroy(@NotNull VirtualItemFrame[] frames, @NotNull Player player) {
        destroy(frames, List.of(player));
    }

}
