package me.squidxtv.visual.screen;

import me.squidxtv.frameui.core.map.Map;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.awt.Color;
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

    @Override
    public @NotNull ItemStack getAsItemStack() {
        throw new UnsupportedOperationException("ItemStack is not existing in Debug environment.");
    }

    public BufferedImage getImage() {
        return image;
    }

}
