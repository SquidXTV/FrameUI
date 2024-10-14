package me.squidxtv.frameui.core.action.scroll;

import org.bukkit.event.player.PlayerItemHeldEvent;

/**
 * Represents an element that can be scrolled.
 */
public interface Scrollable {

    /**
     * Performs a scroll on the element.
     * @param event the event that causes this scroll interaction
     * @param direction the direction of the scroll
     * @param scrollX the x-coordinate on the element
     * @param scrollY the y-coordinate on the element
     */
    default void scroll(PlayerItemHeldEvent event, ScrollDirection direction, int scrollX, int scrollY) {
        getOnScroll().perform(event, direction, scrollX, scrollY);
    }

    /**
     * Returns the current {@link ScrollEventHandler} for this element.
     * @return the current {@link ScrollEventHandler} for this element
     */
    ScrollEventHandler getOnScroll();

    /**
     * Sets the current {@link ScrollEventHandler} for this element
     * @param scrollEventHandler the new scroll event handler
     */
    void setOnScroll(ScrollEventHandler scrollEventHandler);

}
