package me.squidxtv.frameui.core.properties;

import me.squidxtv.frameui.core.Screen;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public record ScreenIntersection(Screen screen, Vector intersection) {
}
