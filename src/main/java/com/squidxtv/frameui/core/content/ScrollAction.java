package com.squidxtv.frameui.core.content;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface ScrollAction {

    void performScroll(@NotNull Component component, int direction, int speed, int clickX, int clickY);
}
