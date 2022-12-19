package com.squidxtv.frameui.util;

import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.jetbrains.annotations.NotNull;

import java.awt.image.*;

public final class FrameRenderer extends MapRenderer {

    private BufferedImage image;
    private boolean done = true;
    private final Object lock = new Object();

    @Override
    public void render(@NotNull MapView view, @NotNull MapCanvas canvas, @NotNull Player player) {
        synchronized (lock) {
            if (done) {
                return;
            }

            canvas.drawImage(0, 0, image);
            view.setTrackingPosition(false);
            done = true;
        }
    }

    public void setImage(@NotNull BufferedImage image) {
        synchronized (lock) {
            this.image = image;
            this.done = false;
        }
    }

}