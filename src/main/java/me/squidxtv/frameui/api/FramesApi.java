package me.squidxtv.frameui.api;

import me.squidxtv.frameui.api.builder.ScreenBuilder;
import me.squidxtv.frameui.api.exceptions.FramesApiException;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicesManager;

/**
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
 */
public interface FramesApi {

    static boolean isInitialized() {
        return getServices().isProvidedFor(ScreenRegistry.class);
    }

    static ScreenBuilder newScreen() {
        return screens().newScreen();
    }

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
