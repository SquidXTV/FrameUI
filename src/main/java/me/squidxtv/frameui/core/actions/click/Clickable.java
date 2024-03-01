package me.squidxtv.frameui.core.actions.click;

import me.squidxtv.frameui.core.actions.initiator.ActionInitiator;
import me.squidxtv.frameui.core.math.BoundingBox;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public interface Clickable {

    void click(@NotNull ActionInitiator initiator, int clickX, int clickY, BoundingBox parentBoundingBox);

    void setClickAction(@Nullable ClickAction clickAction);

    @NotNull Optional<ClickAction> getClickAction();

}
