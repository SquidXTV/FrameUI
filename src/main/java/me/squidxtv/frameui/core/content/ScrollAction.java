package me.squidxtv.frameui.core.content;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface ScrollAction {

    void performScroll(@NotNull ElementNode element, int direction, int speed, int x, int y);

}
