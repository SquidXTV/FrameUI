package me.squidxtv.frameui.core.content;

import me.squidxtv.frameui.core.attributes.Attribute;
import me.squidxtv.frameui.core.attributes.BorderAttribute;
import me.squidxtv.frameui.core.graphics.Graphics;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Element;

import java.util.Arrays;
import java.util.List;

public class Div extends AbstractContent {

    private int x;
    private int y;
    private int width;
    private int height;

    private @NotNull BorderAttribute border;
    private final @NotNull List<Content> children;

    public Div(@NotNull Element element) {
        this(Attribute.ID.get(element),
                Attribute.X.get(element),
                Attribute.Y.get(element),
                Attribute.WIDTH.get(element),
                Attribute.HEIGHT.get(element),
                new BorderAttribute(element),
                Content.getChildren(element));
    }

    public Div(@NotNull String id, int x, int y, int width, int height, @NotNull BorderAttribute border, @NotNull Content[] children) {
        super(id);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.border = border;
        this.children = Arrays.asList(children);
    }

    @Override
    public void draw(@NotNull Graphics<?> graphics, int parentX, int parentY, int parentWidth, int parentHeight) {
        int canvasX = parentX + this.x;
        int canvasY = parentY + this.y;
        int visibleWidth = Math.min(parentWidth - this.x, width);
        int visibleHeight = Math.min(parentHeight - this.y, height);

        if (visibleWidth <= 0 || visibleHeight <= 0) {
            return;
        }

        // TODO: 31/07/2023 implement border padding for all borders / make issue for that instead
        for (Content child : children) {
            child.draw(graphics, canvasX, canvasY, visibleWidth, visibleHeight);
        }

        // TODO: 31/07/2023 check if visibleWidth/visibleHeight is needed instead
        border.draw(graphics, canvasX, canvasY, width, height, parentX, parentY, parentWidth, parentHeight);
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public @NotNull List<Content> getChildren() {
        return children;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public @NotNull BorderAttribute getBorder() {
        return border;
    }

    public void setBorder(@NotNull BorderAttribute border) {
        this.border = border;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Div(%s, %d, %d, %d, %d, %s) {%n".formatted(getId(), x, y, width, height, border));
        for (Content child : children) {
            builder.append("\t").append(child).append("\n");
        }
        builder.append("}");
        return builder.toString();
    }
}
