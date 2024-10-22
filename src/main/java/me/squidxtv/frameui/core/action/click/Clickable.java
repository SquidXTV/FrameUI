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

/**
 * Represents an element that can be clicked.
 */
public interface Clickable {

    /**
     * Performs a click on the element.
     * 
     * @param event  the event that causes this click interaction
     * @param clickX the x-coordinate on the element
     * @param clickY the y-coordinate on the element
     */
    default void click(PlayerInteractEvent event, int clickX, int clickY) {
        getOnClick().perform(event, clickX, clickY);
    }

    /**
     * Returns the current {@link ClickEventHandler} for this element.
     * 
     * @return the current {@link ClickEventHandler} for this element
     */
    ClickEventHandler getOnClick();

    /**
     * Sets the current {@link ClickEventHandler} for this element
     * 
     * @param clickEventHandler the new click event handler
     */
    void setOnClick(ClickEventHandler clickEventHandler);

}
