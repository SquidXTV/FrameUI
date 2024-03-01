package me.squidxtv.frameui.util.protocol;

import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSpawnEntity;
import me.squidxtv.frameui.core.math.Direction;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

public class ItemFrameSpawnPacket extends WrapperPlayServerSpawnEntity {

    public ItemFrameSpawnPacket(int entityID, @NotNull Location location, @NotNull Direction direction) {
        super(entityID,
                Optional.of(UUID.randomUUID()),
                EntityTypes.ITEM_FRAME,
                new Vector3d(location.getBlockX(), location.getBlockY(), location.getBlockZ()),
                direction.getPitch(),
                direction.getYaw(),
                0,
                direction.getPacketValue(),
                Optional.empty());
    }


}
