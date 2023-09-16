package me.squidxtv.frameui.core.attributes;

import me.squidxtv.frameui.core.graphics.Graphics;
import me.squidxtv.frameui.core.math.BoundingBox;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Element;

import java.awt.*;
import java.util.Arrays;

public class BorderAttribute {

    private @NotNull Color color;
    private int width;

    public BorderAttribute(@NotNull Element element) {
        this(Attribute.BORDER_COLOR.get(element),
                Attribute.BORDER_WIDTH.get(element));
    }

    public BorderAttribute(@NotNull Color color, int width) {
        this.color = color;
        this.width = width;
    }

    public void draw(@NotNull Graphics<?> graphics, int x, int y, int width, int height, BoundingBox parentBoundingBox) {
        if (this.width <= 0) {
            return;
        }

        int realX = x;
        int realY = y;
        int realWidth = width;
        int realHeight = height;

        if (realY < parentBoundingBox.y()) {
            realHeight = realHeight - (parentBoundingBox.y() - realY);
            realY = parentBoundingBox.y();
        }

        if ((realY+realHeight) > (parentBoundingBox.y() + parentBoundingBox.height())) {
            realHeight = realHeight - ((realY + realHeight) - (parentBoundingBox.y() + parentBoundingBox.height()));
        }

        if (realX < parentBoundingBox.x()) {
            realWidth = realWidth - (parentBoundingBox.x() - realX);
            realX = parentBoundingBox.x();
        }

        if ((realX + realWidth) > (parentBoundingBox.x() + parentBoundingBox.width())) {
            realWidth = realWidth - ((realX + realWidth) - (parentBoundingBox.x() + parentBoundingBox.width()));
        }


        // top border
        int realStroke = this.width;
        if (y < realY) {
            realStroke = this.width - (realY - y);
        }
        if (realStroke > 0) {
            Color[] rect = new Color[realWidth * realStroke];
            Arrays.fill(rect, color);
            graphics.draw(rect, realWidth, realStroke, realX, realY);
        }

        // bottom border
        realStroke = this.width;
        if ((y + height) > (parentBoundingBox.y() + parentBoundingBox.height())) {
            realStroke = this.width - ((y + height) - (parentBoundingBox.y() + parentBoundingBox.height()));
        }
        if (realStroke > 0) {
            Color[] rect = new Color[realWidth * realStroke];
            Arrays.fill(rect, color);
            graphics.draw(rect, realWidth, realStroke, realX, realY + realHeight);
        }

        // left border
        realStroke = this.width;
        if (x < realX) {
            realStroke = this.width - (realX - x);
        }
        if (realStroke > 0) {
            Color[] rect = new Color[realWidth * realStroke];
            Arrays.fill(rect, color);
            graphics.draw(rect, realStroke, realHeight, realX, realY);
        }

        // right border
        realStroke = this.width;
        if ((x + width) > (parentBoundingBox.x() + parentBoundingBox.width())) {
            realStroke = this.width - ((x + width) - (parentBoundingBox.x() + parentBoundingBox.width()));
        }
        if (realStroke > 0) {
            Color[] rect = new Color[realWidth * realStroke];
            Arrays.fill(rect, color);
            graphics.draw(rect, realStroke, realHeight, realX + realWidth - realStroke, realY);
        }
    }

    public void draw(@NotNull Graphics2D graphics2D, int x, int y, int width, int height) {
        // TODO: 31/07/2023 remove this, check comment in Text.java

        int parentX = x;
        int parentY = y;
        int parentWidth = width;
        int parentHeight = height;

        if (this.width <= 0) {
            return;
        }

        int realX = x;
        int realY = y;
        int realWidth = width;
        int realHeight = height;

        if (realY < parentY) {
            realHeight = realHeight - (parentY - realY);
            realY = parentY;
        }

        if ((realY+realHeight) > (parentY + parentHeight)) {
            realHeight = realHeight - ((realY + realHeight) - (parentY + parentHeight));
        }

        if (realX < parentX) {
            realWidth = realWidth - (parentX - realX);
            realX = parentX;
        }

        if ((realX + realWidth) > (parentX + parentWidth)) {
            realWidth = realWidth - ((realX + realWidth) - (parentX + parentWidth));
        }

        graphics2D.setColor(color);

        // top border
        int realStroke = this.width;
        if (y < realY) {
            realStroke = this.width - (realY - y);
        }
        if (realStroke > 0) {
            graphics2D.fillRect(realX, realY, realWidth, realStroke);
        }

        // bottom border
        realStroke = this.width;
        if ((y + height) > (parentY + parentHeight)) {
            realStroke = this.width - ((y + height) - (parentY + parentHeight));
        }
        if (realStroke > 0) {
            graphics2D.fillRect(realX, realY+realHeight - realStroke, realWidth, realStroke);
        }

        // left border
        realStroke = this.width;
        if (x < realX) {
            realStroke = this.width - (realX - x);
        }
        if (realStroke > 0) {
            graphics2D.fillRect(realX, realY, realStroke, realHeight);
        }

        // right border
        realStroke = this.width;
        if ((x + width) > (parentX + parentWidth)) {
            realStroke = this.width - ((x + width) - (parentX + parentWidth));
        }
        if (realStroke > 0) {
            graphics2D.fillRect(realX + realWidth - realStroke, realY, realStroke, realHeight);
        }
    }

    public @NotNull Color getColor() {
        return color;
    }

    public void setColor(@NotNull Color color) {
        this.color = color;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
