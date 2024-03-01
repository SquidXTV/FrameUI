package me.squidxtv.frameui.api;

import me.squidxtv.frameui.FrameUI;
import me.squidxtv.frameui.exceptions.FontLoadingException;
import org.jetbrains.annotations.NotNull;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

public class FrameAPIImpl implements FrameAPI {

    private static final @NotNull String MINECRAFT_FONT_PATH = "/minecraft_font.ttf";
    private final @NotNull FrameUI plugin;
    private final @NotNull Logger logger;
    private final @NotNull Font minecraftFont;

    public FrameAPIImpl(@NotNull FrameUI plugin) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();

        try (InputStream font = FrameAPIImpl.class.getResourceAsStream(MINECRAFT_FONT_PATH)) {
            if (font == null) {
                plugin.getServer().getPluginManager().disablePlugin(plugin);
                throw new FontLoadingException("Failed to load minecraft_font.ttf. Disabling FrameUI plugin.");
            }
            minecraftFont = Font.createFont(Font.TRUETYPE_FONT, font);
        } catch (IOException | FontFormatException e) {
            plugin.getServer().getPluginManager().disablePlugin(plugin);
            throw new FontLoadingException("Failed to load minecraft_font.ttf. Disabling FrameUI plugin", e);
        }
    }

    @Override
    public @NotNull FrameUI getPlugin() {
        return plugin;
    }

    @Override
    public @NotNull Font getMinecraftFont() {
        return minecraftFont;
    }

    @Override
    public @NotNull Logger getLogger() {
        return logger;
    }

}
