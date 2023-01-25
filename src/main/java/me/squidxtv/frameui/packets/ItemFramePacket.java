package me.squidxtv.frameui.packets;

import com.comphenix.protocol.events.PacketContainer;
import org.jetbrains.annotations.NotNull;

/**
 * Represents all packet information about sending item frames.
 * @param itemFrame generating item frame packet
 * @param data setting data for item frame packet
 * @param entityID entity id of item frame
 */
public record ItemFramePacket(PacketContainer itemFrame, PacketContainer data, int entityID) {
}
