package me.squidxtv.frameui.core.actions;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface Clickable {

    boolean click(@NotNull Player player, int x, int y);

}
