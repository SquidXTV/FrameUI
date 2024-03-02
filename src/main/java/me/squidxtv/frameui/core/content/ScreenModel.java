package me.squidxtv.frameui.core.content;

import me.squidxtv.frameui.api.parser.ScreenParser;
import me.squidxtv.frameui.core.attributes.Attribute;
import me.squidxtv.frameui.core.attributes.BorderAttribute;
import me.squidxtv.frameui.core.graphics.Graphics;
import me.squidxtv.frameui.core.map.Map;
import me.squidxtv.frameui.core.math.BoundingBox;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ScreenModel extends Div {

    private static final ScreenParser SCREEN_PARSER = Objects.requireNonNull(Bukkit.getServicesManager().load(ScreenParser.class));

    private @Nullable BufferedImage backgroundImage;
    private int clickRadius;
    private int scrollRadius;

    private int blockWidth;
    private int blockHeight;

    public ScreenModel(@NotNull Element element) {
        this(Attribute.ID.get(element),
                Attribute.WIDTH.get(element),
                Attribute.HEIGHT.get(element),
                Attribute.BACKGROUND_COLOR.get(element),
                Attribute.BACKGROUND_IMAGE.get(element).orElse(null),
                new BorderAttribute(element),
                Attribute.CLICK_RADIUS.get(element),
                Attribute.SCROLL_RADIUS.get(element),
                Content.getChildren(element));
    }

    public ScreenModel(@NotNull String id,
                       int blockWidth, int blockHeight,
                       @NotNull Color backgroundColor,
                       @Nullable BufferedImage backgroundImage,
                       @NotNull BorderAttribute border,
                       int clickRadius, int scrollRadius,
                       @NotNull List<Content> children) {
        super(id, 0, 0, blockWidth * Map.WIDTH, blockHeight * Map.HEIGHT, backgroundColor, border, children);
        this.blockWidth = blockWidth;
        this.blockHeight = blockHeight;
        this.backgroundImage = backgroundImage;
        this.clickRadius = clickRadius;
        this.scrollRadius = scrollRadius;
    }

    @Override
    public void draw(@NotNull Graphics<?> graphics, @NotNull BoundingBox parentBoundingBox) {
        Color[] background = new Color[parentBoundingBox.width() * parentBoundingBox.height()];
        Arrays.fill(background, getBackgroundColor());
        graphics.draw(background, parentBoundingBox.width(), parentBoundingBox.height(), parentBoundingBox.x(), parentBoundingBox.y());

        if (backgroundImage != null) {
            graphics.draw(backgroundImage);
        }

        for (Content child : getChildren()) {
            child.draw(graphics, parentBoundingBox);
        }

        drawBorder(graphics, 0, 0, parentBoundingBox.width(), parentBoundingBox.height(), parentBoundingBox);
    }

    public @NotNull Optional<BufferedImage> getBackgroundImage() {
        return Optional.ofNullable(backgroundImage);
    }

    public int getClickRadius() {
        return clickRadius;
    }

    public int getScrollRadius() {
        return scrollRadius;
    }

    public void setBackgroundImage(@Nullable BufferedImage backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public void setClickRadius(int clickRadius) {
        this.clickRadius = clickRadius;
    }

    public void setScrollRadius(int scrollRadius) {
        this.scrollRadius = scrollRadius;
    }

    public int getBlockWidth() {
        return blockWidth;
    }

    public void setBlockWidth(int blockWidth) {
        this.blockWidth = blockWidth;
        setWidth(this.blockWidth * Map.WIDTH);
    }

    public int getBlockHeight() {
        return blockHeight;
    }

    public void setBlockHeight(int blockHeight) {
        this.blockHeight = blockHeight;
        setHeight(this.blockHeight * Map.HEIGHT);
    }

    @Override
    public void setX(int x) {
        throw new UnsupportedOperationException("");
    }

    @Override
    public void setY(int y) {
        throw new UnsupportedOperationException("");
    }

    @Override
    public String toString() {
        return "ScreenModel{" +
                "id=" + getId() +
                ", x=" + getX() +
                ", y=" + getY() +
                ", width=" + getWidth() +
                ", height=" + getHeight() +
                ", backgroundColor=" + getBackgroundColor() +
                ", border=" + getBorderAttribute() +
                ", backgroundImage=" + backgroundImage +
                '}';
    }

    public static @NotNull ScreenModel of(@NotNull Path xml) throws IOException, SAXException {
        return SCREEN_PARSER.parse(xml);
    }

    public static @NotNull ScreenModel of(@NotNull File file) throws IOException, SAXException {
        return SCREEN_PARSER.parse(file);
    }

    public static @NotNull ScreenModel of(@NotNull InputStream stream) throws IOException, SAXException {
        return SCREEN_PARSER.parse(stream);
    }

}
