package me.squidxtv.frameui.core.impl;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.player.PlayerManager;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerDestroyEntities;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerMapData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.squidxtv.frameui.core.ItemFrame;
import me.squidxtv.frameui.core.MapItem;
import me.squidxtv.frameui.core.Screen;
import me.squidxtv.frameui.core.ScreenSpawner;
import me.squidxtv.frameui.packets.ItemFrameMetadataPacket;
import me.squidxtv.frameui.packets.ItemFrameSpawnPacket;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * The {@code PacketScreenSpawner} uses packets to spawn, despawn and update the
 * {@link Screen}.
 */
public class PacketScreenSpawner implements ScreenSpawner {

    private static final PlayerManager PLAYER_MANAGER = PacketEvents.getAPI().getPlayerManager();

    @Override
    public void spawn(Screen screen, Collection<Player> viewers) {
        ItemFrame[][] itemFrames = screen.getItemFrames();

        for (ItemFrame[] row : itemFrames) {
            for (ItemFrame frame : row) {
                ItemFrameSpawnPacket spawn = new ItemFrameSpawnPacket(frame.getEntityId(), frame.getLocation(), frame.getDirection());
                ItemFrameMetadataPacket metadata = new ItemFrameMetadataPacket(frame.getEntityId(), frame.getMapItem().getAsItemStack(),
                        frame.isInvisible());

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
        int[] ids = Arrays.stream(frames).flatMap(Arrays::stream).mapToInt(ItemFrame::getEntityId).toArray();

        WrapperPlayServerDestroyEntities destroy = new WrapperPlayServerDestroyEntities(ids);
        for (Player viewer : viewers) {
            PLAYER_MANAGER.sendPacket(viewer, destroy);
        }
    }

    @Override
    public void update(Screen screen) {
        List<Player> viewers = screen.getViewers().stream().map(Bukkit::getPlayer).filter(Objects::nonNull).toList();
        ItemFrame[][] frames = screen.getItemFrames();
        for (ItemFrame[] row : frames) {
            for (ItemFrame frame : row) {
                MapItem map = frame.getMapItem();
                if (!map.isChanged()) {
                    continue;
                }

                WrapperPlayServerMapData update = new WrapperPlayServerMapData(map.getId(), (byte) 0, false, false, null, MapItem.WIDTH,
                        MapItem.HEIGHT, 0, 0, map.getData());
                for (Player viewer : viewers) {
                    PLAYER_MANAGER.sendPacket(viewer, update);
                }
                map.resetChanged();
            }
        }
    }

}
