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
 */
public interface ScreenBuilder {

    /**
     * Sets the width of the screen.
     *
     * @param width the number of horizontal item frames
     * @return this builder instance for method chaining
     */
    ScreenBuilder withWidth(int width);

    /**
     * Sets the height of the screen.
     *
     * @param height the number of vertical item frames
     * @return this builder instance for method chaining
     */
    ScreenBuilder withHeight(int height);

    /**
     * Sets the size of the screen in item frames.
     *
     * @param width  the number of horizontal item frames
     * @param height the number of vertical item frames
     * @return this builder instance for method chaining
     */
    ScreenBuilder withSize(int width, int height);

    /**
     * Sets the name of the screen. The name can be used as a screen identifier.
     *
     * @param name the name of the screen
     * @return this builder instance for method chaining
     */
    ScreenBuilder withName(String name);

    /**
     * Sets the minimal distance from which a player can interact with the screen.
     *
     * @param radius the interaction radius
     * @return this builder instance for method chaining
     */
    ScreenBuilder withClickRadius(double radius);

    /**
     * Sets the scroll radius for the screen.
     *
     * @param scrollRadius the scroll radius
     * @return this builder instance for method chaining
     */
    ScreenBuilder withScrollRadius(double scrollRadius);

    /**
     * Adds a viewer to the screen. Multiple viewers can be added by calling this method multiple times.
     * If the screen location has not been set yet, it sets the screen location as the player's location.
     *
     * @param player the player to add as a viewer
     * @return this builder instance for method chaining
     */
    ScreenBuilder withViewer(Player player);

    /**
     * Adds multiple viewers to the screen.
     *
     * @param players an array of players
     * @return this builder instance for method chaining
     */
    ScreenBuilder withViewer(Player... players);

    /**
     * Adds multiple viewers to the screen.
     *
     * @param players a collection of players
     * @return this builder instance for method chaining
     */
    ScreenBuilder withViewer(Collection<Player> players);

    /**
     * Sets the location of the screen.
     *
     * @param location the location of the screen
     * @return this builder instance for method chaining
     */
    ScreenBuilder withLocation(Location location);

    /**
     * Sets the location and direction of the screen.
     *
     * @param location  the location of the screen
     * @param direction the direction in which the screen is facing
     * @return this builder instance for method chaining
     */
    ScreenBuilder withLocation(Location location, Direction direction);

    /**
     * Sets the renderer for the screen.
     *
     * @param renderer the renderer to be used for the screen
     * @return this builder instance for method chaining
     * @see Renderer
     */
    ScreenBuilder withRenderer(Renderer renderer);

    /**
     * Sets the ScreenSpawner for the screen.
     *
     * @param spawner used to perform spawning screen to players
     * @return this builder instance for method chaining
     * @see ScreenSpawner
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
