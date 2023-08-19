package me.squidxtv.frameui.core.actions.scroll;

import me.squidxtv.frameui.core.actions.initiator.ActionInitiator;
import org.jetbrains.annotations.NotNull;

public interface Scrollable {

    boolean scroll(@NotNull ActionInitiator<?> initiator, @NotNull ScrollDirection direction, int x, int y);

    enum ScrollDirection {
        LEFT(-1),
        RIGHT(1);

        private final int direction;

        ScrollDirection(int direction) {
            this.direction = direction;
        }

    }
}
