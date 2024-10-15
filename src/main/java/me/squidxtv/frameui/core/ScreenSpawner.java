package me.squidxtv.frameui.core;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;

/**
 * The {@code ScreenSpawner} is responsible for spawning and despawning
 * {@link Screen} instances.
 */
public interface ScreenSpawner {

    /**
     * Spawns the given {@link Screen} for the specified players.
     * 
     * @param screen  the screen to be spawned
     * @param viewers the collection of players to spawn the screen for
     */
    void spawn(Screen screen, Collection<Player> viewers);

    /**
     * Despawns the given {@link Screen} for the specified players.
     * 
     * @param screen  the screen to be despawned
     * @param viewers the collection of players to despawn the screen for
     */
    void despawn(Screen screen, Collection<Player> viewers);

    /**
     * Updates the display of the given {@link Screen}. Used for reflecting the
     * pixel changes to the viewers by sending information with packets or similar.
     * 
     * @param screen the screen to be updated
     */
    void update(Screen screen);

    /**
     * Spawns the given {@link Screen} for its current viewers.
     * 
     * @param screen the screen to be spawned
     */
    default void spawn(Screen screen) {
        spawn(screen, screen.getViewers().stream().map(Bukkit::getPlayer).toList());
    }

    /**
     * Despawns the given {@link Screen} for its current viewers.
     * 
     * @param screen the screen to be despawned
     */
    default void despawn(Screen screen) {
        despawn(screen, screen.getViewers().stream().map(Bukkit::getPlayer).toList());
    }

    /**
     * Spawns the given {@link Screen} for a single player.
     * 
     * @param screen the screen to be spawned
     * @param viewer the player to spawn the screen for
     */
    default void spawn(Screen screen, Player viewer) {
        spawn(screen, List.of(viewer));
    }

    /**
     * Despawns the given {@link Screen} for a single player.
     * 
     * @param screen the screen to be despawned
     * @param viewer the player to despawn the screen for
     */
    default void despawn(Screen screen, Player viewer) {
        despawn(screen, List.of(viewer));
    }

}
