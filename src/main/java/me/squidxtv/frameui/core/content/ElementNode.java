package me.squidxtv.frameui.core.content;

import org.jetbrains.annotations.NotNull;

import java.awt.*;

public abstract class ElementNode {

    private final @NotNull String id;

    private ClickAction clickAction;
    private ScrollAction scrollAction;

    protected ElementNode(@NotNull String id) {
        this.id = id;
    }

    public void setClickAction(ClickAction clickAction) {
        this.clickAction = clickAction;
    }

    public void setScrollAction(ScrollAction scrollAction) {
        this.scrollAction = scrollAction;
    }

    public void scroll(int direction, int speed, int x, int y) {
        if (scrollAction == null) {
            return;
        }
        scrollAction.performScroll(this, direction, speed, x, y);
    }

    public void click(int x, int y) {
        if (clickAction == null) {
            return;
        }
        clickAction.onClick(this, x, y);
    }

    public abstract void draw(Graphics g);

    public @NotNull String getId() {
        return id;
    }

}
