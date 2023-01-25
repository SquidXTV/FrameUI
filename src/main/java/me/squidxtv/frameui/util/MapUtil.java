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

/**
 * Utility class for constructing maps.
 */
public final class MapUtil {

    private MapUtil() {}

    /**
     * Constructs new map in given {@link World} and sends map to player.
     * @param viewer player seeing this map
     * @param world world of this map
     * @return created Map
     */
    public static ItemStack construct(Collection<Player> viewer, World world) {
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

        for (Player player : viewer) {
            player.sendMap(view);
        }

        return map;
    }
}
