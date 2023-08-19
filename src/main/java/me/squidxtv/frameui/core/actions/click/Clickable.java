package me.squidxtv.frameui.core.actions.click;

import me.squidxtv.frameui.core.actions.initiator.ActionInitiator;
import org.jetbrains.annotations.NotNull;

public interface Clickable {

    boolean click(@NotNull ActionInitiator<?> initiator, int x, int y);

}
