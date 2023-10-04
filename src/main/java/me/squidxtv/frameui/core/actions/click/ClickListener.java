package me.squidxtv.frameui.core.actions.click;

import me.squidxtv.frameui.core.Screen;
import me.squidxtv.frameui.core.actions.ActionType;
import me.squidxtv.frameui.core.actions.initiator.ActionInitiator;
import me.squidxtv.frameui.core.math.IntersectionHelper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class ClickListener implements Listener {


    @EventHandler
    public void onClick(@NotNull PlayerInteractEvent event) {
        Player player = event.getPlayer();
        IntersectionHelper.getScreenIntersection(player, ActionType.CLICK).ifPresent(intersection -> {
            Point pixel = intersection.pixel();
            Screen<?> screen = intersection.screen();
            screen.click(ActionInitiator.ofPlayer(player), pixel.x, pixel.y);
        });
    }

}
