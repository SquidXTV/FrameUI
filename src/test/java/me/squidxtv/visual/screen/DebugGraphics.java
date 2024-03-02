package me.squidxtv.visual.screen;

import me.squidxtv.frameui.core.content.ScreenModel;
import me.squidxtv.frameui.core.graphics.AbstractGraphics;
import me.squidxtv.frameui.core.map.Map;
import org.jetbrains.annotations.NotNull;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Arrays;

public class DebugGraphics extends AbstractGraphics<DebugItemFrame> {

    private final BufferedImage image;

    public DebugGraphics(ScreenModel model) {
        super(model, new DebugItemFrame[model.getBlockWidth() * model.getBlockHeight()]);
        this.image = new BufferedImage(model.getWidth(), model.getHeight(), BufferedImage.TYPE_INT_RGB);
    }

    @Override
    public void open() {
        super.open();
        DebugItemFrame[] maps = getItemFrames();
        for (int i = 0; i < maps.length; i++) {
            maps[i] = new DebugItemFrame();
        }
    }

    @Override
    public void close() {
        super.close();
        Arrays.fill(getItemFrames(), null);
    }

    @Override
    public void terminate() {
        super.terminate();
        Arrays.fill(getItemFrames(), null);
    }

    public void update() {
        Graphics2D graphics = image.createGraphics();

        DebugItemFrame[] frames = getItemFrames();
        for (int i = 0; i < frames.length; i++) {
            DebugItemFrame frame = frames[i];
            // https://softwareengineering.stackexchange.com/a/212813
            int x = i % getModel().getBlockWidth();
            int y = i / getModel().getBlockWidth();
            graphics.drawImage(frame.getMap().getImage(), x * Map.WIDTH, y * Map.HEIGHT, null);
        }

        graphics.dispose();
    }

    public @NotNull BufferedImage getImage() {
        return image;
    }
}
