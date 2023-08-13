package me.squidxtv.frameui.core.graphics;

import me.squidxtv.frameui.core.map.AbstractMap;
import me.squidxtv.frameui.core.map.Map;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractGraphics<M extends AbstractMap> implements Graphics<M> {

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
}
