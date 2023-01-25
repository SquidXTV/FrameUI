package me.squidxtv.frameui.listener;

import me.squidxtv.frameui.core.Screen;
import me.squidxtv.frameui.core.properties.ScreenIntersection;
import me.squidxtv.frameui.util.ScreenUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.awt.*;


public final class ScrollListener implements Listener {

    /**
     * Triggered when player scrolls on screen.
     * Calculates scroll position and triggers on specific screen.
     * @param event {@link PlayerItemHeldEvent}
     * @implNote scroll triggers on nearest screen.
     */
    @EventHandler
    public void onScroll(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        ScreenIntersection screenIntersectionWithPlayer = ScreenUtil.getScreenIntersection(player);
        if (screenIntersectionWithPlayer == null) {
            return;
        }

        int previousSlot = event.getPreviousSlot();
        int currentSlot = event.getNewSlot();

        // calculate scroll direction and speed
        int direction;
        int speed;
        if (currentSlot == 0 && previousSlot == 8) {
            direction = 1;
            speed = 1;
        } else if (currentSlot == 8 && previousSlot == 0) {
            direction = -1;
            speed = 1;
        } else {
            direction = currentSlot - previousSlot;
            speed = Math.abs(direction);
        }

        Screen screen = screenIntersectionWithPlayer.screen();
        Vector intersection = screenIntersectionWithPlayer.intersection();
        Point pixels = ScreenUtil.getPixel(screen, intersection.clone());

        screen.scroll(player, direction, speed, pixels.x, pixels.y);
    }

}
