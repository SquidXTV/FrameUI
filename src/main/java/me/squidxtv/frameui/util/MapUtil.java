package me.squidxtv.frameui.util;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.UUID;

public final class MapUtil {

    private MapUtil() {}

    public static @NotNull ItemStack construct(@NotNull UUID id, @NotNull Collection<Player> viewer, @NotNull World world, int i, int j) {
        MapView view = Bukkit.createMap(world);

        for (MapRenderer renderer : view.getRenderers()) {
            view.removeRenderer(renderer);
        }

        FrameRenderer renderer = new FrameRenderer();
        view.addRenderer(renderer);

        ItemStack map = new ItemStack(Material.FILLED_MAP);

        MapMeta meta = (MapMeta) map.getItemMeta();

        if (meta == null) {
            return map;
        }

        meta.setMapView(view);
        map.setItemMeta(meta);

        for (@NotNull Player player : viewer) {
            player.sendMap(view);
        }

        return map;
    }
}
