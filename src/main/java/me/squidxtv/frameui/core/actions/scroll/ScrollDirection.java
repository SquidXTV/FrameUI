package me.squidxtv.frameui.core.actions.scroll;

public enum ScrollDirection {

    LEFT(-1),
    RIGHT(1);

    private final int direction;

    ScrollDirection(int direction) {
        this.direction = direction;
    }

}
