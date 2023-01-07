package me.squidxtv.frameui.core;

import me.squidxtv.frameui.JavaPlugin;
import me.squidxtv.frameui.core.content.ElementNode;
import me.squidxtv.frameui.util.XMLUtil;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.util.Arrays;

public class ScreenModel {

    private static final Color BLACK = new Color(13, 13, 13);

    private final @NotNull String id;

    private final int width;

    private final int height;

    private final BufferedImage background;

    private final ElementNode[] childNodes;

    public ScreenModel(URI xmlFile) throws SAXException, IOException {
        DocumentBuilder documentBuilder = org.bukkit.plugin.java.JavaPlugin.getPlugin(JavaPlugin.class).getDocumentBuilder();

        Document xml = documentBuilder.parse(xmlFile.getPath());
        Element root = xml.getDocumentElement();

        this.id = root.getAttribute("id");
        this.width = Integer.parseInt(root.getAttribute("width"));
        this.height = Integer.parseInt(root.getAttribute("height"));

        String backgroundImagePath = root.getAttribute("background-image");
        if (backgroundImagePath.isEmpty() || backgroundImagePath.isBlank()) {
            this.background = new BufferedImage(width*128, height*128, BufferedImage.TYPE_INT_RGB);
            Graphics g = background.getGraphics();
            g.setColor(BLACK);
            g.drawRect(0, 0, background.getWidth(), background.getHeight());
            g.dispose();
        } else {
            this.background = ImageIO.read(Path.of(backgroundImagePath).toFile());
        }

        this.childNodes = XMLUtil.getChildNodes(root);
    }

    public @NotNull String getId() {
        return id;
    }


    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public BufferedImage getBackground() {
        return background;
    }

    public ElementNode[] getChildNodes() {
        return childNodes;
    }

    @Override
    public String toString() {
        return "ScreenModel{" +
                "id='" + id + '\'' +
                ", width=" + width +
                ", height=" + height +
                ", background=" + background +
                ", childNodes=" + Arrays.toString(childNodes) +
                '}';
    }
}
