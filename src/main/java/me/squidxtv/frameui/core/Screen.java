package me.squidxtv.frameui.core;

import me.squidxtv.frameui.api.ScreenRegistry;
import me.squidxtv.frameui.api.data.ScreenProperties;
import me.squidxtv.frameui.core.action.click.ClickEventHandler;
import me.squidxtv.frameui.core.action.click.Clickable;
import me.squidxtv.frameui.core.action.scroll.ScrollEventHandler;
import me.squidxtv.frameui.core.action.scroll.Scrollable;
import me.squidxtv.frameui.core.impl.BufferedImageRenderer;
import me.squidxtv.frameui.core.impl.PacketScreenSpawner;
import me.squidxtv.frameui.math.Direction;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

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
 * The {@code Screen} is a 2d plane of multiple {@link ItemFrame} instances in the Minecraft World. Together they make up a bigger image.
 * Each {@code ItemFrame} hold a {@link MapItem} that display a custom image which is limited to 128x128 pixels.
 */
public class Screen implements Clickable, Scrollable {

    private static final ScreenRegistry REGISTRY = Objects.requireNonNull(Bukkit.getServicesManager().load(ScreenRegistry.class), "Couldn't properly load ScreenRegistry from the ServicesManager.");
    private final Plugin plugin;
    private final Set<UUID> viewers = new HashSet<>();
    private final ItemFrame[][] itemFrames;
    private final Renderer renderer;
    private final ScreenSpawner spawner;
    private final ScreenProperties properties;
    private ClickEventHandler clickEventHandler = ClickEventHandler.empty();
    private ScrollEventHandler scrollEventHandler = ScrollEventHandler.empty();


    /**
     * The {@code State} represents the possible states of a screen.
     */
    public enum State {
        OPEN,
        CLOSED
    }

    /**
     * Creates a new screen with the specified plugin, dimensions, location, spawner and renderer.
     *
     * @param plugin     the plugin associated with this screen
     * @param properties the screen metadata information
     * @param spawner    the spawner responsible for spawning the screen's item frames
     * @param renderer   the renderer responsible for rendering the screen's content
     * @throws NullPointerException if {@code plugin}, {@code location}, {@code spawner}, or {@code renderer} is null
     */
    public Screen(Plugin plugin,
                  ScreenProperties properties,
                  ScreenSpawner spawner,
                  Renderer renderer) {
        Objects.requireNonNull(plugin);
        Objects.requireNonNull(properties);
        Objects.requireNonNull(spawner);
        Objects.requireNonNull(renderer);
        this.plugin = plugin;
        this.itemFrames = new ItemFrame[properties.getHeight()][properties.getWidth()];
        this.spawner = spawner;
        this.renderer = renderer;
        this.properties = properties;
    }

    /**
     * Opens the screen with invisible item frames.
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
        if (hasState(State.OPEN)) {
            close();
        }

        REGISTRY.add(this);
        initializeItemFrames(invisible);
        spawner.spawn(this);
        properties.setState(State.OPEN);
    }

    /**
     * Updates the screen.
     */
    public void update() {
        if (hasState(State.CLOSED)) {
            return;
        }
        renderer.update(this);
        spawner.update(this);
    }

    /**
     * Closes the screen.
     */
    public void close() {
        if (hasState(State.CLOSED)) {
            return;
        }

        REGISTRY.remove(this);
        spawner.despawn(this);
        for (ItemFrame[] row : itemFrames) {
            Arrays.fill(row, null);
        }
        properties.setState(State.CLOSED);
    }

    /**
     * Adds a player to the screen's viewers.
     *
     * @param player the player to add
     * @return {@code true} if player was added based on {@link Set#add(Object)}, {@code false} otherwise
     * @throws NullPointerException if {@code player} is null
     */
    public boolean addViewer(Player player) {
        Objects.requireNonNull(player);
        boolean added = viewers.add(player.getUniqueId());

        if (added && hasState(State.OPEN)) {
            spawner.spawn(this, player);
        }

        return added;
    }

    /**
     * Adds a collection of players to the screen's viewers.
     *
     * @param players the players to add
     * @return the set including the players that actually got added based on {@link Set#add(Object)}
     * @throws NullPointerException if {@code players} is null
     */
    public Set<Player> addViewer(Collection<Player> players) {
        Objects.requireNonNull(players);
        Set<Player> added = new HashSet<>(players);
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
        Objects.requireNonNull(player);
        boolean removed = viewers.remove(player.getUniqueId());

        if (removed && getState() == State.OPEN) {
            spawner.despawn(this, player);
        }

        return removed;
    }

    /**
     * Removes a collection of players from the screen's viewers.
     *
     * @param players the players to remove
     * @return the set including the players that actually got removed based on {@link Set#add(Object)}
     * @throws NullPointerException if {@code players} is null
     */
    public Set<Player> removeViewer(Collection<Player> players) {
        Objects.requireNonNull(players);
        Set<Player> removed = new HashSet<>(players);
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
        spawner.despawn(this, viewers.stream().map(Bukkit::getPlayer).toList());
        viewers.clear();
    }

    /**
     * Checks if a player is a viewer of the screen.
     *
     * @param viewer the viewer to check
     * @return {@code true} if the player is a viewer of this screen, {@code false} otherwise
     * @throws NullPointerException if {@code viewer} is null
     */
    public boolean hasViewer(Player viewer) {
        Objects.requireNonNull(viewer);
        return viewers.contains(viewer.getUniqueId());
    }

    /**
     * Checks if a player is a viewer of the screen.
     *
     * @param uuid uuid to check
     * @return {@code true} if the player is a viewer of this screen, {@code false} otherwise
     */
    public boolean hasViewer(UUID uuid) {
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
     * Sets the name of the screen
     */
    public void setName(String name) {
        properties.setName(name);
    }

    /**
     * Returns the name of the screen
     *
     * @return the name of the screen
     */
    public String getName() {
        return properties.getName();
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
     * @return the screen location of the screen
     */
    public ScreenLocation getLocation() {
        return properties.getScreenLocation();
    }

    /**
     * Returns the block width of the screen.
     *
     * @return the block width of the screen
     */
    public int getWidth() {
        return properties.getWidth();
    }

    /**
     * Returns the block height of the screen.
     *
     * @return the block height of the screen
     */
    public int getHeight() {
        return properties.getHeight();
    }

    /**
     * Returns the pixel width of the screen.
     *
     * @return the pixel width of the screen
     */
    public int getPixelWidth() {
        return getWidth() * MapItem.WIDTH;
    }

    /**
     * Returns the pixel height of the screen.
     *
     * @return the pixel height of the screen
     */
    public int getPixelHeight() {
        return getHeight() * MapItem.HEIGHT;
    }

    @Override
    public ClickEventHandler getOnClick() {
        return clickEventHandler;
    }

    @Override
    public void setOnClick(ClickEventHandler clickEventHandler) {
        Objects.requireNonNull(clickEventHandler);
        this.clickEventHandler = clickEventHandler;
    }

    @Override
    public ScrollEventHandler getOnScroll() {
        return scrollEventHandler;
    }

    @Override
    public void setOnScroll(ScrollEventHandler scrollEventHandler) {
        Objects.requireNonNull(scrollEventHandler);
        this.scrollEventHandler = scrollEventHandler;
    }

    /**
     * Returns the click radius of the screen.
     *
     * @return the click radius of the screen
     */
    public double getClickRadius() {
        return properties.getClickRadius();
    }

    /**
     * Sets the click radius of the screen.
     *
     * @param clickRadius the new click radius
     * @throws IllegalArgumentException if {@code clickRadius < 0}
     */
    public void setClickRadius(double clickRadius) {
        if (clickRadius < 0) {
            throw new IllegalArgumentException("Click radius cannot be negative.");
        }
        properties.setClickRadius(clickRadius);
    }

    /**
     * Returns the scroll radius of the screen.
     *
     * @return the scroll radius of the screen
     */
    public double getScrollRadius() {
        return properties.getScrollRadius();
    }

    /**
     * Sets the scroll radius of the screen
     *
     * @param scrollRadius the new scroll radius
     * @throws IllegalArgumentException if {@code scrollRadius < 0}
     */
    public void setScrollRadius(double scrollRadius) {
        if (scrollRadius < 0) {
            throw new IllegalArgumentException("Scroll radius cannot be negative.");
        }
        properties.setScrollRadius(scrollRadius);
    }

    /**
     * Returns the current state of the screen.
     *
     * @return the current state of the screen
     */
    public State getState() {
        return properties.getState();
    }


    public boolean hasState(State state) {
        return getState().equals(state);
    }

    private void initializeItemFrames(boolean invisible) {
        Direction direction = properties.getScreenLocation().direction();
        int multiplierX = direction.getMultiplierX();
        int multiplierZ = -direction.getMultiplierZ();

        Location origin = properties.getScreenLocation().origin();
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
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics graphics = image.getGraphics();
        graphics.setColor(new Color(13, 13, 13));
        graphics.fillRect(0, 0, image.getWidth(), image.getHeight());
        graphics.dispose();
        return image;
    }
}
