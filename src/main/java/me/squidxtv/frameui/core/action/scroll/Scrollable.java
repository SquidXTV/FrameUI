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

/**
 * Represents an element that can be scrolled.
 */
public interface Scrollable {

    /**
     * Performs a scroll on the element.
     * 
     * @param event     the event that causes this scroll interaction
     * @param direction the direction of the scroll
     * @param scrollX   the x-coordinate on the element
     * @param scrollY   the y-coordinate on the element
     */
    default void scroll(PlayerItemHeldEvent event, ScrollDirection direction, int scrollX, int scrollY) {
        getOnScroll().perform(event, direction, scrollX, scrollY);
    }

    /**
     * Returns the current {@link ScrollEventHandler} for this element.
     * 
     * @return the current {@link ScrollEventHandler} for this element
     */
    ScrollEventHandler getOnScroll();

    /**
     * Sets the current {@link ScrollEventHandler} for this element
     * 
     * @param scrollEventHandler the new scroll event handler
     */
    void setOnScroll(ScrollEventHandler scrollEventHandler);

}
