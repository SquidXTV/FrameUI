package me.squidxtv.frameui.core.actions.click;

import me.squidxtv.frameui.core.actions.initiator.ActionInitiator;
import me.squidxtv.frameui.core.math.BoundingBox;
import org.jetbrains.annotations.NotNull;

public interface Clickable {

    void click(@NotNull ActionInitiator initiator, int clickX, int clickY, BoundingBox parentBoundingBox);

    void setClickAction(@NotNull ClickAction clickAction);

    @NotNull ClickAction getClickAction();

}
