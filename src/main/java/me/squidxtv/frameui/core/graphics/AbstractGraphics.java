package me.squidxtv.frameui.core.graphics;

import me.squidxtv.frameui.api.FrameAPI;
import me.squidxtv.frameui.core.map.Map;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class AbstractGraphics<M extends Map> implements Graphics<M> {

    private static final @NotNull Logger LOGGER = Bukkit.getServicesManager().load(FrameAPI.class).getLogger();

    private final @NotNull M[] maps;
    private final int width;
    private final int height;
    private final int pixelWidth;
    private final int pixelHeight;

    private @NotNull State state = State.CLOSED;


    protected AbstractGraphics(@NotNull M[] maps, int width, int height) {
        this.maps = maps;
        this.width = width;
        this.height = height;
        this.pixelWidth = this.width * Map.WIDTH;
        this.pixelHeight = this.height * Map.HEIGHT;
    }

    @Override
    public void draw(@NotNull BufferedImage image) {
        checkState();
        draw(image, 0, 0);
    }

    @Override
    public void draw(@NotNull BufferedImage image, int offsetX, int offsetY) {
        checkState();
        draw(image, image.getWidth(), image.getHeight(), offsetX, offsetY);
    }

    @Override
    public void draw(@NotNull BufferedImage image, int width, int height, int offsetX, int offsetY) {
        checkState();
        if (image.getType() == BufferedImage.TYPE_CUSTOM) {
            throw new UnsupportedOperationException("Drawing image of type CUSTOM is not supported.");
        }

        int[] pixels = image.getRGB(0, 0, width, height, null, 0, width);
        draw(pixels, width, height, offsetX, offsetY);
    }

    @Override
    public void drawWithDimensions(@NotNull BufferedImage image, int width, int height) {
        checkState();
        draw(image, width, height, 0, 0);
    }

    @Override
    public void draw(@NotNull Color[] pixels, int width, int height) {
        checkState();
        draw(pixels, width, height, 0, 0);
    }

    @Override
    public void draw(@NotNull Color[] pixels, int width, int height, int offsetX, int offsetY) {
        checkState();
        drawPixels((x, y) -> pixels[x + y * width], offsetX, offsetY, width, height);
    }

    @Override
    public void draw(int[] pixels, int width, int height) {
        checkState();
        draw(pixels, width, height, 0, 0);
    }

    @Override
    public void draw(int[] pixels, int width, int height, int offsetX, int offsetY) {
        checkState();
        drawPixels((x, y) -> new Color(pixels[x + y * width], true), offsetX, offsetY, width, height);
    }

    private void drawPixels(PixelProvider provider, int offsetX, int offsetY, int width, int height) {
        checkState();

        for (int i = 0; i < width; i++) {
            int currentX = offsetX + i;
            if (currentX < 0) {
                LOGGER.log(Level.FINE, "Skipping drawing of column at ({}) - x coordinate out of bounds", currentX);
                continue;
            }
            if (currentX >= this.pixelWidth) {
                LOGGER.log(Level.FINE, "Ending drawing - x coordinate out of bounds");
                return;
            }

            for (int j = 0; j < height; j++) {
                Color color = provider.get(i, j);
                if (color == null) {
                    continue;
                }

                int currentY = offsetY + j;
                if (currentY < 0) {
                    LOGGER.log(Level.FINE, "Skipping drawing of pixel at ({}, {}) - y coordinate out of bounds", new Object[]{currentX, currentY});
                    continue;
                }
                if (currentY >= this.pixelHeight) {
                    LOGGER.log(Level.FINE, "Skipping drawing of column at ({}, {}) - y coordinate out of bounds", new Object[]{currentX, currentY});
                    break;
                }

                int mapX = currentX / Map.WIDTH;
                int mapY = currentY / Map.HEIGHT;
                int mapIndex = mapX + mapY * this.width;

                maps[mapIndex].update(color, currentX - (mapX * Map.WIDTH), currentY - (mapY * Map.HEIGHT));
            }
        }
    }

    private void checkState() {
        if (state == State.CLOSED) {
            throw new IllegalStateException("Graphics currently closed, drawing not allowed.");
        }
        if (state == State.REMOVED) {
            throw new IllegalStateException("Graphics got removed, drawing not allowed.");
        }
    }

    @Override
    public void open() {
        state = State.OPEN;
    }

    @Override
    public void close() {
        state = State.CLOSED;
    }

    @Override
    public void remove() {
        state = State.REMOVED;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public @NotNull M[] getMaps() {
        return maps;
    }

    public @NotNull State getState() {
        return state;
    }

    public int getPixelWidth() {
        return pixelWidth;
    }

    public int getPixelHeight() {
        return pixelHeight;
    }

    public enum State {
        OPEN,
        CLOSED,
        REMOVED
    }

    @FunctionalInterface
    private interface PixelProvider {

        @Nullable Color get(int x, int y);

    }
}
