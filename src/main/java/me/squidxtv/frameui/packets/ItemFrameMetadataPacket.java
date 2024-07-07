package me.squidxtv.frameui.packets;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityMetadata;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * The {@code ItemFrameMetadataPacket} is a {@link WrapperPlayServerEntityMetadata} packet specifically for
 * item frames.
 */
public final class ItemFrameMetadataPacket extends WrapperPlayServerEntityMetadata {

    /**
     * Creates a new {@code ItemFrameMetadataPacket} with the specific entity id, map and visibility.
     * @param entityId the id of the item frame entity
     * @param map the {@link ItemStack} representing the map to be displayed in the item frame
     * @param invisible a boolean indicating whether the item frame should be invisible
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
