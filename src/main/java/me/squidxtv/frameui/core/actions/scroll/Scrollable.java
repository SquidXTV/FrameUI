package me.squidxtv.frameui.core.actions.scroll;

import me.squidxtv.frameui.core.actions.initiator.ActionInitiator;
import me.squidxtv.frameui.core.math.BoundingBox;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public interface Scrollable {

    void scroll(@NotNull ActionInitiator<?> initiator, @NotNull ScrollDirection direction, int scrollX, int scrollY, @NotNull BoundingBox parentBoundingBox);

    void setScrollAction(@Nullable ScrollAction action);
    @NotNull Optional<ScrollAction> getScrollAction();

}
