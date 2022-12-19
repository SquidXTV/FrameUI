package com.squidxtv.frameui.packets;

import com.comphenix.protocol.events.PacketContainer;
import org.jetbrains.annotations.NotNull;

public record ItemFramePacket(@NotNull PacketContainer itemFrame, @NotNull PacketContainer itemFrameData, int entityID) {
}
