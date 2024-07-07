package me.squidxtv.frameui.listener;

import me.squidxtv.frameui.api.ScreenRegistry;
import me.squidxtv.frameui.core.Screen;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class LeaveListener implements Listener {

    private final ScreenRegistry registry;

    public LeaveListener(ScreenRegistry registry) {
        this.registry = registry;
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        for (Screen screen : registry.getByViewer(uuid)) {
            screen.removeViewer(player);
        }
    }

}
