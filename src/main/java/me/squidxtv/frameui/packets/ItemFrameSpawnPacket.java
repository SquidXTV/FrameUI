/*
 * FrameUI: Minecraft plugin library designed to easily create screens within a server.
 * Copyright (C) 2023-2024 Connor Schweighöfer
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package me.squidxtv.frameui.packets;

import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSpawnEntity;
import org.bukkit.Location;

import me.squidxtv.frameui.math.Direction;

import java.util.Optional;
import java.util.UUID;

/**
 * The {@code ItemFrameSpawnPacket} is a {@link WrapperPlayServerSpawnEntity}
 * packet specifically for spawning item frames.
 */
public final class ItemFrameSpawnPacket extends WrapperPlayServerSpawnEntity {

    /**
     * Creates a new {@code ItemFrameSpawnPacket} with the specific entity id,
     * location and direction.
     * 
     * @param entityId  the id of the item frame entity
     * @param location  the {@link Location} where the item frame should spawn
     * @param direction the {@link Direction} the item frame should face
     */
    public ItemFrameSpawnPacket(int entityId, Location location, Direction direction) {
        super(entityId, Optional.of(UUID.randomUUID()), EntityTypes.ITEM_FRAME, toVector3d(location), direction.getPitch(),
                direction.getYaw(), 0, direction.getPacketId(), Optional.empty());
    }

    private static Vector3d toVector3d(Location location) {
        return new Vector3d(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

}
