package me.squidxtv.frameui.core.builder;

import me.squidxtv.frameui.api.ScreenRegistry;
import me.squidxtv.frameui.api.builder.ScreenBuilder;
import me.squidxtv.frameui.core.Renderer;
import me.squidxtv.frameui.core.Screen;
import me.squidxtv.frameui.core.ScreenLocation;
import me.squidxtv.frameui.math.Direction;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.joml.Vector2i;

import java.util.*;

public class ScreenBuilderImpl implements ScreenBuilder {

    private final Plugin plugin;
    private final ScreenRegistry screenRegistry;
    private final Vector2i size;
    private final Set<Player> viewers;
    private ScreenLocation screenLocation;
    private Renderer renderer;
    private String name;

    public ScreenBuilderImpl(Plugin plugin, ScreenRegistry screenRegistry) {
        this.plugin = plugin;
        this.screenRegistry = screenRegistry;
        this.size = new Vector2i(4, 4);
        this.viewers = new HashSet<>();
        this.name = "Screen";
    }

    @Override
    public ScreenBuilder withWidth(int width) {
        this.size.x = width;
        return this;
    }

    @Override
    public ScreenBuilder withHeight(int height) {
        this.size.y = height;
        return this;
    }

    @Override
    public ScreenBuilder withSize(int width, int height) {
        this.size.set(width, height);
        return this;
    }

    @Override
    public ScreenBuilder withName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public ScreenBuilder withViewer(Player player) {
        this.viewers.add(player);

        if (screenLocation != null) {
            return this;
        }
        return withLocation(player.getLocation());
    }

    @Override
    public ScreenBuilder withLocation(Location location) {
        return withLocation(location, Direction.EAST);
    }

    @Override
    public ScreenBuilder withLocation(Location location, Direction direction) {
        this.screenLocation = new ScreenLocation(location, direction);
        return this;
    }

    @Override
    public ScreenBuilder withRenderer(Renderer renderer) {
        this.renderer = renderer;
        return this;
    }

    @Override
    public Screen build() {
        var screen = new Screen(plugin, size.x, size.y, screenLocation);
        screen.setName(name);
        screenRegistry.add(screen);
        return screen;
    }

    @Override
    public Screen buildAndOpen() {
        var screen = build();
        screen.open();
        return screen;
    }
}
