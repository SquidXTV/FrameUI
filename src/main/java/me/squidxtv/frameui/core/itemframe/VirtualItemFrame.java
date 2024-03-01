package me.squidxtv.frameui.core.itemframe;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.player.PlayerManager;
import com.github.retrooper.packetevents.manager.protocol.ProtocolManager;
import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerDestroyEntities;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import io.github.retrooper.packetevents.util.SpigotReflectionUtil;
import me.squidxtv.frameui.core.map.VirtualMap;
import me.squidxtv.frameui.core.math.Direction;
import me.squidxtv.frameui.util.protocol.ItemFrameMetadataPacket;
import me.squidxtv.frameui.util.protocol.ItemFrameSpawnPacket;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class VirtualItemFrame extends AbstractItemFrame<VirtualMap> {

    private static final ProtocolManager PROTOCOL_MANAGER = PacketEvents.getAPI().getProtocolManager();
    private static final @NotNull PlayerManager PLAYER_MANAGER = PacketEvents.getAPI().getPlayerManager();

    private final int entityId = SpigotReflectionUtil.generateEntityId();
    private final ItemFrameSpawnPacket spawn;
    private final ItemFrameMetadataPacket metadata;

    private boolean invisible = true;

    public VirtualItemFrame(@NotNull World world, @NotNull Location location, @NotNull Direction direction) {
        super(location, new VirtualMap(world));
        spawn = new ItemFrameSpawnPacket(entityId, location, direction);
        metadata = new ItemFrameMetadataPacket(entityId, map.getAsItemStack(), invisible);
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

    public @NotNull ItemFrameSpawnPacket getSpawn() {
        return spawn;
    }

    public @NotNull ItemFrameMetadataPacket getMetadata() {
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

    @Override
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

    public static void destroy(@NotNull VirtualItemFrame[] frames, @NotNull Collection<Player> players) {
        int[] ids = Arrays.stream(frames).mapToInt(VirtualItemFrame::getEntityId).toArray();
        WrapperPlayServerDestroyEntities destroy = new WrapperPlayServerDestroyEntities(ids);
        for (Player player : players) {
            // ToDo: test this
//            PROTOCOL_MANAGER.sendPacket(PLAYER_MANAGER.getChannel(player), destroy);
            PLAYER_MANAGER.sendPacket(player, destroy);
        }
    }

    public static void destroy(@NotNull VirtualItemFrame[] frames, @NotNull Player player) {
        destroy(frames, List.of(player));
    }

}
