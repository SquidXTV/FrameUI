package me.squidxtv.frameui.api.builder;

import me.squidxtv.frameui.core.Renderer;
import me.squidxtv.frameui.core.Screen;
import me.squidxtv.frameui.core.ScreenSpawner;
import me.squidxtv.frameui.math.Direction;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Collection;

/**
 * Builder class for creating and configuring instances of {@link Screen}.
 * This builder uses a fluent API style, enabling method chaining for ease of use.
 *
 * <p>Example usage:</p>
 *
 * <pre>
 * {@code
 * public void example(ScreenRegistry registry) {
 *     var player = Bukkit.getPlayer("mike");
 *     var screen = FramesApi.newScreen()
 *             .withSize(10, 10)
 *             .withViewer(player)
 *             .withName("Main Menu Screen")
 *             .withLocation(player.getLocation())
 *             .build();
 *
 *     screen.open();
 * }
 * }
 * </pre>
 */
public interface ScreenBuilder {

    /**
     * @param width the number of horizontal item frames
     */
    ScreenBuilder withWidth(int width);

    /**
     * @param height the number of vertical item frames
     */
    ScreenBuilder withHeight(int height);

    /**
     * @param width  the number of horizontal item frames
     * @param height the number of vertical item frames
     */
    ScreenBuilder withSize(int width, int height);

    /**
     * Sets the name of the screen. The name can be used as a screen identifier.
     *
     * @param name the name of the screen
     */
    ScreenBuilder withName(String name);

    /**
     * Sets minimal distance from with player can interact with screen
     *
     * @param radios value of the radios
     * @return
     */
    ScreenBuilder withClickRadius(double radios);

    /**
     * @param scrollRadios value of the scrollRadios
     * @return
     */
    ScreenBuilder withScrollRadius(double scrollRadios);

    /**
     * Adds a viewer to the screen. Multiple viewers can be added by calling this method multiple times.
     * When screen location haven't set yet, then sets screen location as player location
     *
     * @param player the player to add as a viewer
     */
    ScreenBuilder withViewer(Player player);

    /**
     * Perform withViewer method for multiple players
     *
     * @param players array of players
     */
    ScreenBuilder withViewer(Player... players);

    /**
     * Perform withViewer method for multiple players
     *
     * @param players collection of players
     */
    ScreenBuilder withViewer(Collection<Player> players);


    /**
     * Sets the location of the screen.
     *
     * @param location the location of the screen
     */
    ScreenBuilder withLocation(Location location);

    /**
     * Sets the location and direction of the screen.
     *
     * @param location  the location of the screen
     * @param direction the direction in which the screen is facing
     */
    ScreenBuilder withLocation(Location location, Direction direction);

    /**
     * Sets the renderer for the screen.
     *
     * @param renderer the renderer to be used for the screen
     */
    ScreenBuilder withRenderer(Renderer renderer);


    /**
     * Sets the ScreenSpawner for the screen.
     *
     * @param spawner used to perform spawning screen to players
     */
    ScreenBuilder withSpawner(ScreenSpawner spawner);


    /**
     * Builds a new instance of {@link Screen} based on the current configuration of the builder.
     *
     * @return a new instance of {@link Screen}
     */
    Screen build();

    /**
     * Builds a new instance of {@link Screen} based on the current configuration of the builder
     * and opens it immediately.
     *
     * @return a new instance of {@link Screen}
     */
    Screen buildAndOpen();
}
