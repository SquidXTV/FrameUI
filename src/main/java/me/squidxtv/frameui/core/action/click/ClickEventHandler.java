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
package me.squidxtv.frameui.core.action.click;

import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Objects;

/**
 * A functional interface representing a handler for click events.
 */
@FunctionalInterface
public interface ClickEventHandler {

    /**
     * Performs the click interaction.
     * 
     * @param event  the event that causes this click interaction
     * @param clickX the x-coordinate on the element
     * @param clickY the y-coordinate on the element
     */
    void perform(PlayerInteractEvent event, int clickX, int clickY);

    /**
     * Returns a composed {@code ClickEventHandler} that performs, in sequence, this
     * handler followed by the provided {@code after} handler.
     * 
     * @param after the handler to perform after this one
     * @return a composed {@code ClickEventHandler}
     * @throws NullPointerException if the provided {@code after} handler is null
     */
    default ClickEventHandler andThen(ClickEventHandler after) {
        Objects.requireNonNull(after);
        return (event, clickX, clickY) -> {
            perform(event, clickX, clickY);
            after.perform(event, clickX, clickY);
        };
    }

    /**
     * Returns an empty {@code ClickEventHandler} that performs no action.
     * 
     * @return an empty click event handler
     */
    static ClickEventHandler empty() {
        return (_, _, _) -> {};
    }

}
