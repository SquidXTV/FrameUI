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

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityMetadata;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * The {@code ItemFrameMetadataPacket} is a
 * {@link WrapperPlayServerEntityMetadata} packet specifically for item frames.
 */
public final class ItemFrameMetadataPacket extends WrapperPlayServerEntityMetadata {

    /**
     * Creates a new {@code ItemFrameMetadataPacket} with the specific entity id,
     * map and visibility.
     * 
     * @param entityId  the id of the item frame entity
     * @param map       the {@link ItemStack} representing the map to be displayed
     *                  in the item frame
     * @param invisible a boolean indicating whether the item frame should be
     *                  invisible
     */
    public ItemFrameMetadataPacket(int entityId, ItemStack map, boolean invisible) {
        super(entityId, List.of(mapToEntityData(map), invisibleToEntityData(invisible)));
    }

    private static EntityData mapToEntityData(ItemStack map) {
        return new EntityData(8, EntityDataTypes.ITEMSTACK, SpigotConversionUtil.fromBukkitItemStack(map));
    }

    private static EntityData invisibleToEntityData(boolean invisible) {
        return new EntityData(0, EntityDataTypes.BYTE, (byte) (invisible ? 0x20 : 0));
    }

}
