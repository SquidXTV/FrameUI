package me.squidxtv.frameui.core.map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractMap implements Map {

    protected final @NotNull ItemStack item;
    protected final @NotNull Renderer renderer;

    protected AbstractMap(@NotNull World world) {
        this.renderer = new Renderer();

        MapView view = Bukkit.createMap(world);
        view.getRenderers().forEach(view::removeRenderer);

        view.addRenderer(renderer);

        item = new ItemStack(Material.FILLED_MAP);

        MapMeta meta = (MapMeta) item.getItemMeta();

        if (meta != null) {
            meta.setMapView(view);
            item.setItemMeta(meta);
        }
    }
}
