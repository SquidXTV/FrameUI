package me.squidxtv.frameui.core;

import me.squidxtv.frameui.api.registry.ScreenRegistry;
import me.squidxtv.frameui.core.actions.initiator.ActionInitiator;
import me.squidxtv.frameui.core.actions.initiator.PlayerInitiator;
import me.squidxtv.frameui.core.actions.scroll.ScrollDirection;
import me.squidxtv.frameui.core.content.ScreenModel;
import me.squidxtv.frameui.core.graphics.Graphics;
import me.squidxtv.frameui.exceptions.ScreenRemovedException;
import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;

public abstract class AbstractScreen<G extends Graphics<?>> implements Screen<G> {

    protected static final ScreenRegistry SCREEN_REGISTRY = Objects.requireNonNull(Bukkit.getServicesManager().load(ScreenRegistry.class));

    private final @NotNull JavaPlugin plugin;
    private @NotNull ScreenModel model;
    private @NotNull G graphics;
    private @Nullable Permission clickPermission = null;
    private @Nullable Permission scrollPermission = null;
    private @NotNull State state = State.CLOSED;

    protected AbstractScreen(@NotNull JavaPlugin plugin, @NotNull ScreenModel model, @NotNull G graphics) {
        this.plugin = plugin;
        this.model = model;
        this.graphics = graphics;
        SCREEN_REGISTRY.add(this);
    }

    @Override
    public void open() {
        throwIfTerminated();

        if (state == State.OPEN) {
            close();
        }

        graphics.open();
        state = State.OPEN;
    }

    @Override
    public void close() {
        throwIfTerminated();

        if (state == State.CLOSED) {
            return;
        }

        graphics.close();
        state = State.CLOSED;
    }

    @Override
    public void terminate() {
        throwIfTerminated();
        close();
        graphics.terminate();

        SCREEN_REGISTRY.remove(this);
        state = State.TERMINATED;
    }

    @Override
    public void update() {
        throwIfTerminated();
        graphics.update();
    }

    @Override
    public @NotNull G getGraphics() {
        return graphics;
    }

    @Override
    public void setGraphics(@NotNull G graphics) {
        this.graphics = graphics;
    }

    @Override
    public boolean click(@NotNull ActionInitiator initiator, int x, int y) {
        throwIfTerminated();
        if (state == State.CLOSED) {
            return false;
        }

        if (clickPermission == null) {
            return true;
        }

        if (initiator instanceof PlayerInitiator playerInitiator) {
            return playerInitiator.player().hasPermission(clickPermission);
        }

        return true;
    }

    @Override
    public boolean scroll(@NotNull ActionInitiator initiator, @NotNull ScrollDirection direction, int x, int y) {
        throwIfTerminated();
        if (state == State.CLOSED) {
            return false;
        }

        if (scrollPermission == null) {
            return true;
        }

        if (initiator instanceof PlayerInitiator playerInitiator) {
            return playerInitiator.player().hasPermission(scrollPermission);
        }

        return true;
    }

    protected void throwIfTerminated() {
        if (state == State.TERMINATED) {
            throw new ScreenRemovedException(this);
        }
    }

    @Override
    public @NotNull JavaPlugin getPlugin() {
        return plugin;
    }

    @Override
    public @NotNull ScreenModel getModel() {
        return model;
    }

    @Override
    public void setModel(@NotNull ScreenModel model) {
        if (state == State.OPEN) {
            close();
        }
        this.model = model;
    }

    @Override
    public @NotNull Optional<Permission> getClickPermission() {
        return Optional.ofNullable(clickPermission);
    }

    @Override
    public @NotNull Optional<Permission> getScrollPermission() {
        return Optional.ofNullable(scrollPermission);
    }

    @Override
    public void setClickPermission(@Nullable Permission clickPermission) {
        this.clickPermission = clickPermission;
    }

    @Override
    public void setScrollPermission(@Nullable Permission scrollPermission) {
        this.scrollPermission = scrollPermission;
    }

    public @NotNull State getState() {
        return state;
    }

    protected void setState(@NotNull State state) {
        this.state = state;
    }

}
