package me.squidxtv.frameui.core.content;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Element;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;

public class Img extends ElementNode {

    private int x;
    private int y;
    private int width;
    private int height;
    private @NotNull BufferedImage image;

    public Img(String id, int x, int y, int width, int height, BufferedImage image) {
        super(id);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.image = image;
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(image, x, y, width, height, null);
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

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    @Contract("_ -> new")
    public static Img of(Element element) {
        String id = element.getAttribute("id");
        int x = Integer.parseInt(element.getAttribute("x"));
        int y = Integer.parseInt(element.getAttribute("y"));
        int width = Integer.parseInt(element.getAttribute("width"));
        int height = Integer.parseInt(element.getAttribute("height"));
        String imagePath = element.getAttribute("path");
        BufferedImage image;
        try {
            image = ImageIO.read(Path.of(imagePath).toFile());
        } catch (IOException e) {
            throw new RuntimeException(e); // TODO: 25/01/2023 throw custom exception 
        }
        return new Img(id, x, y, width, height, image);
    }
}
