package me.squidxtv.frameui.core.actions;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface ScrollAction {

    void perform(@NotNull Player player, @NotNull Scrollable.ScrollDirection direction, int scrollX, int scrollY);

}
