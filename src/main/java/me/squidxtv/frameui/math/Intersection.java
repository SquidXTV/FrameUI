package me.squidxtv.frameui.math;

import me.squidxtv.frameui.core.Screen;

import java.awt.Point;

/**
 * The {@code Intersection} record represents an intersection between a player and a screen.
 * @param screen the screen where the intersection occurs
 * @param pixel the pixel location on the screen
 * @param distanceSquared the squared distance between player and screen intersection
 */
public record Intersection(Screen screen, Point pixel, double distanceSquared) {
}
