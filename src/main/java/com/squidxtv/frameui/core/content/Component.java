package com.squidxtv.frameui.core.content;

import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.image.*;
import java.util.ArrayList;
import java.util.Collection;

public class Component {

    protected BufferedImage texture;
    protected ClickAction clickAction;
    protected ScrollAction scrollAction;
    protected final @NotNull Collection<Text> texts;

    protected int x;
    protected int y;

    protected final int width;
    protected final int height;

    public Component(BufferedImage texture, int x, int y, int width, int height) {
        this.texture = texture;
        this.clickAction = null;
        this.scrollAction = null;
        this.texts = new ArrayList<>();

        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void draw(@NotNull Graphics graphics) {
        if (texture != null) {
            graphics.drawImage(texture, x, y, width, height, null);
        }
        for (Text text : texts) {
            Point drawPoint = text.getDrawPoint(this, graphics);
            graphics.drawString(text.getString(), drawPoint.x, drawPoint.y);
        }
    }

    public void click(int x, int y) {
        if (clickAction == null) {
            return;
        }
        if (x < this.x || y < this.y || x > this.x + this.width || y > this.y + this.height) {
            return;
        }

        clickAction.performClick(this, x, y);
    }

    public void scroll(int direction, int speed, int x, int y) {
        if (scrollAction == null) {
            return;
        }

        if (x < this.x || y < this.y || x > this.x + this.width || y > this.y + this.height) {
            return;
        }

        scrollAction.performScroll(this, direction, speed, x, y);
    }

    public void move(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setTexture(BufferedImage texture) {
        this.texture = texture;
    }

    public void setClickAction(ClickAction clickAction) {
        this.clickAction = clickAction;
    }

    public void setScrollAction(ScrollAction scrollAction) {
        this.scrollAction = scrollAction;
    }

    public void add(@NotNull Text text) {
        this.texts.add(text);
    }

    public void addAll(@NotNull Collection<Text> texts) {
        this.texts.addAll(texts);
    }

    public void remove(@NotNull Text text) {
        this.texts.remove(text);
    }

    public void removeAll(@NotNull Collection<Text> texts) {
        this.texts.removeAll(texts);
    }

    public void clear() {
        this.texts.clear();
    }

}
