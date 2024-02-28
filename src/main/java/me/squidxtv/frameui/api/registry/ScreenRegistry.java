package me.squidxtv.frameui.api.registry;

import me.squidxtv.frameui.core.Screen;
import me.squidxtv.frameui.core.VirtualScreen;
import me.squidxtv.frameui.core.graphics.Graphics;
import me.squidxtv.frameui.core.itemframe.ItemFrame;
import me.squidxtv.frameui.core.map.Map;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Stream;

public interface ScreenRegistry {

    void add(@NotNull Screen<?> screen);
    void remove(@NotNull Screen<?> screen);

    @NotNull Stream<Screen<?>> get(@NotNull JavaPlugin plugin);
    @NotNull Stream<VirtualScreen> get(@NotNull JavaPlugin plugin, @NotNull Player viewer);

    @NotNull Stream<VirtualScreen> get(@NotNull Player viewer);
    @NotNull Stream<Screen<?>> get(@NotNull JavaPlugin plugin, @NotNull String id);

    void clear(@NotNull JavaPlugin plugin);

}
