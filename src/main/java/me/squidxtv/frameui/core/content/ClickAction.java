package me.squidxtv.frameui.core.content;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface ClickAction {

    void onClick(@NotNull ElementNode node, int clickX, int clickY);

}
