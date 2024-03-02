package me.squidxtv.visual.screen;

import me.squidxtv.frameui.core.Screen;
import me.squidxtv.frameui.core.actions.initiator.ActionInitiator;
import me.squidxtv.frameui.core.actions.scroll.ScrollDirection;
import me.squidxtv.frameui.core.content.ScreenModel;
import me.squidxtv.frameui.core.math.BoundingBox;
import me.squidxtv.frameui.exceptions.ScreenRemovedException;
import org.bukkit.World;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class DebugScreen implements Screen<DebugGraphics> {

    private @NotNull ScreenModel model;
    private @NotNull DebugGraphics graphics;
    private @NotNull State state = State.CLOSED;

    public DebugScreen(@NotNull ScreenModel model) {
        this.model = model;
        graphics = new DebugGraphics(model);
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
    public void terminate() {
        throwIfRemoved();
        close();
        graphics.terminate();
        state = State.TERMINATED;
    }

    @Override
    public void update() {
        throwIfRemoved();
        model.draw(graphics, new BoundingBox(0, 0, model.getWidth(), model.getHeight()));
    }

    protected void throwIfRemoved() {
        if (state == State.TERMINATED) {
            throw new ScreenRemovedException(this);
        }
    }

    public enum State {
        OPEN,
        CLOSED,
        TERMINATED
    }

    @Override
    public boolean click(@NotNull ActionInitiator initiator, int x, int y) {
        model.click(initiator, x, y, new BoundingBox(0, 0, model.getWidth(), model.getHeight()));
        return true;
    }

    @Override
    public boolean scroll(@NotNull ActionInitiator initiator, @NotNull ScrollDirection direction, int x, int y) {
        model.scroll(initiator, direction, x, y, new BoundingBox(0, 0, model.getWidth(), model.getHeight()));
        return true;
    }

    @Override
    public @NotNull Optional<Permission> getClickPermission() {
        return Optional.empty();
    }

    @Override
    public @NotNull Optional<Permission> getScrollPermission() {
        return Optional.empty();
    }

    @Override
    public void setClickPermission(@Nullable Permission clickPermission) {
    }

    @Override
    public void setScrollPermission(@Nullable Permission scrollPermission) {
    }

    @Override
    public @NotNull World getWorld() {
        throw new UnsupportedOperationException("World is not existing in Debug environment.");
    }

    @Override
    public void setWorld(@NotNull World world) {
        throw new UnsupportedOperationException("World is not existing in Debug environment.");
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
        throw new UnsupportedOperationException("Plugin is not existing in Debug environment.");
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
