/*
 * FrameUI: Minecraft plugin library designed to easily create screens within a server.
 * Copyright (C) 2023-2024 Connor Schweighöfer
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package me.squidxtv.frameui.core.impl;

import me.squidxtv.frameui.core.ItemFrame;
import me.squidxtv.frameui.core.MapItem;
import me.squidxtv.frameui.core.Renderer;
import me.squidxtv.frameui.core.Screen;

import java.awt.image.BufferedImage;
import java.util.Objects;

/**
 * The {@code BufferedImageRenderer} uses a {@link BufferedImage} to display an
 * image onto a {@link Screen}.
 */
public class BufferedImageRenderer implements Renderer {

    private BufferedImage image;

    /**
     * Creates a new {@code BufferedImageRenderer} with the specified image.
     * 
     * @param image the {@link BufferedImage} to render
     * @throws NullPointerException if {@code image} is null
     */
    public BufferedImageRenderer(BufferedImage image) {
        Objects.requireNonNull(image, "Image cannot be null");
        this.image = image;
    }

    @Override
    public void update(Screen screen) {
        ItemFrame[][] frames = screen.getItemFrames();
        for (int i = 0; i < frames.length; i++) {
            for (int j = 0; j < frames[i].length; j++) {
                BufferedImage sub = image.getSubimage(j * MapItem.WIDTH, i * MapItem.HEIGHT, MapItem.WIDTH, MapItem.HEIGHT);
                MapItem map = frames[i][j].getMapItem();
                int[] rgb = sub.getRGB(0, 0, MapItem.WIDTH, MapItem.HEIGHT, null, 0, MapItem.WIDTH);
                map.draw(rgb);
            }
        }
    }

    /**
     * Returns the image of this renderer.
     * 
     * @return the image of this renderer
     */
    public BufferedImage getImage() {
        return image;
    }

    /**
     * Sets the image of this renderer.
     * 
     * @param image the new image
     * @throws NullPointerException if {@code image} is null
     */
    public void setImage(BufferedImage image) {
        Objects.requireNonNull(image, "Image cannot be null");
        this.image = image;
    }

}
