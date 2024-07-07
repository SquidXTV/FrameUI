package me.squidxtv.frameui.listener;

import me.squidxtv.frameui.api.ScreenRegistry;
import me.squidxtv.frameui.core.Screen;
import me.squidxtv.frameui.core.action.scroll.ScrollDirection;
import me.squidxtv.frameui.math.Intersection;
import me.squidxtv.frameui.math.IntersectionHelper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;

import java.awt.Point;
import java.util.Optional;

public class ScrollListener implements Listener {

    private final ScreenRegistry registry;

    public ScrollListener(ScreenRegistry registry) {
        this.registry = registry;
    }

    @EventHandler
    public void onScroll(PlayerItemHeldEvent event) {
        int previous = event.getPreviousSlot();
        int current = event.getNewSlot();

        int direction = getDirection(previous, current);

        if (Math.abs(direction) != 1) {
            // Too fast
            return;
        }

        Player player = event.getPlayer();
        Optional<Intersection> nearest = IntersectionHelper.getNearestIntersection(registry.getByViewer(player), player, Screen::getScrollRadius);
        nearest.ifPresent(intersection -> {
            Point pixel = intersection.pixel();
            intersection.screen().scroll(event, ScrollDirection.getDirection(direction), pixel.x, pixel.y);
        });
    }

    private static int getDirection(int previous, int current) {
        if (previous == 8 && current == 0) {
            return 1;
        }

        if (previous == 0 && current == 8) {
            return -1;
        }

        return current - previous;
    }

}
