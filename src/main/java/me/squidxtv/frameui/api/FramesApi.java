package me.squidxtv.frameui.api;

import me.squidxtv.frameui.api.builder.ScreenBuilder;
import me.squidxtv.frameui.api.exceptions.FramesApiException;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicesManager;

/**
 * FramesApi provides methods to interact with the screen management system within the plugin.
 * It allows for the creation and management of screen objects.
 */
public interface FramesApi {
    /**
     * Checks if the Frames API library has been initialized.
     *
     * @return true if the library has been initialized, false otherwise.
     */
    static boolean isInitialized() {
        return getServices().isProvidedFor(ScreenRegistry.class);
    }

    /**
     * Creates a new {@link ScreenBuilder} instance, which is a flexible creator for new {@link me.squidxtv.frameui.core.Screen} objects.
     * <p>
     * Example usage:
     * <pre>
     * {@code
     * public void example(ScreenRegistry registry) {
     *     var player = Bukkit.getPlayer("mike");
     *     var screen = FramesApi.newScreen()
     *             .withSize(10, 10)
     *             .withViewer(player)
     *             .withName("Main Menu Screen")
     *             .withLocation(player.getLocation())
     *             .build();
     *
     *     screen.open();
     * }
     * }
     * </pre>
     *
     * @return a new instance of ScreenBuilder.
     * @throws FramesApiException if the library has not been initialized.
     * @see ScreenBuilder
     */
    static ScreenBuilder newScreen() {
        return screens().newScreen();
    }


    /**
     * Returns the {@link ScreenRegistry} instance that manages all screens.
     *
     * @return the ScreenRegistry instance that manages all screens.
     * @throws FramesApiException if the library has not been initialized.
     * @see ScreenRegistry
     */
    static ScreenRegistry screens() {
        if (!isInitialized()) {
            throw new FramesApiException("Frames Api has not been initialized yet!");
        }
        return getServices().load(ScreenRegistry.class);
    }

    private static ServicesManager getServices() {
        return Bukkit.getServer().getServicesManager();
    }

}
