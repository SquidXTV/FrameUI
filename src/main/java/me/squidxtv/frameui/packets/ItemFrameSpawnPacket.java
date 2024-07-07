package me.squidxtv.frameui.packets;

import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSpawnEntity;
import me.squidxtv.frameui.math.Direction;
import org.bukkit.Location;

import java.util.Optional;
import java.util.UUID;

/**
 * The {@code ItemFrameSpawnPacket} is a {@link WrapperPlayServerSpawnEntity} packet specifically for spawning
 * item frames.
 */
public final class ItemFrameSpawnPacket extends WrapperPlayServerSpawnEntity {

    /**
     * Creates a new {@code ItemFrameSpawnPacket} with the specific entity id, location and direction.
     * @param entityId the id of the item frame entity
     * @param location the {@link Location} where the item frame should spawn
     * @param direction the {@link Direction} the item frame should face
     */
    public ItemFrameSpawnPacket(int entityId, Location location, Direction direction) {
        super(entityId,
                Optional.of(UUID.randomUUID()),
                EntityTypes.ITEM_FRAME,
                new Vector3d(location.getBlockX(), location.getBlockY(), location.getBlockZ()),
                direction.getPitch(),
                direction.getYaw(),
                0,
                direction.getPacketId(),
                Optional.empty());
    }

}
