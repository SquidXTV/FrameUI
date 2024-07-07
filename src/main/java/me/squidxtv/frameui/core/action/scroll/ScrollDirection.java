package me.squidxtv.frameui.core.action.scroll;

public enum ScrollDirection {

    UP(-1),
    DOWN(1);

    private final int direction;

    ScrollDirection(int direction) {
        this.direction = direction;
    }

    public static ScrollDirection getDirection(int direction) {
        for (ScrollDirection value : values()) {
            if (value.direction == direction) {
                return value;
            }
        }
        throw new IllegalArgumentException("Invalid direction: " + direction);
    }

}
