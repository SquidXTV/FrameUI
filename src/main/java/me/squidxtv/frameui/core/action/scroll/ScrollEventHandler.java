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
package me.squidxtv.frameui.core.action.scroll;

import org.bukkit.event.player.PlayerItemHeldEvent;

import java.util.Objects;

/**
 * A functional interface representing a handler for scroll events.
 */
public interface ScrollEventHandler {

    /**
     * Performs the scroll interaction.
     * 
     * @param event     the event that causes this scroll interaction
     * @param direction the direction of the scroll interaction
     * @param scrollX   the x-coordinate on the element
     * @param scrollY   the y-coordinate on the element
     */
    void perform(PlayerItemHeldEvent event, ScrollDirection direction, int scrollX, int scrollY);

    /**
     * Returns a composed {@code ScrollEventHandler} that performs, in sequence,
     * this handler followed by the provided {@code after} handler.
     * 
     * @param after the handler to perform after this one
     * @return a composed {@code ScrollEventHandler}
     * @throws NullPointerException if the provided {@code after} handler is null
     */
    default ScrollEventHandler andThen(ScrollEventHandler after) {
        Objects.requireNonNull(after);
        return (event, direction, scrollX, scrollY) -> {
            perform(event, direction, scrollX, scrollY);
            after.perform(event, direction, scrollX, scrollY);
        };
    }

    /**
     * Returns an empty {@code ScrollEventHandler} that performs no action.
     * 
     * @return an empty scroll event handler
     */
    static ScrollEventHandler empty() {
        return (_, _, _, _) -> {};
    }

}
