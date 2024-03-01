package me.squidxtv.frameui.listener;

import me.squidxtv.frameui.core.Screen;
import me.squidxtv.frameui.core.actions.ActionType;
import me.squidxtv.frameui.core.actions.initiator.ActionInitiator;
import me.squidxtv.frameui.core.actions.scroll.ScrollDirection;
import me.squidxtv.frameui.core.math.IntersectionHelper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.jetbrains.annotations.NotNull;

import java.awt.Point;


public class ScrollListener implements Listener {


    @EventHandler
    public void onScroll(@NotNull PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        IntersectionHelper.getScreenIntersection(player, ActionType.SCROLL).ifPresent(intersection -> {
            int previousSlot = event.getPreviousSlot();
            int currentSlot = event.getNewSlot();

            int direction = currentSlot - previousSlot;
            if (currentSlot == 0 && previousSlot == 8) {
                direction = 1;
            } else if (currentSlot == 8 && previousSlot == 0) {
                direction = -1;
            }
            if (Math.abs(direction) != 1) {
                return;
            }

            Point pixel = intersection.pixel();
            Screen<?> screen = intersection.screen();
            screen.scroll(ActionInitiator.ofPlayer(player), ScrollDirection.getDirection(direction), pixel.x, pixel.y);
        });
    }

}
