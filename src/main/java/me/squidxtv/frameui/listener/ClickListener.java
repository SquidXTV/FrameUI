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
package me.squidxtv.frameui.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import me.squidxtv.frameui.api.ScreenRegistry;
import me.squidxtv.frameui.core.Screen;
import me.squidxtv.frameui.math.Intersection;
import me.squidxtv.frameui.math.IntersectionHelper;

import java.awt.Point;
import java.util.List;
import java.util.Optional;

/**
 * The {@code ClickListener} is responsible for handling player clicks for
 * interacting with a {@link Screen}.
 */
public class ClickListener implements Listener {

    private final ScreenRegistry registry;

    /**
     * Creates the {@code ClickListener}.
     * 
     * @param registry the screen registry
     */
    public ClickListener(ScreenRegistry registry) {
        this.registry = registry;
    }

    /**
     * Handles the {@link PlayerInteractEvent}.
     * 
     * @param event the {@link PlayerInteractEvent} triggered by the player
     * @implNote Triggers the click on the nearest {@link Screen} that contains the
     *           player as a viewer
     */
    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        if (event.getAction() == Action.PHYSICAL) {
            // ignore physical actions, as they do not involve clicking
            return;
        }

        Player player = event.getPlayer();
        List<Screen> screens = registry.getByViewer(player);
        Optional<Intersection> nearest = IntersectionHelper.getNearestIntersection(screens, player, Screen::getClickRadius);
        nearest.ifPresent(intersection -> {
            Point pixel = intersection.pixel();
            intersection.screen().click(event, pixel.x, pixel.y);
        });
    }

}
