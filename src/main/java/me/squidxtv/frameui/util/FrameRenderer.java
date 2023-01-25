package me.squidxtv.frameui.util;

import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.jetbrains.annotations.NotNull;

import java.awt.image.*;

/**
 * Renderer used by {@link me.squidxtv.frameui.core.Screen}.
 */
public final class FrameRenderer extends MapRenderer {

    private BufferedImage image;
    private boolean done = true;
    private final Object lock = new Object();

    @Override
    public void render(MapView view, MapCanvas canvas, Player player) {
        synchronized (lock) {
            if (done) {
                return;
            }

            canvas.drawImage(0, 0, image);
            view.setTrackingPosition(false);
            done = true;
        }
    }

    /**
     * Sets Image on this Map.
     * @param image new Image
     */
    public void setImage(BufferedImage image) {
        synchronized (lock) {
            this.image = image;
            this.done = false;
        }
    }

}