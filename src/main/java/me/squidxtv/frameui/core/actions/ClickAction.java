package me.squidxtv.frameui.core.actions;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface ClickAction {

    void perform(@NotNull Player player, int clickX, int clickY);
}
