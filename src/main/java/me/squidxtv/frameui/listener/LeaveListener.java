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
import org.bukkit.event.player.PlayerQuitEvent;

import me.squidxtv.frameui.api.ScreenRegistry;
import me.squidxtv.frameui.core.Screen;

import java.util.List;
import java.util.UUID;

/**
 * The {@code LeaveListener} is responsible for removing players from all the
 * screens they are viewing when they leave.
 */
public class LeaveListener implements Listener {

    private final ScreenRegistry registry;

    /**
     * Creates the {@code LeaveListener}.
     * 
     * @param registry the screen registry
     */
    public LeaveListener(ScreenRegistry registry) {
        this.registry = registry;
    }

    /**
     * Handles the {@link PlayerQuitEvent}.
     * 
     * @param event the {@link PlayerQuitEvent} triggered by leaving the server
     */
    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        List<Screen> screens = registry.getByViewer(uuid);
        for (Screen screen : screens) {
            screen.removeViewer(player);
        }
    }

}
