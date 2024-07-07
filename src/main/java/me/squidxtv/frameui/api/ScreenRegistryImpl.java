package me.squidxtv.frameui.api;

import me.squidxtv.frameui.api.builder.ScreenBuilder;
import me.squidxtv.frameui.core.Screen;
import me.squidxtv.frameui.core.builder.ScreenBuilderImpl;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ScreenRegistryImpl implements ScreenRegistry {

    private final List<Screen> registered = new ArrayList<>();

    @Override
    public ScreenBuilder create(Plugin plugin) {
        return new ScreenBuilderImpl(plugin, this);
    }

    @Override
    public void add(Screen screen) {
        registered.add(screen);
    }

    @Override
    public void remove(Screen screen) {
        registered.remove(screen);
    }

    @Override
    public List<Screen> getAll() {
        return registered;
    }

    @Override
    public List<Screen> getByPlugin(Plugin plugin) {
        return registered.stream().filter(screen -> screen.getPlugin().equals(plugin)).toList();
    }

    @Override
    public List<Screen> getByViewer(Player viewer) {
        return registered.stream().filter(screen -> screen.hasViewer(viewer)).toList();
    }

    @Override
    public List<Screen> getByName(String name) {
        return registered.stream().filter(screen -> screen.getName().equals(name)).toList();
    }

    @Override
    public List<Screen> getByViewer(UUID uuid) {
        return registered.stream().filter(screen -> screen.hasViewer(uuid)).toList();
    }
}
