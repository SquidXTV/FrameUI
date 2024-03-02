package me.squidxtv.frameui.core.actions.click;

import me.squidxtv.frameui.core.actions.initiator.ActionInitiator;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface ClickAction {

    void perform(@NotNull ActionInitiator initiator, int clickX, int clickY);

    static @NotNull ClickAction empty() {
        return (initiator, clickX, clickY) -> {};
    }

}
