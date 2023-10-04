package me.squidxtv.frameui.core.content;

import me.squidxtv.frameui.core.actions.click.ClickAction;
import me.squidxtv.frameui.core.actions.initiator.ActionInitiator;
import me.squidxtv.frameui.core.actions.scroll.ScrollAction;
import me.squidxtv.frameui.core.actions.scroll.ScrollDirection;
import me.squidxtv.frameui.core.math.BoundingBox;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public abstract class AbstractContent implements Content {

    private final @NotNull String id;

    private @Nullable ClickAction clickAction;
    private @Nullable ScrollAction scrollAction;

    protected AbstractContent(@NotNull String id) {
        this.id = id;
    }

    @Override
    public @NotNull String getId() {
        return id;
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

        optionalScrollAction.get().perform(initiator, direction, scrollX, scrollY);
    }

    protected @NotNull BoundingBox getAbsolutePosition(@NotNull BoundingBox parentBoundingBox) {
        int absoluteX = parentBoundingBox.x() + getX();
        int absoluteY = parentBoundingBox.y() + getY();
        int visibleWidth = Math.min(parentBoundingBox.width() - getX(), getWidth());
        int visibleHeight = Math.min(parentBoundingBox.height() - getY(), getHeight());
        return new BoundingBox(absoluteX, absoluteY, visibleWidth, visibleHeight);
    }

    @Override
    public @NotNull Optional<ClickAction> getClickAction() {
        return Optional.ofNullable(clickAction);
    }

    @Override
    public void setClickAction(@Nullable ClickAction clickAction) {
        this.clickAction = clickAction;
    }

    @Override
    public @NotNull Optional<ScrollAction> getScrollAction() {
        return Optional.ofNullable(scrollAction);
    }

    @Override
    public void setScrollAction(@Nullable ScrollAction scrollAction) {
        this.scrollAction = scrollAction;
    }

}
