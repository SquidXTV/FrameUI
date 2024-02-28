package me.squidxtv.frameui.api.registry;

import me.squidxtv.frameui.core.Screen;
import me.squidxtv.frameui.core.VirtualScreen;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ScreenRegistryImpl implements ScreenRegistry {

    private final @NotNull List<Screen<?>> registered = new ArrayList<>();

    @Override
    public void add(@NotNull Screen<?> screen) {
        registered.add(screen);
    }

    @Override
    public void remove(@NotNull Screen<?> screen) {
        registered.remove(screen);
    }

    @Override
    public @NotNull Stream<Screen<?>> get(@NotNull JavaPlugin plugin) {
        return registered.stream()
                .filter(screen -> screen.getPlugin().equals(plugin));
    }

    @Override
    public @NotNull Stream<VirtualScreen> get(@NotNull JavaPlugin plugin, @NotNull Player viewer) {
        return get(plugin)
                .filter(screen -> screen instanceof VirtualScreen virtualScreen && virtualScreen.containsViewer(viewer))
                .map(VirtualScreen.class::cast);
    }

    @Override
    public @NotNull Stream<VirtualScreen> get(@NotNull Player viewer) {
        return registered.stream()
                .filter(screen -> screen instanceof VirtualScreen virtualScreen && virtualScreen.containsViewer(viewer))
                .map(VirtualScreen.class::cast);
    }

    @Override
    public @NotNull Stream<Screen<?>> get(@NotNull JavaPlugin plugin, @NotNull String id) {
        return get(plugin).filter(screen -> screen.getModel().getId().equals(id));
    }

    // ToDo: might be removing while iterating
    @Override
    public void clear(@NotNull JavaPlugin plugin) {
        get(plugin).forEach(Screen::terminate);
    }

}
