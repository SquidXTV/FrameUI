package me.squidxtv.frameui.core;

/**
 * The {@code Renderer} is responsible for updating the display of a {@link Screen}.
 */
@FunctionalInterface
public interface Renderer {

    /**
     * Called to update the display of a given {@link Screen}.
     * @param screen the screen to get updated
     */
    void update(Screen screen);

}
