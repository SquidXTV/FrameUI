package me.squidxtv.frameui.core.graphics;

import me.squidxtv.frameui.core.itemframe.ItemFrame;
import org.jetbrains.annotations.NotNull;

import java.awt.Color;
import java.awt.image.BufferedImage;

public interface Graphics<I extends ItemFrame<?>> {

    void open();

    void close();

    void terminate();

    void update();

    void draw(@NotNull BufferedImage image);

    void draw(@NotNull BufferedImage image, int width, int height, int offsetX, int offsetY);

    void draw(@NotNull BufferedImage image, int offsetX, int offsetY);

    void drawWithDimensions(@NotNull BufferedImage image, int width, int height);

    void draw(@NotNull Color[] pixels, int width, int height);

    void draw(@NotNull Color[] pixels, int width, int height, int offsetX, int offsetY);

    void draw(int[] pixels, int width, int height);

    void draw(int[] pixels, int width, int height, int offsetX, int offsetY);


    @NotNull I[] getItemFrames();

}
