package me.squidxtv.frameui.core.content;

import me.squidxtv.frameui.core.actions.ClickAction;
import me.squidxtv.frameui.core.actions.ScrollAction;
import org.bukkit.entity.Player;
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
    public boolean click(@NotNull Player player, int x, int y) {
        return clickAction != null;
    }

    @Override
    public boolean scroll(@NotNull Player player, @NotNull ScrollDirection direction, int x, int y) {
        return scrollAction != null;
    }

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
