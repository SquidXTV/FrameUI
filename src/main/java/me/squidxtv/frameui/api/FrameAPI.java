package me.squidxtv.frameui.api;

import me.squidxtv.frameui.FrameUI;
import org.jetbrains.annotations.NotNull;

import java.awt.Font;
import java.util.logging.Logger;

public interface FrameAPI {

    @NotNull Logger getLogger();

    @NotNull FrameUI getPlugin();

    @NotNull Font getMinecraftFont();

}
