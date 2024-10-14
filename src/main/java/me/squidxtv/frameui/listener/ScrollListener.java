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
import java.util.List;
import java.util.Optional;

/**
 * The {@code ScrollListener} is responsible for handling scrolls for interacting with a {@link Screen}.
 */
public class ScrollListener implements Listener {

    private final ScreenRegistry registry;

    /**
     * Creates the {@code ScrollListener}.
     * @param registry the screen registry
     */
    public ScrollListener(ScreenRegistry registry) {
        this.registry = registry;
    }

    /**
     * Handles the {@link PlayerItemHeldEvent}.
     * @param event the {@link PlayerItemHeldEvent}
     * @implNote The implementation assumes the item changed by a scroll on the client.
     * A scroll action is only considered valid if the item is changed by a single position.
     * If the change is by more than one position, it is ambiguous which direction the player scrolled,
     * and therefore no scroll event is triggered.
     */
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
        List<Screen> screens = registry.getByViewer(player);
        Optional<Intersection> nearest = IntersectionHelper.getNearestIntersection(screens, player, Screen::getScrollRadius);
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
