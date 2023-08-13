package me.squidxtv.frameui.core.actions;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface Scrollable {

    boolean scroll(@NotNull Player player, @NotNull ScrollDirection direction, int x, int y);

    enum ScrollDirection {
        LEFT(-1),
        RIGHT(1);

        private final int direction;

        ScrollDirection(int direction) {
            this.direction = direction;
        }

    }
}
