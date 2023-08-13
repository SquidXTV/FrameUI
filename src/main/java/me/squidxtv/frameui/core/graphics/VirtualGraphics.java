package me.squidxtv.frameui.core.graphics;

import me.squidxtv.frameui.api.FrameAPI;
import me.squidxtv.frameui.core.map.Map;
import me.squidxtv.frameui.core.map.VirtualMap;
import me.squidxtv.frameui.core.math.Direction;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VirtualGraphics extends AbstractGraphics<VirtualMap> {

    private static final @NotNull Logger LOGGER = Bukkit.getServicesManager().load(FrameAPI.class).getLogger();

    private @NotNull World world;
    private @NotNull Location location;
    private @NotNull Direction direction;

    public VirtualGraphics(@NotNull World world, @NotNull Location location, @NotNull Direction direction, int width, int height) {
        super(new VirtualMap[width * height], width, height);
        this.world = world;
        this.location = location;
        this.direction = direction;
    }

    @Override
    public void open() {
        super.open();
        initializeMaps();
    }

    @Override
    public void close() {
        super.close();
        closeMaps();
    }

    @Override
    public void remove() {
        super.remove();
        closeMaps();
    }

    private void initializeMaps() {
        VirtualMap[] maps = getMaps();
        for (int i = 0; i < getWidth(); i++) {
            for (int j = 0; j < getHeight(); j++) {
                int x = location.getBlockX() + (i * direction.getMultiplierX());
                int y = location.getBlockY() - j;
                int z = location.getBlockZ() + (i * direction.getMultiplierZ());

                int index = i + j * getWidth();
                maps[index] = new VirtualMap(world, new Location(world, x, y, z), direction);
            }
        }
    }

    private void closeMaps() {
        VirtualMap[] maps = getMaps();
        Arrays.fill(maps, null);
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
        int pixelWidth = getPixelWidth();
        int pixelHeight = getPixelHeight();
        VirtualMap[] maps = getMaps();
        int mapWidth = getWidth();

        for (int i = 0; i < width; i++) {
            int currentX = offsetX + i;
            if (currentX < 0) {
                LOGGER.log(Level.FINE, "Skipping drawing of column at ({}) - x coordinate out of bounds", currentX);
                continue;
            }
            if (currentX >= pixelWidth) {
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
                if (currentY >= pixelHeight) {
                    LOGGER.log(Level.FINE, "Skipping drawing of column at ({}, {}) - y coordinate out of bounds", new Object[]{currentX, currentY});
                    break;
                }

                int mapX = currentX / Map.WIDTH;
                int mapY = currentY / Map.HEIGHT;
                int mapIndex = mapX + mapY * mapWidth;

                maps[mapIndex].update(color, currentX - (mapX * Map.WIDTH), currentY - (mapY * Map.HEIGHT));
            }
        }
    }

    private void checkState() {
        State state = getState();
        if (state == State.CLOSED) {
            throw new IllegalStateException("Graphics currently closed, drawing not allowed.");
        }
        if (state == State.REMOVED) {
            throw new IllegalStateException("Graphics got removed, drawing not allowed.");
        }
    }

    @FunctionalInterface
    private interface PixelProvider {

        @Nullable Color get(int x, int y);

    }

    public @NotNull World getWorld() {
        return world;
    }

    public void setWorld(@NotNull World world) {
        this.world = world;
    }

    public @NotNull Location getLocation() {
        return location;
    }

    public void setLocation(@NotNull Location location) {
        this.location = location;
    }

    public @NotNull Direction getDirection() {
        return direction;
    }

    public void setDirection(@NotNull Direction direction) {
        this.direction = direction;
    }
}
