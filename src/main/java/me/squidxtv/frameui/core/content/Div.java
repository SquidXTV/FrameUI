package me.squidxtv.frameui.core.content;

import me.squidxtv.frameui.FrameUI;
import me.squidxtv.frameui.util.XMLUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Element;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.Logger;

public class Div extends ElementNode {

    private static final Logger LOGGER = org.bukkit.plugin.java.JavaPlugin.getPlugin(FrameUI.class).getLogger();

    private final int x;
    private final int y;

    private final int width;
    private final int height;

    private @Nullable BufferedImage background;
    private final @NotNull ElementNode[] childNodes;

    public Div(String id, int x, int y, int width, int height, @Nullable BufferedImage background, ElementNode[] childNodes) {
        super(id);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.background = background;
        this.childNodes = childNodes;
    }

    @Override
    public void draw(Graphics g) {
        if (background != null) {
            g.drawImage(background, x, y, width, height, null);
        }
        for (ElementNode child : childNodes) {
            child.draw(g);
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public @Nullable BufferedImage getBackground() {
        return background;
    }

    public void setBackground(@Nullable BufferedImage background) {
        this.background = background;
    }

    @Contract("_ -> new")
    public static Div of(Element element) {
        String id = element.getAttribute("id");
        int x = Integer.parseInt(element.getAttribute("x"));
        int y = Integer.parseInt(element.getAttribute("y"));
        int width = Integer.parseInt(element.getAttribute("width"));
        int height = Integer.parseInt(element.getAttribute("height"));

        String backgroundPath = element.getAttribute("background-image");
        BufferedImage background = null;
        if (!(backgroundPath.isEmpty() || backgroundPath.isBlank())) {
            try {
                background = ImageIO.read(Path.of(backgroundPath).toFile());
            } catch (IOException e) {
                LOGGER.warning("Loading background image for div " + id + " failed with path " + backgroundPath + ".");
            }
        }

        return new Div(id, x, y, width, height, background, XMLUtil.getChildNodes(element));
    }
}