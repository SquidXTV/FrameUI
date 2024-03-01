package me.squidxtv.frameui.core.graphics;

import me.squidxtv.frameui.api.FrameAPI;
import me.squidxtv.frameui.core.content.ScreenModel;
import me.squidxtv.frameui.core.itemframe.ItemFrame;
import me.squidxtv.frameui.core.map.Map;
import me.squidxtv.frameui.core.math.BoundingBox;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class AbstractGraphics<I extends ItemFrame<?>> implements Graphics<I> {

    private static final @NotNull Logger LOGGER = Objects.requireNonNull(Bukkit.getServicesManager().load(FrameAPI.class)).getLogger();

    private final @NotNull ScreenModel model;
    private final @NotNull I[] frames;
    private final int width;
    private final int height;
    private final int pixelWidth;
    private final int pixelHeight;

    private @NotNull State state = State.CLOSED;


    protected AbstractGraphics(@NotNull ScreenModel model, @NotNull I[] frames, int width, int height) {
        this.model = model;
        this.frames = frames;
        this.width = width;
        this.height = height;
        this.pixelWidth = this.width * Map.WIDTH;
        this.pixelHeight = this.height * Map.HEIGHT;
    }

    @Override
    public void update() {
        model.draw(this, new BoundingBox(0, 0, pixelWidth, pixelHeight));
    }

    @Override
    public void draw(@NotNull BufferedImage image) {
        draw(image, 0, 0);
    }

    @Override
    public void draw(@NotNull BufferedImage image, int offsetX, int offsetY) {
        draw(image, image.getWidth(), image.getHeight(), offsetX, offsetY);
    }

    @Override
    public void draw(@NotNull BufferedImage image, int width, int height, int offsetX, int offsetY) {
        if (image.getType() == BufferedImage.TYPE_CUSTOM) {
            throw new UnsupportedOperationException("Drawing image of type CUSTOM is not supported.");
        }

        int[] pixels = image.getRGB(0, 0, width, height, null, 0, width);
        draw(pixels, width, height, offsetX, offsetY);
    }

    @Override
    public void drawWithDimensions(@NotNull BufferedImage image, int width, int height) {
        draw(image, width, height, 0, 0);
    }

    @Override
    public void draw(@NotNull Color[] pixels, int width, int height) {
        draw(pixels, width, height, 0, 0);
    }

    @Override
    public void draw(@NotNull Color[] pixels, int width, int height, int offsetX, int offsetY) {
        drawPixels((x, y) -> pixels[x + y * width], offsetX, offsetY, width, height);
    }

    @Override
    public void draw(int[] pixels, int width, int height) {
        draw(pixels, width, height, 0, 0);
    }

    @Override
    public void draw(int[] pixels, int width, int height, int offsetX, int offsetY) {
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

                int frameX = currentX / Map.WIDTH;
                int frameY = currentY / Map.HEIGHT;
                int frameIndex = frameX + frameY * this.width;

                int pixelX = currentX - (frameX * Map.WIDTH);
                int pixelY = currentY - (frameY * Map.HEIGHT);
                frames[frameIndex].getMap().update(color, pixelX, pixelY);
            }
        }
    }

    private void checkState() {
        if (state == State.CLOSED) {
            throw new IllegalStateException("Graphics currently closed, drawing not allowed.");
        }
        if (state == State.TERMINATED) {
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
    public void terminate() {
        state = State.TERMINATED;
    }

    @Override
    public @NotNull I[] getItemFrames() {
        return frames;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public int getPixelWidth() {
        return pixelWidth;
    }

    @Override
    public int getPixelHeight() {
        return pixelHeight;
    }

    public @NotNull State getState() {
        return state;
    }

    protected void setState(@NotNull State state) {
        this.state = state;
    }

    public enum State {
        OPEN,
        CLOSED,
        TERMINATED
    }

    @FunctionalInterface
    private interface PixelProvider {

        @Nullable Color get(int x, int y);

    }

}
