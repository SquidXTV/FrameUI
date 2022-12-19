package me.squidxtv.frameui.core.content;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface ClickAction {

    /**
     * Invoked when an action occurs.
     * @see Component
     * @param component Button which invokes this method
     * @param clickX click position
     * @param clickY click position
     */
    void performClick(@NotNull Component component, int clickX, int clickY);

}
