package me.squidxtv.frameui.util.protocol;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityMetadata;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ItemFrameMetadataPacket extends WrapperPlayServerEntityMetadata {

    public ItemFrameMetadataPacket(int entityID, @NotNull ItemStack map, boolean invisible) {
        super(entityID, List.of(convertToEntityData(map), convertToEntityData(invisible)));
    }

    private static @NotNull EntityData convertToEntityData(boolean invisible) {
        return new EntityData(0, EntityDataTypes.BYTE, (byte) (invisible ? 0x20 : 0));
    }

    private static @NotNull EntityData convertToEntityData(@NotNull ItemStack map) {
        return new EntityData(8, EntityDataTypes.ITEMSTACK, SpigotConversionUtil.fromBukkitItemStack(map));
    }

}
