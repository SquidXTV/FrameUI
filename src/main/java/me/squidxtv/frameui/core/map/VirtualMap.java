package me.squidxtv.frameui.core.map;

import me.squidxtv.frameui.util.protocol.WrapperPlayServerMapData;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapPalette;
import org.bukkit.map.MapView;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class VirtualMap implements Map {

    private final byte[] data = new byte[WIDTH * HEIGHT];
    private boolean changed = false;

    private final int id;
    private final ItemStack item;

    public VirtualMap(@NotNull World world) {
        this.item = new ItemStack(Material.FILLED_MAP);

        MapMeta meta = ((MapMeta) item.getItemMeta());
        if (meta == null) {
            throw new IllegalStateException("MapMeta of newly created FILLED_MAP ItemStack is null.");
        }

        MapView view;
        if (meta.hasMapView()) {
            view = meta.getMapView();
        } else {
            view = Bukkit.createMap(world);
            meta.setMapView(view);
            item.setItemMeta(meta);
        }

        if (view == null) {
            throw new IllegalStateException("MapView of newly created Map is null.");
        }

        view.getRenderers().forEach(view::removeRenderer);
        this.id = view.getId();
    }

    @Override
    @SuppressWarnings("deprecation")
    public void update(@NotNull Color pixel, int x, int y) {
        // ToDo: store furthest column updated
        int index = x + y * WIDTH;
        byte change = MapPalette.matchColor(pixel);
        if (data[index] != change) {
            data[index] = change;
            this.changed = true;
        }
    }

    @Override
    public @NotNull ItemStack getAsItemStack() {
        return this.item;
    }

    public boolean isChanged() {
        return changed;
    }

    public int getId() {
        return id;
    }

    public WrapperPlayServerMapData getDataPacket() {
        return new WrapperPlayServerMapData(id, 0, false, null, data, 0, 0, WIDTH, HEIGHT);
    }

}
