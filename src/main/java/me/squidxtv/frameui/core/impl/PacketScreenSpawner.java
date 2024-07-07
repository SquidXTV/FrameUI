package me.squidxtv.frameui.virtual;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.player.PlayerManager;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerDestroyEntities;
import io.github.retrooper.packetevents.util.SpigotReflectionUtil;
import me.squidxtv.frameui.core.ItemFrame;
import me.squidxtv.frameui.core.Screen;
import me.squidxtv.frameui.core.ScreenSpawner;
import me.squidxtv.frameui.packets.ItemFrameMetadataPacket;
import me.squidxtv.frameui.packets.ItemFrameSpawnPacket;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class VirtualScreenSpawner implements ScreenSpawner {

    private static final PlayerManager PLAYER_MANAGER = PacketEvents.getAPI().getPlayerManager();

    private final Map<ItemFrame, Integer> entityIds = new HashMap<>();

    @Override
    public void spawn(Screen screen, Collection<Player> viewers) {
        ItemFrame[][] itemFrames = screen.getItemFrames();

        for (ItemFrame[] row : itemFrames) {
            for (ItemFrame frame : row) {
                int id = entityIds.computeIfAbsent(frame, _ -> SpigotReflectionUtil.generateEntityId());
                ItemFrameSpawnPacket spawn = new ItemFrameSpawnPacket(id, frame.getLocation(), frame.getDirection());
                ItemFrameMetadataPacket metadata = new ItemFrameMetadataPacket(id, frame.getMapItem().getAsItemStack(), frame.isInvisible());

                for (Player viewer : viewers) {
                    PLAYER_MANAGER.sendPacket(viewer, spawn);
                    PLAYER_MANAGER.sendPacket(viewer, metadata);
                }
            }
        }
    }

    @Override
    public void despawn(Screen screen, Collection<Player> viewers) {
        ItemFrame[][] frames = screen.getItemFrames();
        int[] ids = Arrays.stream(frames)
                .flatMap(Arrays::stream)
                .map(entityIds::get)
                .filter(Objects::nonNull)
                .mapToInt(value -> value)
                .toArray();

        WrapperPlayServerDestroyEntities destroy = new WrapperPlayServerDestroyEntities(ids);
        for (Player viewer : viewers) {
            PLAYER_MANAGER.sendPacket(viewer, destroy);
        }
    }

    @Override
    public void despawn(Screen screen) {
        ItemFrame[][] frames = screen.getItemFrames();
        int[] ids = Arrays.stream(frames)
                .flatMap(Arrays::stream)
                .map(entityIds::remove)
                .filter(Objects::nonNull)
                .mapToInt(value -> value)
                .toArray();

        WrapperPlayServerDestroyEntities destroy = new WrapperPlayServerDestroyEntities(ids);
        for (Player viewer : screen.getViewers()) {
            PLAYER_MANAGER.sendPacket(viewer, destroy);
        }
    }

}
