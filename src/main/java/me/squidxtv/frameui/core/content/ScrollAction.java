package me.squidxtv.frameui.core.content;

@FunctionalInterface
public interface ScrollAction {

    void performScroll(ElementNode element, int direction, int speed, int x, int y);

}
