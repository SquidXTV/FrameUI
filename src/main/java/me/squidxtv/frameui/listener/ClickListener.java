package me.squidxtv.frameui.listener;

import me.squidxtv.frameui.api.ScreenRegistry;
import me.squidxtv.frameui.core.Screen;
import me.squidxtv.frameui.math.Intersection;
import me.squidxtv.frameui.math.IntersectionHelper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.awt.Point;
import java.util.Optional;

public class ClickListener implements Listener {

    private final ScreenRegistry registry;

    public ClickListener(ScreenRegistry registry) {
        this.registry = registry;
    }

    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        if (event.getAction() == Action.PHYSICAL) {
            return;
        }

        Player player = event.getPlayer();
        Optional<Intersection> nearest = IntersectionHelper.getNearestIntersection(registry.getByViewer(player), player, Screen::getClickRadius);
        nearest.ifPresent(intersection -> {
            Point pixel = intersection.pixel();
            intersection.screen().click(event, pixel.x, pixel.y);
        });
    }

}
