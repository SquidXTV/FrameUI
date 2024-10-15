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
