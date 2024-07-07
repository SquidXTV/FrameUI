package me.squidxtv.frameui.core;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapPalette;
import org.bukkit.map.MapView;

import java.awt.Color;

/**
 * The {@code MapItem} represents a custom map item in Minecraft.
 * It stores the displayed pixel data and allows for drawing on the map.
 * Uses {@link MapPalette#matchColor(Color)} to convert a pixel to the limited color palette.
 */
public class MapItem {

    /**
     * The pixel width of a map.
     */
    public static final int WIDTH = 128;

    /**
     * The pixel height of a map.
     */
    public static final int HEIGHT = 128;

    private final int id;
    private final byte[] data = new byte[WIDTH * HEIGHT];
    private final ItemStack item;

    private boolean changed = false;

    /**
     * Creates a new {@link MapItem} in the given World.
     * @param world the world in which the map is created
     * @throws IllegalStateException if the {@link MapMeta} after creation is null
     */
    public MapItem(World world) {
        this.item = new ItemStack(Material.FILLED_MAP);

        MapMeta meta = (MapMeta) item.getItemMeta();
        if (meta == null) {
            throw new IllegalStateException("MapMeta of item can't be null.");
        }

        MapView view = Bukkit.createMap(world);
        view.getRenderers().forEach(view::removeRenderer);
        this.id = view.getId();

        meta.setMapView(view);
        item.setItemMeta(meta);
    }

    /**
     * Draws a pixel on the map at the specified coordinate.
     * @param pixel the color of the pixel to draw
     * @param x the x-coordinate of the pixel
     * @param y the y-coordinate of the pixel
     */
    public void draw(Color pixel, int x, int y) {
        draw(pixel, x + y * WIDTH);
    }

    /**
     * Draws a pixel on the map at the specified index.
     * @param pixel the color of the pixel to draw
     * @param index the index location of the pixel
     */
    public void draw(Color pixel, int index) {
        byte change = MapPalette.matchColor(pixel);
        if (data[index] != change) {
            data[index] = change;
            changed = true;
        }
    }

    /**
     * Draws multiple pixels on the map with an offset location.
     * @param rgb an array of RGB color values
     * @param offset the offset on the data array
     */
    public void draw(int[] rgb, int offset) {
        for (int i = 0; i < rgb.length; i++) {
            draw(new Color(rgb[i]), offset + i);
        }
    }

    /**
     * Draws multiple pixels on the map.
     * @param rgb an array of RGB color values
     */
    public void draw(int[] rgb) {
        draw(rgb, 0);
    }

    /**
     * Returns the id of the map.
     * @return the id of the map
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the map data byte array.
     * @return the byte array representing the map data
     * @implNote The stored bytes are magic values based on {@link MapPalette#matchColor(Color)}.
     */
    public byte[] getData() {
        return data;
    }

    /**
     * Returns the map as an {@link ItemStack}.
     * @return the {@code ItemStack} representing the map
     */
    public ItemStack getAsItemStack() {
        return item;
    }

    /**
     * Checks if the map data has been changed.
     * @return {@code true} if the map data has been changed, {@code false otherwise}
     */
    public boolean isChanged() {
        return changed;
    }

    /**
     * Resets the changed status of the map data.
     */
    public void resetChanged() {
        this.changed = false;
    }

}
