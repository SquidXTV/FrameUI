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
package me.squidxtv.frameui;

import io.github.retrooper.packetevents.bstats.bukkit.Metrics;
import io.github.retrooper.packetevents.bstats.charts.SimplePie;
import org.bukkit.Server;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;

import me.squidxtv.frameui.api.ScreenRegistry;
import me.squidxtv.frameui.api.ScreenRegistryImpl;
import me.squidxtv.frameui.listener.ClickListener;
import me.squidxtv.frameui.listener.LeaveListener;
import me.squidxtv.frameui.listener.ScrollListener;

import java.util.Arrays;

/**
 * The {@code FrameUI} class is the main plugin class for the FrameUI plugin
 * library.
 */
public class FrameUI extends JavaPlugin {

    private Metrics metrics;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        Server server = getServer();

        ServicesManager services = server.getServicesManager();
        ScreenRegistry registry = new ScreenRegistryImpl();
        services.register(ScreenRegistry.class, registry, this, ServicePriority.Normal);

        PluginManager pluginManager = server.getPluginManager();
        pluginManager.registerEvents(new LeaveListener(registry), this);

        FileConfiguration config = getConfig();
        if (config.getBoolean("features.click", true)) {
            pluginManager.registerEvents(new ClickListener(registry), this);
        }

        if (config.getBoolean("features.scroll", true)) {
            pluginManager.registerEvents(new ScrollListener(registry), this);
        }

        metrics = new Metrics(this, 23942);
        metrics.addCustomChart(new SimplePie("dependents_count",
                () -> String.valueOf(Arrays.stream(pluginManager.getPlugins())
                        .flatMap(plugin -> plugin.getDescription().getDepend().stream())
                        .filter(s -> s.equalsIgnoreCase("FrameUI"))
                        .count())));
    }

    @Override
    public void onDisable() {
        metrics.shutdown();
    }

}
