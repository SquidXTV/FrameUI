package me.squidxtv.frameui.core;

/**
 * The {@code Renderer} is responsible for updating the content of a
 * {@link Screen}.
 */
@FunctionalInterface
public interface Renderer {

    /**
     * Updates the content of the specified {@link Screen}.
     * 
     * @param screen the screen to update
     */
    void update(Screen screen);

}
