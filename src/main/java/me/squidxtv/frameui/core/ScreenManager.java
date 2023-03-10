package me.squidxtv.frameui.core;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public final class ScreenManager {

    private static ScreenManager instance;

    private final @NotNull Map<JavaPlugin, List<Screen>> registeredScreens;

    private ScreenManager() {
        registeredScreens = new HashMap<>();
    }

    public @NotNull List<Screen> getAll() {
        return registeredScreens.values().stream().flatMap(Collection::stream).toList();
    }

    public @NotNull List<Screen> getByPlugin(@NotNull JavaPlugin plugin) {
        return registeredScreens.getOrDefault(plugin, new ArrayList<>());
    }

    public void register(@NotNull JavaPlugin plugin, @NotNull Screen screen) {
        registeredScreens.computeIfAbsent(plugin, k -> new ArrayList<>()).add(screen);
    }

    public void unregister(@NotNull JavaPlugin plugin, @NotNull Screen screen) {
        registeredScreens.computeIfAbsent(plugin, k -> new ArrayList<>()).remove(screen);
    }

    public void clear(@NotNull JavaPlugin plugin) {
        registeredScreens.getOrDefault(plugin, new ArrayList<>()).clear();
    }

    public @NotNull List<Screen> getByPlayer(@NotNull Player player) {
        return registeredScreens.values().stream().flatMap(Collection::stream).filter(screen -> screen.containsViewer(player)).toList();
    }

    public @NotNull List<Screen> getById(@NotNull String id) {
        return registeredScreens.values().stream().flatMap(Collection::stream).filter(screen -> screen.getId().equals(id)).toList();
    }

    public static ScreenManager getInstance() {
        if (instance == null) {
            instance = new ScreenManager();
        }

        return instance;
    }
}
