package me.squidxtv.frameui.core.content;

import me.squidxtv.frameui.api.parser.ScreenParser;
import me.squidxtv.frameui.core.attributes.Attribute;
import me.squidxtv.frameui.core.attributes.BorderAttribute;
import me.squidxtv.frameui.core.graphics.Graphics;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class ScreenModel extends AbstractContent {

    protected static final ScreenParser SCREEN_PARSER = Bukkit.getServicesManager().load(ScreenParser.class);

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

    public ScreenModel(@NotNull String id, int width, int height, @NotNull Color backgroundColor, @Nullable BufferedImage backgroundImage, @NotNull BorderAttribute border, int clickRadius, int scrollRadius, @NotNull Content[] children) {
        super(id);
        this.width = width;
        this.height = height;
        this.backgroundColor = backgroundColor;
        this.backgroundImage = backgroundImage;
        this.border = border;
        this.clickRadius = clickRadius;
        this.scrollRadius = scrollRadius;
        this.children = Arrays.asList(children);
    }

    @Override
    public void draw(@NotNull Graphics<?> graphics, int parentX, int parentY, int parentWidth, int parentHeight) {
        Color[] background = new Color[parentWidth * parentHeight];
        Arrays.fill(background, backgroundColor);
        graphics.draw(background, parentWidth, parentHeight, parentX, parentY);

        if (backgroundImage != null) {
            graphics.draw(backgroundImage);
        }

        for (Content child : children) {
            child.draw(graphics, parentX, parentY, parentWidth, parentHeight);
        }

        border.draw(graphics, 0, 0, parentWidth, parentHeight, parentX, parentY, parentWidth, parentHeight);
    }

    @Override
    public boolean click(@NotNull Player player, int x, int y) {
        if (!super.click(player, x, y)) {
            return false;
        }

        for (Content child : children) {
            child.click(player, x, y);
        }

        return true;
    }

    @Override
    public boolean scroll(@NotNull Player player, @NotNull ScrollDirection direction, int x, int y) {
        if (!super.scroll(player, direction, x, y)) {
            return false;
        }

        for (Content child : children) {
            child.scroll(player, direction, x, y);
        }

        return true;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public @NotNull Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(@NotNull Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public @Nullable BufferedImage getBackgroundImage() {
        return backgroundImage;
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

    public int getClickRadius() {
        return clickRadius;
    }

    public void setClickRadius(int clickRadius) {
        this.clickRadius = clickRadius;
    }

    public int getScrollRadius() {
        return scrollRadius;
    }

    public void setScrollRadius(int scrollRadius) {
        this.scrollRadius = scrollRadius;
    }

    public @NotNull List<Content> getChildren() {
        return children;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{\n");
        builder.append("\tScreen(%s, %d, %d, %s, %s, %s, %d, %d)\n".formatted(getId(), width, height, backgroundColor, backgroundImage, border, clickRadius, scrollRadius));
        for (Content child : children) {
            builder.append("\t\t").append(child).append("\n");
        }
        builder.append("}");
        return builder.toString();
    }

    public static @NotNull ScreenModel of(@NotNull Path xml) throws IOException, SAXException {
        return SCREEN_PARSER.parse(xml);
    }
}
