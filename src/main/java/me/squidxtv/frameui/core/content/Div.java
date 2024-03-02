package me.squidxtv.frameui.core.content;

import me.squidxtv.frameui.core.attributes.Attribute;
import me.squidxtv.frameui.core.attributes.BorderAttribute;
import me.squidxtv.frameui.core.graphics.Graphics;
import me.squidxtv.frameui.core.math.BoundingBox;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Element;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;

public class Div extends ParentContent {

    private int x;
    private int y;
    private int width;
    private int height;

    private @NotNull Color backgroundColor;

    public Div(@NotNull Element element) {
        this(Attribute.ID.get(element),
                Attribute.X.get(element),
                Attribute.Y.get(element),
                Attribute.WIDTH.get(element),
                Attribute.HEIGHT.get(element),
                Attribute.BACKGROUND_COLOR.get(element),
                new BorderAttribute(element),
                Content.getChildren(element));
    }

    public Div(@NotNull String id,
               int x, int y,
               int width, int height,
               @NotNull Color backgroundColor,
               @NotNull BorderAttribute border,
               @NotNull List<Content> children) {
        super(id, border, children);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.backgroundColor = backgroundColor;
    }

    @Override
    public void draw(@NotNull Graphics<?> graphics, @NotNull BoundingBox parentBoundingBox) {
        BoundingBox absolutePosition = getAbsolutePosition(parentBoundingBox);

        if (absolutePosition.width() <= 0 || absolutePosition.height() <= 0) {
            return;
        }

        Color[] background = new Color[absolutePosition.width() * absolutePosition.height()];
        Arrays.fill(background, backgroundColor);
        graphics.draw(background, absolutePosition.width(), absolutePosition.height(), absolutePosition.x(), absolutePosition.y());

        for (Content child : getChildren()) {
            child.draw(graphics, absolutePosition);
        }

        drawBorder(graphics, absolutePosition.x(), absolutePosition.y(), width, height, parentBoundingBox);
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    public @NotNull Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setBackgroundColor(@NotNull Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    @Override
    public String toString() {
        return "Div{" +
                "id=" + getId() +
                ", x=" + x +
                ", y=" + y +
                ", width=" + width +
                ", height=" + height +
                ", backgroundColor=" + backgroundColor +
                ", border=" + getBorderAttribute() +
                '}';
    }
}
