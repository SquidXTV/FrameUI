package me.squidxtv.frameui.core.actions.scroll;

import me.squidxtv.frameui.core.actions.initiator.ActionInitiator;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface ScrollAction {

    void perform(@NotNull ActionInitiator<?> initiator, @NotNull ScrollDirection direction, int scrollX, int scrollY);

}
