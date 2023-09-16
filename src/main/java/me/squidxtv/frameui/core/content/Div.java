package me.squidxtv.frameui.core.content;

import me.squidxtv.frameui.core.actions.click.ClickAction;
import me.squidxtv.frameui.core.actions.initiator.ActionInitiator;
import me.squidxtv.frameui.core.actions.scroll.ScrollAction;
import me.squidxtv.frameui.core.actions.scroll.ScrollDirection;
import me.squidxtv.frameui.core.attributes.Attribute;
import me.squidxtv.frameui.core.attributes.BorderAttribute;
import me.squidxtv.frameui.core.graphics.Graphics;
import me.squidxtv.frameui.core.math.BoundingBox;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Element;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Div extends AbstractContent implements Parent {

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
    public void draw(@NotNull Graphics<?> graphics, BoundingBox parentBoundingBox) {
        BoundingBox absolutePosition = getAbsolutePosition(parentBoundingBox);

        if (absolutePosition.width() <= 0 || absolutePosition.height() <= 0) {
            return;
        }

        // TODO: 31/07/2023 implement border padding for all borders / make issue for that instead
        for (Content child : children) {
            child.draw(graphics, absolutePosition);
        }

        // TODO: 31/07/2023 check if visibleWidth/visibleHeight is needed instead
        border.draw(graphics, absolutePosition.x(), absolutePosition.y(), width, height, parentBoundingBox);
    }

    @Override
    public void click(@NotNull ActionInitiator<?> initiator, int clickX, int clickY, BoundingBox parentBoundingBox) {
        Optional<ClickAction> optionalClickAction = getClickAction();
        if (optionalClickAction.isEmpty()) {
            return;
        }

        BoundingBox absolutePosition = getAbsolutePosition(parentBoundingBox);

        if (absolutePosition.width() <= 0 || absolutePosition.height() <= 0) {
            return;
        }

        if(absolutePosition.isPositionOutside(clickX, clickY)) {
            return;
        }

        for (Content child : children) {
            child.click(initiator, clickX, clickY, absolutePosition);
        }
        optionalClickAction.get().perform(initiator, clickX, clickY);
    }

    @Override
    public void scroll(@NotNull ActionInitiator<?> initiator, @NotNull ScrollDirection direction, int scrollX, int scrollY, @NotNull BoundingBox parentBoundingBox) {
        Optional<ScrollAction> optionalScrollAction = getScrollAction();
        if (optionalScrollAction.isEmpty()) {
            return;
        }

        BoundingBox absolutePosition = getAbsolutePosition(parentBoundingBox);

        if (absolutePosition.width() <= 0 || absolutePosition.height() <= 0) {
            return;
        }

        if(absolutePosition.isPositionOutside(scrollX, scrollY)) {
            return;
        }

        for (Content child : children) {
            child.scroll(initiator, direction, scrollX, scrollY, absolutePosition);
        }
        optionalScrollAction.get().perform(initiator, direction, scrollX, scrollY);
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
    public @NotNull List<Content> getChildren() {
        return children;
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
