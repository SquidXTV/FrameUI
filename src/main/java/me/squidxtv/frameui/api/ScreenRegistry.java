package me.squidxtv.frameui.api;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import me.squidxtv.frameui.core.Screen;

import java.util.List;
import java.util.UUID;

/**
 * The {@code ScreenRegistry} interface is used to manage {@link Screen}
 * instances across the entire server.
 */
public interface ScreenRegistry {

    /**
     * Adds a {@link Screen} to the registry.
     *
     * @param screen the screen to add to the registry
     */
    void add(Screen screen);

    /**
     * Removes a {@link Screen} from the registry.
     *
     * @param screen the screen to remove from the registry
     */
    void remove(Screen screen);

    /**
     * Returns a list of all screens in the registry.
     * 
     * @return a list of all screens
     */
    List<Screen> getAll();

    /**
     * Returns a list of screens associated with a specific plugin.
     * 
     * @param plugin the plugin to filter registered screens by
     * @return a list of screens associated with the given plugin
     */
    List<Screen> getByPlugin(Plugin plugin);

    /**
     * Returns a list of screens that contain the specific player as a viewer.
     * 
     * @param viewer the viewer to filter registered screens by
     * @return a list of screens that contain the given viewer
     */
    List<Screen> getByViewer(Player viewer);

    /**
     * Returns a list of screens that contain the specific uuid of a viewer.
     * 
     * @param uuid the uuid to filter registered screens by
     * @return a list of screens that contain the given uuid of a viewer
     */
    List<Screen> getByViewer(UUID uuid);

}
