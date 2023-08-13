package me.squidxtv.frameui.core.graphics;

import me.squidxtv.frameui.core.map.Map;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.image.BufferedImage;

public interface Graphics<M extends Map> {

    void open();
    void close();
    void remove();

    void draw(@NotNull BufferedImage image);
    void draw(@NotNull BufferedImage image, int width, int height, int offsetX, int offsetY);
    void draw(@NotNull BufferedImage image, int offsetX, int offsetY);
    void drawWithDimensions(@NotNull BufferedImage image, int width, int height);

    void draw(@NotNull Color[] pixels, int width, int height);
    void draw(@NotNull Color[] pixels, int width, int height, int offsetX, int offsetY);

    void draw(int[] pixels, int width, int height);
    void draw(int[] pixels, int width, int height, int offsetX, int offsetY);


    @NotNull M[] getMaps();
}
