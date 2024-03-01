package me.squidxtv.frameui.core;

import me.squidxtv.frameui.core.actions.initiator.ActionInitiator;
import me.squidxtv.frameui.core.actions.scroll.ScrollDirection;
import me.squidxtv.frameui.core.content.ScreenModel;
import me.squidxtv.frameui.core.graphics.Graphics;
import org.bukkit.World;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;


public interface Screen<G extends Graphics<?>> {

    void open();

    void close();

    void terminate();

    void update();

    @NotNull G getGraphics();

    void setGraphics(@NotNull G graphics);

    boolean click(@NotNull ActionInitiator initiator, int x, int y);

    boolean scroll(@NotNull ActionInitiator initiator, @NotNull ScrollDirection direction, int x, int y);

    @NotNull Optional<Permission> getClickPermission();

    @NotNull Optional<Permission> getScrollPermission();

    void setClickPermission(@Nullable Permission clickPermission);

    void setScrollPermission(@Nullable Permission scrollPermission);

    @NotNull World getWorld();

    void setWorld(@NotNull World world);

    @NotNull JavaPlugin getPlugin();

    @NotNull ScreenModel getModel();

    void setModel(@NotNull ScreenModel model);

    enum State {
        OPEN,
        CLOSED,
        TERMINATED
    }

}
