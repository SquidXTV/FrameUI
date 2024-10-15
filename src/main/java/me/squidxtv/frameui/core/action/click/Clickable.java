package me.squidxtv.frameui.core.action.click;

import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Represents an element that can be clicked.
 */
public interface Clickable {

    /**
     * Performs a click on the element.
     * 
     * @param event  the event that causes this click interaction
     * @param clickX the x-coordinate on the element
     * @param clickY the y-coordinate on the element
     */
    default void click(PlayerInteractEvent event, int clickX, int clickY) {
        getOnClick().perform(event, clickX, clickY);
    }

    /**
     * Returns the current {@link ClickEventHandler} for this element.
     * 
     * @return the current {@link ClickEventHandler} for this element
     */
    ClickEventHandler getOnClick();

    /**
     * Sets the current {@link ClickEventHandler} for this element
     * 
     * @param clickEventHandler the new click event handler
     */
    void setOnClick(ClickEventHandler clickEventHandler);

}
