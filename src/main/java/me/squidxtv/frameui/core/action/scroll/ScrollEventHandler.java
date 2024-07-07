package me.squidxtv.frameui.core.action.scroll;

import org.bukkit.event.player.PlayerItemHeldEvent;

import java.util.Objects;

/**
 * Functional interface representing a handler for scroll interactions.
 */
public interface ScrollEventHandler {

    /**
     * Performs the scroll interaction.
     * @param event the event that causes this scroll interaction
     * @param direction
     * @param scrollX the x-coordinate on the element
     * @param scrollY the y-coordinate on the element
     */
    void perform(PlayerItemHeldEvent event, ScrollDirection direction, int scrollX, int scrollY);

    /**
     * Returns a composed {@code ScrollEventHandler} that performs, in sequence, this handler followed
     * by the provided {@code after} handler.
     * @param after the handler to perform after this one
     * @return a composed {@code ScrollEventHandler}
     * @throws NullPointerException if the provided {@code after} handler is null
     */
    default ScrollEventHandler andThen(ScrollEventHandler after) {
        Objects.requireNonNull(after);
        return (event, direction, scrollX, scrollY) -> {
            perform(event, direction, scrollX, scrollY);
            after.perform(event, direction, scrollX, scrollY);
        };
    }

    /**
     * Returns an empty {@code ScrollEventHandler} that performs no action.
     * @return an empty scroll event handler
     */
    static ScrollEventHandler empty() {
        return (_, _, _, _) -> {};
    }
}
