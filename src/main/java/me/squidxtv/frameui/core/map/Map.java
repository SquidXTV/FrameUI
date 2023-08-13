package me.squidxtv.frameui.core.map;

import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayDeque;
import java.util.Queue;

public interface Map {

    int WIDTH = 128;
    int HEIGHT = 128;

    void update(@NotNull Color pixel, int x, int y);

    /**
     * A custom map renderer that manages pixel updates and rendering on a MapView.
     */
    class Renderer extends MapRenderer {

        /**
         * The queue of render updates to be applied to the map canvas.
         */
        private final @NotNull Queue<PixelChange> renderUpdates = new ArrayDeque<>();

        /**
         * Initializes the map renderer.
         * @param map The MapView instance to initialize the renderer for.
         */
        @Override
        public void initialize(@NotNull MapView map) {
            map.setTrackingPosition(false);
        }

        /**
         * Renders the map view with the stored pixel updates.
         * @param view The MapView instance to render.
         * @param canvas The MapCanvas to render the pixels on.
         * @param player The player who is viewing the map.
         */
        @Override
        public void render(@NotNull MapView view, @NotNull MapCanvas canvas, @NotNull Player player) {
            while (!renderUpdates.isEmpty()) {
                PixelChange update = renderUpdates.poll();
                canvas.setPixelColor(update.x(), update.y(), update.color());
            }
        }

        /**
         * Sets the color of a pixel at the specified coordinates.
         * @param color The color to set.
         * @param x The x-coordinate of the pixel.
         * @param y The y-coordinate of the pixel.
         */
        public void setPixel(@NotNull Color color, int x, int y) {
            renderUpdates.add(new PixelChange(color, x, y));
        }

        private record PixelChange(@NotNull Color color, int x, int y) { }

    }

}