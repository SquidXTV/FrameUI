package me.squidxtv.frameui.core.action.scroll;

/**
 * The {@code ScrollDirection} enum represents the two possible scroll
 * directions, {@code UP} and {@code DOWN}. Each is associated with an integer
 * value.
 */
public enum ScrollDirection {

    /**
     * Indicates a scroll action in the upward direction.
     */
    UP(-1),
    /**
     * Indicates a scroll action in the downward direction.
     */
    DOWN(1);

    private final int direction;

    ScrollDirection(int direction) {
        this.direction = direction;
    }

    /**
     * Returns the {@code ScrollDirection} corresponding to the given integer value.
     *
     * @param direction the integer value representing the scroll direction
     * @return the {@code ScrollDirection} corresponding to the given direction
     * @throws IllegalArgumentException if the provided direction is invalid
     */
    public static ScrollDirection getDirection(int direction) {
        for (ScrollDirection value : values()) {
            if (value.direction == direction) {
                return value;
            }
        }
        throw new IllegalArgumentException("Invalid direction: " + direction);
    }

}
