package me.squidxtv.frameui.core;

import me.squidxtv.frameui.core.actions.click.Clickable;
import me.squidxtv.frameui.core.actions.scroll.Scrollable;
import me.squidxtv.frameui.core.content.ScreenModel;
import me.squidxtv.frameui.core.graphics.Graphics;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;


public interface Screen<G extends Graphics<?>> extends Clickable, Scrollable {

    void open();
    void close();
    void remove();
    void update();

    @NotNull G getGraphics();
    void setGraphics(@NotNull G graphics);

    @NotNull JavaPlugin getPlugin();
    @NotNull ScreenModel getModel();
    void setModel(@NotNull ScreenModel model);

}
