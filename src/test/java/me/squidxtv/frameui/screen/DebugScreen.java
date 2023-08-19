package me.squidxtv.frameui.screen;

import me.squidxtv.frameui.core.Screen;
import me.squidxtv.frameui.core.actions.initiator.ActionInitiator;
import me.squidxtv.frameui.core.content.ScreenModel;
import me.squidxtv.frameui.exceptions.ScreenRemovedException;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class DebugScreen implements Screen<DebugGraphics> {

    private @NotNull ScreenModel model;
    private @NotNull DebugGraphics graphics;
    private @NotNull State state = State.CLOSED;

    public DebugScreen(ScreenModel model) {
        this.model = model;
        graphics = new DebugGraphics(model.getWidth(), model.getHeight());
    }

    @Override
    public void open() {
        throwIfRemoved();
        if (state == State.OPEN) {
            close();
        }

        graphics.open();
        state = State.OPEN;
    }

    @Override
    public void close() {
        throwIfRemoved();
        if (state == State.CLOSED) {
            return;
        }

        graphics.close();
        state = State.CLOSED;
    }

    @Override
    public void remove() {
        throwIfRemoved();
        close();
        graphics.remove();
        state = State.REMOVED;
    }

    @Override
    public void update() {
        throwIfRemoved();
        model.draw(graphics, 0, 0, graphics.getPixelWidth(), graphics.getPixelHeight());
    }

    protected void throwIfRemoved() {
        if (state == State.REMOVED) {
            throw new ScreenRemovedException(this);
        }
    }

    public enum State {
        OPEN,
        CLOSED,
        REMOVED
    }

    @Override
    public boolean click(@NotNull ActionInitiator<?> initiator, int x, int y) {
        return model.click(initiator, x, y);
    }

    @Override
    public boolean scroll(@NotNull ActionInitiator<?> initiator, @NotNull ScrollDirection direction, int x, int y) {
        return model.scroll(initiator, direction, x, y);
    }


    @Override
    public @NotNull DebugGraphics getGraphics() {
        return graphics;
    }

    @Override
    public void setGraphics(@NotNull DebugGraphics graphics) {
        this.graphics = graphics;
    }

    @Override
    public @NotNull JavaPlugin getPlugin() {
        return null;
    }

    @Override
    public @NotNull ScreenModel getModel() {
        return model;
    }

    @Override
    public void setModel(@NotNull ScreenModel model) {
        this.model = model;
    }

}
