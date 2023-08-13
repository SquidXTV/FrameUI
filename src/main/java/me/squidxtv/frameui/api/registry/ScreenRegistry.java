package me.squidxtv.frameui.api.registry;

import me.squidxtv.frameui.core.Screen;
import me.squidxtv.frameui.core.VirtualScreen;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface ScreenRegistry {

    void add(@NotNull Screen<?> screen);
    void remove(@NotNull Screen<?> screen);

    @NotNull List<Screen<?>> get(@NotNull JavaPlugin plugin);
    @NotNull List<VirtualScreen> get(@NotNull JavaPlugin plugin, @NotNull Player viewer);

    @NotNull List<VirtualScreen> get(@NotNull Player viewer);
    @NotNull List<Screen<?>> get(@NotNull JavaPlugin plugin, @NotNull String id);

    void clear(@NotNull JavaPlugin plugin);

}
