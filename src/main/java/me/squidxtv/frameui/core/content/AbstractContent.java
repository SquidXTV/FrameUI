package me.squidxtv.frameui.core.content;

import me.squidxtv.frameui.core.actions.click.ClickAction;
import me.squidxtv.frameui.core.actions.initiator.ActionInitiator;
import me.squidxtv.frameui.core.actions.scroll.ScrollAction;
import me.squidxtv.frameui.core.actions.scroll.ScrollDirection;
import me.squidxtv.frameui.core.attributes.BorderAttribute;
import me.squidxtv.frameui.core.attributes.Insets;
import me.squidxtv.frameui.core.graphics.Graphics;
import me.squidxtv.frameui.core.math.BoundingBox;
import org.jetbrains.annotations.NotNull;

import java.awt.Color;
import java.util.Arrays;

public abstract class AbstractContent implements Content, Bordered {

    private final @NotNull String id;

    private @NotNull BorderAttribute borderAttribute;

    private @NotNull ClickAction clickAction;
    private @NotNull ScrollAction scrollAction;

    protected AbstractContent(@NotNull String id, @NotNull BorderAttribute borderAttribute) {
        this.id = id;
        this.borderAttribute = borderAttribute;
        this.clickAction = ClickAction.empty();
        this.scrollAction = ScrollAction.empty();
    }

    @Override
    public void click(@NotNull ActionInitiator initiator, int clickX, int clickY, BoundingBox parentBoundingBox) {
        BoundingBox absolutePosition = getAbsolutePosition(parentBoundingBox);

        if (absolutePosition.width() <= 0 || absolutePosition.height() <= 0) {
            return;
        }

        if (absolutePosition.isPositionOutside(clickX, clickY)) {
            return;
        }

        clickAction.perform(initiator, clickX, clickY);
    }

    @Override
    public void scroll(@NotNull ActionInitiator initiator, @NotNull ScrollDirection direction, int scrollX, int scrollY, @NotNull BoundingBox parentBoundingBox) {
        BoundingBox absolutePosition = getAbsolutePosition(parentBoundingBox);

        if (absolutePosition.width() <= 0 || absolutePosition.height() <= 0) {
            return;
        }

        if (absolutePosition.isPositionOutside(scrollX, scrollY)) {
            return;
        }

        scrollAction.perform(initiator, direction, scrollX, scrollY);
    }

    @Override
    public void drawBorder(@NotNull Graphics<?> graphics, int x, int y, int width, int height, BoundingBox parentBoundingBox) {
        Insets borderSizes = borderAttribute.insets();
        if (borderSizes.north() == 0 || borderSizes.south() == 0 || borderSizes.east() == 0 || borderSizes.west() == 0) {
            return;
        }

        int borderX = x;
        int borderY = y;
        int borderWidth = width;
        int borderHeight = height;

        if (y < parentBoundingBox.y()) {
            borderHeight -= (parentBoundingBox.y() - borderY);
            borderY = parentBoundingBox.y();
        }

        if ((borderY + borderHeight) > (parentBoundingBox.y() + parentBoundingBox.height())) {
            borderHeight -= ((borderY + borderHeight) - (parentBoundingBox.y() + parentBoundingBox.height()));
        }

        if (borderX < parentBoundingBox.x()) {
            borderWidth -= (parentBoundingBox.x() - borderX);
            borderX = parentBoundingBox.x();
        }

        if ((borderX + borderWidth) > (parentBoundingBox.x() + parentBoundingBox.width())) {
            borderWidth -= ((borderX + borderWidth) - (parentBoundingBox.x()) + parentBoundingBox.width());
        }

        // north border
        int borderSize = borderSizes.north();
        if (y < borderY) {
            borderSize -= (borderY - y);
        }

        if (borderSize > 0) {
            Color[] border = new Color[borderWidth * borderSize];
            Arrays.fill(border, borderAttribute.color());
            graphics.draw(border, borderWidth, borderSize, borderX, borderY);
        }


        // south border
        borderSize = borderSizes.south();
        if ((y + height) > (parentBoundingBox.y() + parentBoundingBox.height())) {
            borderSize -= ((y + height) - (parentBoundingBox.y() + parentBoundingBox.height()));
        }
        if (borderSize > 0) {
            Color[] rect = new Color[borderWidth * borderSize];
            Arrays.fill(rect, borderAttribute.color());
            int drawingY = (y + height - borderSizes.south());
            graphics.draw(rect, borderWidth, borderSize, borderX, drawingY);
        }

        // west border
        borderSize = borderSizes.west();
        if (x < borderX) {
            borderSize -= (borderX - x);
        }
        if (borderSize > 0) {
            Color[] rect = new Color[borderHeight * borderSize];
            Arrays.fill(rect, borderAttribute.color());
            graphics.draw(rect, borderSize, borderHeight, borderX, borderY);
        }

        // east border
        borderSize = borderSizes.east();
        if ((x + width) > (parentBoundingBox.x() + parentBoundingBox.width())) {
            borderSize -= ((x + width) - (parentBoundingBox.x() + parentBoundingBox.width()));
        }
        if (borderSize > 0) {
            Color[] rect = new Color[borderHeight * borderSize];
            Arrays.fill(rect, borderAttribute.color());
            int drawingX = (x + width - borderSizes.east());
            graphics.draw(rect, borderSize, borderHeight, drawingX, borderY);
        }
    }

    @Override
    public void setBorderAttribute(@NotNull BorderAttribute borderAttribute) {
        this.borderAttribute = borderAttribute;
    }

    @Override
    public @NotNull BorderAttribute getBorderAttribute() {
        return borderAttribute;
    }

    @Override
    public @NotNull String getId() {
        return id;
    }

    protected @NotNull BoundingBox getAbsolutePosition(@NotNull BoundingBox parentBoundingBox) {
        int absoluteX = parentBoundingBox.x() + getX();
        int absoluteY = parentBoundingBox.y() + getY();
        int visibleWidth = Math.min(parentBoundingBox.width() - getX(), getWidth());
        int visibleHeight = Math.min(parentBoundingBox.height() - getY(), getHeight());
        return new BoundingBox(absoluteX, absoluteY, visibleWidth, visibleHeight);
    }

    @Override
    public void setClickAction(@NotNull ClickAction clickAction) {
        this.clickAction = clickAction;
    }

    @Override
    public @NotNull ClickAction getClickAction() {
        return clickAction;
    }

    @Override
    public void setScrollAction(@NotNull ScrollAction scrollAction) {
        this.scrollAction = scrollAction;
    }

    @Override
    public @NotNull ScrollAction getScrollAction() {
        return scrollAction;
    }

    @Override
    public String toString() {
        return "AbstractContent{" +
                "id='" + id + '\'' +
                ", borderAttribute=" + borderAttribute +
                ", clickAction=" + clickAction +
                ", scrollAction=" + scrollAction +
                '}';
    }
}
