package me.squidxtv.frameui.core;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import me.squidxtv.frameui.api.ScreenRegistry;
import me.squidxtv.frameui.core.action.click.ClickEventHandler;
import me.squidxtv.frameui.core.action.click.Clickable;
import me.squidxtv.frameui.core.action.scroll.ScrollEventHandler;
import me.squidxtv.frameui.core.action.scroll.Scrollable;
import me.squidxtv.frameui.core.impl.BufferedImageRenderer;
import me.squidxtv.frameui.core.impl.PacketScreenSpawner;
import me.squidxtv.frameui.math.Direction;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * The {@code Screen} is a 2d plane of multiple {@link ItemFrame} instances in
 * the Minecraft World. Together they make up a bigger image. Each
 * {@code ItemFrame} hold a {@link MapItem} that display a custom image which is
 * limited to 128x128 pixels.
 */
public class Screen implements Clickable, Scrollable {

    private static final ScreenRegistry REGISTRY = Objects.requireNonNull(Bukkit.getServicesManager().load(ScreenRegistry.class),
            "Couldn't properly load ScreenRegistry from the ServicesManager.");

    private final Plugin plugin;
    private final Set<UUID> viewers = new HashSet<>();

    private final ItemFrame[][] itemFrames;

    private final int width;
    private final int height;
    private final ScreenLocation location;

    private final Renderer renderer;
    private final ScreenSpawner spawner;

    private ClickEventHandler clickEventHandler = ClickEventHandler.empty();
    private ScrollEventHandler scrollEventHandler = ScrollEventHandler.empty();

    private double clickRadius = 15;
    private double scrollRadius = 15;

    private State state = State.CLOSED;

    /**
     * The {@code State} represents the possible states of a screen.
     */
    public enum State {
        /**
         * Indicates that the {@link Screen} is open and displayed to the viewers.
         */
        OPEN,
        /**
         * Indicates that the {@link Screen} is closed and currently not visible.
         */
        CLOSED
    }

    /**
     * Creates a new screen with the specified plugin, dimensions, location, spawner
     * and renderer.
     *
     * @param plugin   the plugin associated with this screen
     * @param width    the block width of the screen
     * @param height   the block height of the screen
     * @param location the location of the screen
     * @param spawner  the spawner responsible for spawning the screen's item frames
     * @param renderer the renderer responsible for rendering the screen's content
     * @throws NullPointerException if {@code plugin}, {@code location},
     *                              {@code spawner}, or {@code renderer} is null
     */
    public Screen(Plugin plugin, int width, int height, ScreenLocation location, ScreenSpawner spawner, Renderer renderer) {
        Objects.requireNonNull(plugin, "Plugin cannot be null");
        Objects.requireNonNull(location, "Location cannot be null");
        Objects.requireNonNull(spawner, "Spawner cannot be null");
        Objects.requireNonNull(renderer, "Renderer cannot be null");
        this.plugin = plugin;
        this.itemFrames = new ItemFrame[height][width];

        this.width = width;
        this.height = height;
        this.location = location;

        this.spawner = spawner;
        this.renderer = renderer;
    }

    /**
     * Creates a new screen with the specified plugin, dimensions and location. Uses
     * packets for spawning and a {@link BufferedImage} for rendering.
     *
     * @param plugin   the plugin associated with this screen
     * @param width    the block width of the screen
     * @param height   the block height of the screen
     * @param location the location of the screen
     * @param image    the image rendered on the image
     * @throws NullPointerException if {@code plugin}, {@code location} or
     *                              {@code image} is null
     */
    public Screen(Plugin plugin, int width, int height, ScreenLocation location, BufferedImage image) {
        this(plugin, width, height, location, new PacketScreenSpawner(), new BufferedImageRenderer(image));
    }

    /**
     * Creates a new screen with the specified plugin, dimensions and location. Uses
     * packets for spawning and a default image for rendering.
     *
     * @param plugin   the plugin associated with this screen
     * @param width    the block width of the screen
     * @param height   the block height of the screen
     * @param location the location of the screen
     * @throws NullPointerException if {@code plugin} or {@code location} is null
     */
    public Screen(Plugin plugin, int width, int height, ScreenLocation location) {
        this(plugin, width, height, location, getDefaultImage(width, height));
    }

    /**
     * Opens the screen with <strong>invisible</strong> item frames.
     */
    public void open() {
        open(true);
    }

    /**
     * Opens the screen.
     * 
     * @param invisible sets the visibility of the item frames
     */
    public void open(boolean invisible) {
        if (state == State.OPEN) {
            close();
        }

        REGISTRY.add(this);
        initializeItemFrames(invisible);
        spawner.spawn(this);
        state = State.OPEN;
    }

    /**
     * Updates the screen.
     */
    public void update() {
        if (state == State.CLOSED) {
            return;
        }
        renderer.update(this);
        spawner.update(this);
    }

    /**
     * Closes the screen.
     */
    public void close() {
        if (state == State.CLOSED) {
            return;
        }

        REGISTRY.remove(this);
        spawner.despawn(this);
        for (ItemFrame[] row : itemFrames) {
            Arrays.fill(row, null);
        }
        state = State.CLOSED;
    }

    /**
     * Adds a player to the screen's viewers.
     * 
     * @param player the player to add
     * @return {@code true} if player was added based on {@link Set#add(Object)},
     *         {@code false} otherwise
     * @throws NullPointerException if {@code player} is null
     */
    public boolean addViewer(Player player) {
        Objects.requireNonNull(player, "Player cannot be null");
        boolean added = viewers.add(player.getUniqueId());

        if (added && state == State.OPEN) {
            spawner.spawn(this, player);
        }

        return added;
    }

    /**
     * Adds a collection of players to the screen's viewers.
     * 
     * @param players the players to add
     * @return the set including the players that actually got added based on
     *         {@link Set#add(Object)}
     * @throws NullPointerException if {@code players} is null
     */
    public Set<Player> addViewer(Collection<Player> players) {
        Objects.requireNonNull(players, "Collection of players cannot be null");
        Set<Player> added = new HashSet<>();
        for (Player player : players) {
            if (viewers.add(player.getUniqueId())) {
                added.add(player);
            }
        }

        spawner.spawn(this, added);
        return added;
    }

    /**
     * Removes a player form the screen's viewers.
     * 
     * @param player the player to remove
     * @return {@code true} if the player was removed, {@code false} otherwise
     * @throws NullPointerException if {@code player} is null
     */
    public boolean removeViewer(Player player) {
        Objects.requireNonNull(player, "Player cannot be null");
        boolean removed = viewers.remove(player.getUniqueId());

        if (removed && state == State.OPEN) {
            spawner.despawn(this, player);
        }

        return removed;
    }

    /**
     * Removes a collection of players from the screen's viewers.
     * 
     * @param players the players to remove
     * @return the set including the players that actually got removed based on
     *         {@link Set#remove(Object)}
     * @throws NullPointerException if {@code players} is null
     */
    public Set<Player> removeViewer(Collection<Player> players) {
        Objects.requireNonNull(players, "Collection of players cannot be null");
        Set<Player> removed = new HashSet<>();
        for (Player player : players) {
            if (viewers.remove(player.getUniqueId())) {
                removed.add(player);
            }
        }

        spawner.despawn(this, removed);
        return removed;
    }

    /**
     * Clears all viewers from the screen.
     */
    public void clearViewer() {
        spawner.despawn(this);
        viewers.clear();
    }

    /**
     * Checks if a player is a viewer of the screen.
     * 
     * @param player the player to check
     * @return {@code true} if the player is a viewer of this screen, {@code false}
     *         otherwise
     * @throws NullPointerException if {@code player} is null
     */
    public boolean hasViewer(Player player) {
        Objects.requireNonNull(player, "Player cannot be null");
        return viewers.contains(player.getUniqueId());
    }

    /**
     * Checks if a player is a viewer of the screen.
     * 
     * @param uuid uuid to check
     * @return {@code true} if the player is a viewer of this screen, {@code false}
     *         otherwise
     * @throws NullPointerException if {@code uuid} is null
     */
    public boolean hasViewer(UUID uuid) {
        Objects.requireNonNull(uuid, "UUID cannot be null");
        return viewers.contains(uuid);
    }

    /**
     * Returns the plugin associated with this screen.
     * 
     * @return the plugin associated with this screen
     */
    public Plugin getPlugin() {
        return plugin;
    }

    /**
     * Returns the viewers of the screen.
     * 
     * @return a copy of the viewers
     */
    public Set<UUID> getViewers() {
        return Set.copyOf(viewers);
    }

    /**
     * Returns the item frames that make up the screen.
     * 
     * @return a copy of the 2d array of the item frames
     */
    public ItemFrame[][] getItemFrames() {
        return Arrays.stream(itemFrames).map(arr -> arr == null ? null : arr.clone()).toArray(ItemFrame[][]::new);
    }

    /**
     * Returns the renderer of the screen.
     * 
     * @return the renderer of the screen
     */
    public Renderer getRenderer() {
        return renderer;
    }

    /**
     * Returns the spawner of the screen.
     * 
     * @return the spawner of the screen
     */
    public ScreenSpawner getSpawner() {
        return spawner;
    }

    /**
     * Returns the location of the screen.
     * 
     * @return the location of the screen
     */
    public ScreenLocation getLocation() {
        return location;
    }

    /**
     * Returns the block width of the screen.
     * 
     * @return the block width of the screen
     */
    public int getWidth() {
        return width;
    }

    /**
     * Returns the block height of the screen.
     * 
     * @return the block height of the screen
     */
    public int getHeight() {
        return height;
    }

    /**
     * Returns the pixel width of the screen.
     * 
     * @return the pixel width of the screen
     */
    public int getPixelWidth() {
        return width * MapItem.WIDTH;
    }

    /**
     * Returns the pixel height of the screen.
     * 
     * @return the pixel height of the screen
     */
    public int getPixelHeight() {
        return height * MapItem.HEIGHT;
    }

    @Override
    public ClickEventHandler getOnClick() {
        return clickEventHandler;
    }

    @Override
    public void setOnClick(ClickEventHandler clickEventHandler) {
        this.clickEventHandler = Objects.requireNonNullElse(clickEventHandler, ClickEventHandler.empty());
    }

    @Override
    public ScrollEventHandler getOnScroll() {
        return scrollEventHandler;
    }

    @Override
    public void setOnScroll(ScrollEventHandler scrollEventHandler) {
        this.scrollEventHandler = Objects.requireNonNullElse(scrollEventHandler, ScrollEventHandler.empty());
    }

    /**
     * Returns the max allowed click radius (in blocks) of the screen.
     * 
     * @return the max allowed click radius (in blocks) of the screen
     */
    public double getClickRadius() {
        return clickRadius;
    }

    /**
     * Sets the max allowed click radius (in blocks) of the screen. It is
     * recommended to keep this value small.
     *
     * @param clickRadius the new max allowed click radius
     * @throws IllegalArgumentException if {@code clickRadius < 0}
     */
    public void setClickRadius(double clickRadius) {
        if (clickRadius < 0) {
            throw new IllegalArgumentException("Click radius cannot be negative.");
        }
        this.clickRadius = clickRadius;
    }

    /**
     * Returns the max allowed scroll radius (in blocks) of the screen.
     * 
     * @return the max allowed scroll radius (in blocks) of the screen
     */
    public double getScrollRadius() {
        return scrollRadius;
    }

    /**
     * Sets the max allowed scroll radius (in blocks) of the screen. It is
     * recommended to keep this value small.
     * 
     * @param scrollRadius the new max allowed scroll radius
     * @throws IllegalArgumentException if {@code scrollRadius < 0}
     */
    public void setScrollRadius(double scrollRadius) {
        if (scrollRadius < 0) {
            throw new IllegalArgumentException("Scroll radius cannot be negative.");
        }
        this.scrollRadius = scrollRadius;
    }

    /**
     * Returns the current state of the screen.
     * 
     * @return the current state of the screen
     */
    public State getState() {
        return state;
    }

    private void initializeItemFrames(boolean invisible) {
        Direction direction = location.direction();
        int multiplierX = direction.getMultiplierX();
        int multiplierZ = direction.getMultiplierZ();

        Location origin = location.origin();
        World world = origin.getWorld();

        for (int i = 0; i < itemFrames.length; i++) {
            for (int j = 0; j < itemFrames[i].length; j++) {
                int x = origin.getBlockX() + (j * multiplierX);
                int y = origin.getBlockY() - i;
                int z = origin.getBlockZ() + (j * multiplierZ);

                itemFrames[i][j] = new ItemFrame(new Location(world, x, y, z), direction, new MapItem(world), invisible);
            }
        }
    }

    private static BufferedImage getDefaultImage(int width, int height) {
        BufferedImage image = new BufferedImage(width * MapItem.WIDTH, height * MapItem.HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics graphics = image.getGraphics();
        graphics.setColor(new Color(13, 13, 13));
        graphics.fillRect(0, 0, image.getWidth(), image.getHeight());
        graphics.dispose();
        return image;
    }

}
