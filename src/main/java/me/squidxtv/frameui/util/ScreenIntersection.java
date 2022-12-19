package me.squidxtv.frameui.util;

import me.squidxtv.frameui.core.Screen;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public record ScreenIntersection(@NotNull Screen screen, @NotNull Vector intersection) {
}
