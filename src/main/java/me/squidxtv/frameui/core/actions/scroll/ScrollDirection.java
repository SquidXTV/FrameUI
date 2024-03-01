package me.squidxtv.frameui.core.actions.scroll;

public enum ScrollDirection {

    UP(-1),
    DOWN(1);

    private final int direction;

    ScrollDirection(int direction) {
        this.direction = direction;
    }


    public static ScrollDirection getDirection(int slotDirection) {
        if (slotDirection == -1) {
            return ScrollDirection.UP;
        } else if (slotDirection == 1) {
            return ScrollDirection.DOWN;
        }
        throw new IllegalArgumentException("Provided slot direction must be either 1 or -1.");
    }

}
