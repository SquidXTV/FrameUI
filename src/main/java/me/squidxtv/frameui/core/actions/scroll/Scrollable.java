package me.squidxtv.frameui.core.actions.scroll;

import me.squidxtv.frameui.core.actions.initiator.ActionInitiator;
import me.squidxtv.frameui.core.math.BoundingBox;
import org.jetbrains.annotations.NotNull;

public interface Scrollable {

    void scroll(@NotNull ActionInitiator<?> initiator, @NotNull ScrollDirection direction, int scrollX, int scrollY, @NotNull BoundingBox parentBoundingBox);

}
