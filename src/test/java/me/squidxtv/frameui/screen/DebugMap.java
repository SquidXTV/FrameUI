package me.squidxtv.frameui.screen;

import me.squidxtv.frameui.core.map.Map;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.image.BufferedImage;

public class DebugMap implements Map {

    private final BufferedImage image;

    public DebugMap() {
        image = new BufferedImage(Map.WIDTH, Map.HEIGHT, BufferedImage.TYPE_INT_RGB);
    }

    @Override
    public void update(@NotNull Color pixel, int x, int y) {
        image.setRGB(x, y, pixel.getRGB());
    }

    public BufferedImage getImage() {
        return image;
    }
}
