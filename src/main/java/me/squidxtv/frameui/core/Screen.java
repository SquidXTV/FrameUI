package me.squidxtv.frameui.core;

import me.squidxtv.frameui.core.actions.initiator.ActionInitiator;
import me.squidxtv.frameui.core.actions.scroll.ScrollDirection;
import me.squidxtv.frameui.core.content.ScreenModel;
import me.squidxtv.frameui.core.graphics.Graphics;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;


public interface Screen<G extends Graphics<?>> {

    void open();
    void close();
    void remove();
    void update();

    @NotNull G getGraphics();
    void setGraphics(@NotNull G graphics);

    boolean click(@NotNull ActionInitiator<?> initiator, int x, int y);
    boolean scroll(@NotNull ActionInitiator<?> initiator, @NotNull ScrollDirection direction, int x, int y);

    @NotNull JavaPlugin getPlugin();
    @NotNull ScreenModel getModel();
    void setModel(@NotNull ScreenModel model);

    enum State {
        OPEN,
        CLOSED,
        REMOVED
    }

}
