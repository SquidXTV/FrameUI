package me.squidxtv.visual.screen;

import me.squidxtv.frameui.core.graphics.AbstractGraphics;
import me.squidxtv.frameui.core.map.Map;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;

public class DebugGraphics extends AbstractGraphics<DebugMap> {

    private final BufferedImage image = new BufferedImage(getPixelWidth(), getPixelHeight(), BufferedImage.TYPE_INT_RGB);

    public DebugGraphics(int width, int height) {
        super(new DebugMap[width * height], width, height);
    }

    @Override
    public void open() {
        super.open();
        DebugMap[] maps = getMaps();
        for (int i = 0; i < maps.length; i++) {
            maps[i] = new DebugMap();
        }
    }

    @Override
    public void close() {
        super.close();
        Arrays.fill(getMaps(), null);
    }

    @Override
    public void remove() {
        super.remove();
        Arrays.fill(getMaps(), null);
    }

    public void update() {
        Graphics2D graphics = image.createGraphics();

        DebugMap[] maps = getMaps();
        for (int i = 0; i < maps.length; i++) {
            DebugMap map = maps[i];
            // https://softwareengineering.stackexchange.com/a/212813
            int x = i % getWidth();
            int y = i / getWidth();
            graphics.drawImage(map.getImage(), x * Map.WIDTH, y * Map.HEIGHT, null);
        }

        graphics.dispose();
    }

    public @NotNull BufferedImage getImage() {
        return image;
    }
}
