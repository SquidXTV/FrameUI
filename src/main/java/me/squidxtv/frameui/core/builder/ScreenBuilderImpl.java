package me.squidxtv.frameui.core.builder;

import me.squidxtv.frameui.api.ScreenRegistry;
import me.squidxtv.frameui.api.builder.ScreenBuilder;
import me.squidxtv.frameui.api.data.ScreenProperties;
import me.squidxtv.frameui.core.Renderer;
import me.squidxtv.frameui.core.Screen;
import me.squidxtv.frameui.core.ScreenLocation;
import me.squidxtv.frameui.core.ScreenSpawner;
import me.squidxtv.frameui.core.impl.EmptyRenderer;
import me.squidxtv.frameui.core.impl.PacketScreenSpawner;
import me.squidxtv.frameui.math.Direction;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.joml.Vector2i;

import java.util.*;

public class ScreenBuilderImpl implements ScreenBuilder {

    private final Plugin plugin;
    private final ScreenRegistry screenRegistry;
    private final Set<Player> viewers;
    private final ScreenProperties screenProperties;
    private Renderer renderer;
    private ScreenSpawner spawner;

    public ScreenBuilderImpl(Plugin plugin, ScreenRegistry screenRegistry) {
        this.plugin = plugin;
        this.screenRegistry = screenRegistry;
        this.viewers = new HashSet<>();
        this.screenProperties = getDefaultScreenProperties();
        this.spawner = new PacketScreenSpawner();
        this.renderer = new EmptyRenderer();
    }

    @Override
    public ScreenBuilder withWidth(int width) {
        screenProperties.getSize().x = width;
        return this;
    }

    @Override
    public ScreenBuilder withHeight(int height) {
        screenProperties.getSize().y = height;
        return this;
    }

    @Override
    public ScreenBuilder withSize(int width, int height) {
        screenProperties.getSize().set(width, height);
        return this;
    }

    @Override
    public ScreenBuilder withName(String name) {
        screenProperties.setName(name);
        return this;
    }

    @Override
    public ScreenBuilder withClickRadius(double radios) {
        screenProperties.setClickRadius(radios);
        return this;
    }

    @Override
    public ScreenBuilder withScrollRadius(double scrollRadios) {
        screenProperties.setScrollRadius(scrollRadios);
        return this;
    }

    @Override
    public ScreenBuilder withViewer(Player player) {
        this.viewers.add(player);
        if (screenProperties.getScreenLocation() != null) {
            return this;
        }
        return withLocation(player.getLocation());
    }

    @Override
    public ScreenBuilder withViewer(Player... players) {
        for (var player : players) {
            withViewer(player);
        }
        return this;
    }

    @Override
    public ScreenBuilder withViewer(Collection<Player> players) {
        players.forEach(this::withViewer);
        return this;
    }

    @Override
    public ScreenBuilder withLocation(Location location) {
        return withLocation(location, Direction.EAST);
    }

    @Override
    public ScreenBuilder withLocation(Location location, Direction direction) {
        screenProperties.setScreenLocation(new ScreenLocation(location, direction));
        return this;
    }

    @Override
    public ScreenBuilder withRenderer(Renderer renderer) {
        this.renderer = renderer;
        return this;
    }

    @Override
    public ScreenBuilder withSpawner(ScreenSpawner spawner) {
        this.spawner = spawner;
        return null;
    }

    @Override
    public Screen build() {
        var screen = new Screen(plugin, screenProperties, spawner, renderer);
        screen.addViewer(viewers);
        screenRegistry.add(screen);
        return screen;
    }

    @Override
    public Screen buildAndOpen() {
        var screen = build();
        screen.open();
        return screen;
    }


    private ScreenProperties getDefaultScreenProperties() {
        var properties = new ScreenProperties();

        properties.setSize(new Vector2i(3, 3));
        properties.setName(UUID.randomUUID().toString());
        properties.setState(Screen.State.CLOSED);
        properties.setClickRadius(20);
        properties.setScrollRadius(20);

        return properties;
    }
}
