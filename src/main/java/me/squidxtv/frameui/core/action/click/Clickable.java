package me.squidxtv.frameui.core.action;

public interface Clickable {

    void click(int clickX, int clickY);

    void set(ClickCallback callback);

}
