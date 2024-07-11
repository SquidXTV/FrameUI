package me.squidxtv.frameui.api;

import me.squidxtv.frameui.api.builder.ScreenBuilder;
import me.squidxtv.frameui.api.exceptions.FramesApiException;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicesManager;


public interface FramesApi {
    /**
     * @return true if library has been initialed
     */
    static boolean isInitialized() {
        return getServices().isProvidedFor(ScreenRegistry.class);
    }

    /**
     * Use this method to create new screen
     * <p>
     * code```
     * <p>
     * var screen = FramesApi
     * .newScreen()
     * .withName("Default screen!")
     * .withSize(10, 10)
     * .build();
     * <p>
     * screen.open();
     * ```
     *
     * @return screen creator
     * @see ScreenBuilder
     */
    static ScreenBuilder newScreen() {
        return screens().newScreen();
    }


    /**
     * @return ScreenRegistry object that manage all screens
     * @throws FramesApiException when library has not been initialized
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
