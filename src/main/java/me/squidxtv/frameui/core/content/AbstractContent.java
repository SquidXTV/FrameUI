package me.squidxtv.frameui.core.content;

import me.squidxtv.frameui.core.actions.click.ClickAction;
import me.squidxtv.frameui.core.actions.initiator.ActionInitiator;
import me.squidxtv.frameui.core.actions.scroll.ScrollAction;
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
    public boolean click(@NotNull ActionInitiator<?> initiator, int x, int y) {
        if (clickAction == null) {
            return true;
        }

        clickAction.perform(initiator, x, y);
        return true;
    }

    @Override
    public boolean scroll(@NotNull ActionInitiator<?> initiator, @NotNull ScrollDirection direction, int x, int y) {
        if (scrollAction == null) {
            return true;
        }

        scrollAction.perform(initiator, direction, x, y);
        return true;
    }

    @Override
    public @NotNull String getId() {
        return id;
    }

    public Optional<ClickAction> getClickAction() {
        return Optional.ofNullable(clickAction);
    }

    public void setClickAction(@Nullable ClickAction clickAction) {
        this.clickAction = clickAction;
    }

    public Optional<ScrollAction> getScrollAction() {
        return Optional.ofNullable(scrollAction);
    }

    public void setScrollAction(@Nullable ScrollAction scrollAction) {
        this.scrollAction = scrollAction;
    }

}
