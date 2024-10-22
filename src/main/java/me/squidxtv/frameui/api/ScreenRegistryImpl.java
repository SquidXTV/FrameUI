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
package me.squidxtv.frameui.api;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import me.squidxtv.frameui.core.Screen;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * The {@code ScreenRegistryImpl} class implements the {@link ScreenRegistry}
 * using a simple {@link ArrayList} to store a {@link Screen}.
 */
public class ScreenRegistryImpl implements ScreenRegistry {

    private final List<Screen> registered = new ArrayList<>();

    @Override
    public void add(Screen screen) {
        registered.add(screen);
    }

    @Override
    public void remove(Screen screen) {
        registered.remove(screen);
    }

    @Override
    public List<Screen> getAll() {
        return registered;
    }

    @Override
    public List<Screen> getByPlugin(Plugin plugin) {
        return registered.stream().filter(screen -> screen.getPlugin().equals(plugin)).toList();
    }

    @Override
    public List<Screen> getByViewer(Player viewer) {
        return registered.stream().filter(screen -> screen.hasViewer(viewer)).toList();
    }

    @Override
    public List<Screen> getByViewer(UUID uuid) {
        return registered.stream().filter(screen -> screen.hasViewer(uuid)).toList();
    }

}
