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

/**
 * The {@code ScrollDirection} enum represents the two possible scroll
 * directions, {@code UP} and {@code DOWN}. Each is associated with an integer
 * value.
 */
public enum ScrollDirection {

    /**
     * Indicates a scroll action in the upward direction.
     */
    UP(-1),
    /**
     * Indicates a scroll action in the downward direction.
     */
    DOWN(1);

    private final int direction;

    ScrollDirection(int direction) {
        this.direction = direction;
    }

    /**
     * Returns the {@code ScrollDirection} corresponding to the given integer value.
     *
     * @param direction the integer value representing the scroll direction
     * @return the {@code ScrollDirection} corresponding to the given direction
     * @throws IllegalArgumentException if the provided direction is invalid
     */
    public static ScrollDirection getDirection(int direction) {
        for (ScrollDirection value : values()) {
            if (value.direction == direction) {
                return value;
            }
        }
        throw new IllegalArgumentException("Invalid direction: " + direction);
    }

}
