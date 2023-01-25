package me.squidxtv.frameui.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Utility class for {@link BufferedImage}.
 */
public final class BufferedImageUtil {

    private BufferedImageUtil() {}

    /**
     * Splits {@link BufferedImage} in a matrix, using width and height of each split.
     * @param original Image to split
     * @param width Width of part
     * @param height Height of part
     * @return 2D array of images
     */
    public static BufferedImage[][] split(BufferedImage original, int width, int height) {
        BufferedImage[][] parts = new BufferedImage[height][width];
        int heightPerPart = original.getHeight() / height;
        int widthPerPart = original.getWidth() / width;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                parts[i][j] = original.getSubimage((j * widthPerPart), (i * heightPerPart),
                        widthPerPart, heightPerPart);
            }
        }
        return parts;
    }

    /**
     * Generates a copy of given {@link BufferedImage}.
     * @param original Image to copy
     * @return copy of original Image
     */
    public static BufferedImage deepCopy(BufferedImage original) {
        ColorModel model = original.getColorModel();
        boolean premultiplied = original.isAlphaPremultiplied();
        WritableRaster raster =
                original.copyData(original.getRaster().createCompatibleWritableRaster());
        return new BufferedImage(model, raster, premultiplied, null);
    }


    /**
     * Resizes {@link BufferedImage} to new width and height.
     * @implNote this method uses {@link Image.SCALE_SMOOTH}
     * @param image BufferedImage
     * @param width new width
     * @param height new height
     * @return resized Image
     */
    public static BufferedImage resize(BufferedImage image, int width, int height) {
        Image tmp = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage newSize = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = newSize.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return newSize;
    }

    /**
     * Checks if both {@link BufferedImage} are equal.
     * @param a first Image
     * @param b second Image
     * @return true if all pixel are equal or both null
     */
    public static boolean deepEquals(@Nullable BufferedImage a, @Nullable BufferedImage b) {
        if (a == b) {
            return true;
        } else if (a == null || b == null) {
            return false;
        }

        if (a.getWidth() != b.getWidth() || a.getHeight() != b.getHeight()) {
            return false;
        }

        int width = a.getWidth();
        int height = a.getHeight();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (a.getRGB(x, y) != b.getRGB(x, y)) {
                    return false;
                }
            }
        }

        return true;
    }

}
