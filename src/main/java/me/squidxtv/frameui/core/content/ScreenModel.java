package me.squidxtv.frameui.core.content;

import me.squidxtv.frameui.api.parser.ScreenParser;
import me.squidxtv.frameui.core.actions.initiator.ActionInitiator;
import me.squidxtv.frameui.core.actions.scroll.ScrollDirection;
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

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ScreenModel extends AbstractContent implements Parent {

    private static final ScreenParser SCREEN_PARSER = Objects.requireNonNull(Bukkit.getServicesManager().load(ScreenParser.class));

    private int width;
    private int height;
    private @NotNull Color backgroundColor;
    private @Nullable BufferedImage backgroundImage;
    private @NotNull BorderAttribute border;

    private int clickRadius;
    private int scrollRadius;
    private final @NotNull List<Content> children;

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

    public ScreenModel(@NotNull String id, int width, int height, @NotNull Color backgroundColor, @Nullable BufferedImage backgroundImage, @NotNull BorderAttribute border, int clickRadius, int scrollRadius, @NotNull List<Content> children) {
        super(id);
        this.width = width;
        this.height = height;
        this.backgroundColor = backgroundColor;
        this.backgroundImage = backgroundImage;
        this.border = border;
        this.clickRadius = clickRadius;
        this.scrollRadius = scrollRadius;
        this.children = children;
    }

    @Override
    public void draw(@NotNull Graphics<?> graphics, @NotNull BoundingBox parentBoundingBox) {
        Color[] background = new Color[parentBoundingBox.width() * parentBoundingBox.height()];
        Arrays.fill(background, backgroundColor);
        graphics.draw(background, parentBoundingBox.width(), parentBoundingBox.height(), parentBoundingBox.x(), parentBoundingBox.y());

        if (backgroundImage != null) {
            graphics.draw(backgroundImage);
        }

        for (Content child : children) {
            child.draw(graphics, parentBoundingBox);
        }

        border.draw(graphics, 0, 0, parentBoundingBox.width(), parentBoundingBox.height(), parentBoundingBox);
    }

    @Override
    public void click(@NotNull ActionInitiator<?> initiator, int clickX, int clickY, BoundingBox parentBoundingBox) {
        if (getClickAction().isEmpty()) {
            return;
        }

        BoundingBox absolutePosition = getAbsolutePosition(parentBoundingBox);

        if (absolutePosition.width() <= 0 || absolutePosition.height() <= 0) {
            return;
        }

        if(absolutePosition.isPositionOutside(clickX, clickY)) {
            return;
        }

        for (Content child : children) {
            child.click(initiator, clickX, clickY, absolutePosition);
        }
        getClickAction().get().perform(initiator, clickX, clickY);
    }

    @Override
    public void scroll(@NotNull ActionInitiator<?> initiator, @NotNull ScrollDirection direction, int scrollX, int scrollY, @NotNull BoundingBox parentBoundingBox) {
        if (getScrollAction().isEmpty()) {
            return;
        }

        BoundingBox absolutePosition = getAbsolutePosition(parentBoundingBox);

        if (absolutePosition.width() <= 0 || absolutePosition.height() <= 0) {
            return;
        }

        if(absolutePosition.isPositionOutside(scrollX, scrollY)) {
            return;
        }

        for (Content child : children) {
            child.scroll(initiator, direction, scrollX, scrollY, absolutePosition);
        }
        getScrollAction().get().perform(initiator, direction, scrollX, scrollY);
    }

    @Override
    public int getX() {
        return 0;
    }

    @Override
    public int getY() {
        return 0;
    }

    @Override
    public int getWidth() {
        return width * Map.WIDTH;
    }

    @Override
    public int getHeight() {
        return height * Map.HEIGHT;
    }

    public int getBlockWidth() {
        return width;
    }

    public int getBlockHeight() {
        return height;
    }

    public @NotNull Color getBackgroundColor() {
        return backgroundColor;
    }

    public @Nullable BufferedImage getBackgroundImage() {
        return backgroundImage;
    }

    public int getClickRadius() {
        return clickRadius;
    }

    public int getScrollRadius() {
        return scrollRadius;
    }

    @Override
    public @NotNull List<Content> getChildren() {
        return children;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setBackgroundColor(@NotNull Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void setBackgroundImage(@Nullable BufferedImage backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public @NotNull BorderAttribute getBorder() {
        return border;
    }

    public void setBorder(@NotNull BorderAttribute border) {
        this.border = border;
    }

    public void setClickRadius(int clickRadius) {
        this.clickRadius = clickRadius;
    }

    public void setScrollRadius(int scrollRadius) {
        this.scrollRadius = scrollRadius;
    }

    @Override
    public String toString() {
        return "Screen(%s, %d, %d, %s, %s, %s, %d, %d)".formatted(getId(), width, height, backgroundColor, backgroundImage, border, clickRadius, scrollRadius);
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
