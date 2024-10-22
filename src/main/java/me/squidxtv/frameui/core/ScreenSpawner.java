/*
 * FrameUI: Minecraft plugin library designed to easily create screens within a server.
 * Copyright (C) 2023-2024 Connor Schweighöfer
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
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
