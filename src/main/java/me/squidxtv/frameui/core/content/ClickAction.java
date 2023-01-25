package me.squidxtv.frameui.core.content;

@FunctionalInterface
public interface ClickAction {

    void onClick(ElementNode node, int clickX, int clickY);

}
