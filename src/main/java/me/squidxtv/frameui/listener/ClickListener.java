package me.squidxtv.frameui.listener;

import me.squidxtv.frameui.core.Screen;
import me.squidxtv.frameui.core.properties.ScreenIntersection;
import me.squidxtv.frameui.util.ScreenUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


/**
 * Listener handling clicks on Screen.
 */
public final class ClickListener implements Listener {

    private final @NotNull Map<UUID, Long> lastClickTimes = new HashMap<>();
    private static final long COOLDOWN_MILLIS = 1000;

    /**
     * Triggered when player clicks on a block or in air.
     * Calculates click and triggers on specific screen.
     * @param event {@link PlayerInteractEvent}
     * @implNote implementation uses one second cooldown, also click will be triggered on nearest Screen.
     */
    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        UUID playerId = player.getUniqueId();
        long currentTime = System.currentTimeMillis();
        Long lastClickTime = lastClickTimes.get(playerId);
        if (lastClickTime != null && currentTime - lastClickTime < COOLDOWN_MILLIS) {
            return;
        }
        lastClickTimes.put(playerId, currentTime);

        ScreenIntersection screenIntersectionWithPlayer = ScreenUtil.getScreenIntersection(player);
        if (screenIntersectionWithPlayer == null) {
            return;
        }

        Screen screen = screenIntersectionWithPlayer.screen();
        Vector intersection = screenIntersectionWithPlayer.intersection();

        Point pixels = ScreenUtil.getPixel(screen, intersection.clone());
        screen.click(player, pixels.x, pixels.y);
    }


}
