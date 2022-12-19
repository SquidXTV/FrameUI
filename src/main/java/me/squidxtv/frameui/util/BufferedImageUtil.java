package me.squidxtv.frameui.util;

import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class BufferedImageUtil {

    private BufferedImageUtil() {}

    /**
     * Splits {@link BufferedImage} in a matrix, using width and height of each split.
     * @param original BufferedImage to split
     * @param width width of part
     * @param height height of part
     * @return BufferedImage 2d Array
     */
    public static @NotNull BufferedImage[][] split(@NotNull BufferedImage original, int width, int height) {
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
     * Creates and returns deepCopy of given {@link BufferedImage}.
     * @param original BufferedImage to copy
     * @return copy of original
     */
    public static @NotNull BufferedImage deepCopy(@NotNull BufferedImage original) {
        ColorModel model = original.getColorModel();
        boolean premultiplied = original.isAlphaPremultiplied();
        WritableRaster raster =
                original.copyData(original.getRaster().createCompatibleWritableRaster());
        return new BufferedImage(model, raster, premultiplied, null);
    }

    /**
     * Loads {@link BufferedImage} from given Path.
     * @param path Path of BufferedImage
     * @return loaded BufferedImage
     * @throws IOException if {@link ImageIO#read(InputStream)} fails
     */
    public static @NotNull BufferedImage loadFromPath(@NotNull String path) throws IOException {
        try (InputStream in = BufferedImageUtil.class.getResourceAsStream(path)) {
            if (in == null) {
                throw new FileNotFoundException();
            }
            return ImageIO.read(in);
        }
    }

    /**
     * Resizes {@link BufferedImage} to new width and height.
     * @param image BufferedImage
     * @param width new width
     * @param height new height
     * @return resized BufferedImage
     */
    public static @NotNull BufferedImage resize(@NotNull BufferedImage image, int width, int height) {
        Image tmp = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage newSize = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = newSize.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return newSize;
    }

    /**
     * Returns best fitting width for given BufferedImage.
     * @param image BufferedImage
     * @return best fitting width
     */
    public static int getBestWidth(@NotNull BufferedImage image) {
        return (int) Math.round(image.getWidth() / 128.0);
    }

    /**
     * Returns best fitting height for given BufferedImage.
     * @param image BufferedImage
     * @return best fitting height
     */
    public static int getBestHeight(@NotNull BufferedImage image) {
        return (int) Math.round(image.getHeight() / 128.0);
    }

    /**
     * Returns true if both images are equal.
     * @param a first {@link BufferedImage}
     * @param b second {@link BufferedImage}
     * @return true if equal
     */
    public static boolean deepEquals(BufferedImage a, BufferedImage b) {
        if (a == null && b == null) {
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
