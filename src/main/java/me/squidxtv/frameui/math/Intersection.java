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
package me.squidxtv.frameui.math;

import me.squidxtv.frameui.core.Screen;

import java.awt.Point;

/**
 * The {@code Intersection} record represents an intersection between a player
 * and a screen.
 * 
 * @param screen          the screen where the intersection occurs
 * @param pixel           the pixel location on the screen
 * @param distanceSquared the squared distance between player and screen
 *                        intersection
 */
public record Intersection(Screen screen, Point pixel, double distanceSquared) {}
