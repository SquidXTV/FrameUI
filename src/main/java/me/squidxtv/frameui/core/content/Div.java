package me.squidxtv.frameui.core.content;

import me.squidxtv.frameui.core.attributes.Attribute;
import me.squidxtv.frameui.core.attributes.BorderAttribute;
import me.squidxtv.frameui.core.graphics.Graphics;
import me.squidxtv.frameui.core.math.BoundingBox;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Element;

import java.util.List;

public class Div extends ParentContent {

    private int x;
    private int y;
    private int width;
    private int height;

    private @NotNull BorderAttribute border;

    public Div(@NotNull Element element) {
        this(Attribute.ID.get(element),
                Attribute.X.get(element),
                Attribute.Y.get(element),
                Attribute.WIDTH.get(element),
                Attribute.HEIGHT.get(element),
                new BorderAttribute(element),
                Content.getChildren(element));
    }

    public Div(@NotNull String id, int x, int y, int width, int height, @NotNull BorderAttribute border, @NotNull List<Content> children) {
        super(id, children);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.border = border;
    }

    @Override
    public void draw(@NotNull Graphics<?> graphics, BoundingBox parentBoundingBox) {
        BoundingBox absolutePosition = getAbsolutePosition(parentBoundingBox);

        if (absolutePosition.width() <= 0 || absolutePosition.height() <= 0) {
            return;
        }

        // TODO: 31/07/2023 implement border padding for all borders / make issue for that instead
        for (Content child : getChildren()) {
            child.draw(graphics, absolutePosition);
        }

        // TODO: 31/07/2023 check if visibleWidth/visibleHeight is needed instead
        border.draw(graphics, absolutePosition.x(), absolutePosition.y(), width, height, parentBoundingBox);
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

    public @NotNull BorderAttribute getBorder() {
        return border;
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

    public void setBorder(@NotNull BorderAttribute border) {
        this.border = border;
    }

    @Override
    public String toString() {
        return "Div(%s, %d, %d, %d, %d, %s)".formatted(getId(), x, y, width, height, border);
    }
}
