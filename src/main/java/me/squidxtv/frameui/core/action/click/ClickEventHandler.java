package me.squidxtv.frameui.core.action;

@FunctionalInterface
public interface ClickCallback {

    void perform(int clickX, int clickY);

    static ClickCallback empty() {
        return (_, _) -> {};
    }

}
