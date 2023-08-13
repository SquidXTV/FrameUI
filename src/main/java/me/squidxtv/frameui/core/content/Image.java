package me.squidxtv.frameui.core.content;

import me.squidxtv.frameui.api.cache.ImageCache;
import me.squidxtv.frameui.core.attributes.Attribute;
import me.squidxtv.frameui.core.attributes.BorderAttribute;
import me.squidxtv.frameui.core.graphics.Graphics;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Element;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;

public class Image extends AbstractContent {

    private static final @NotNull ImageCache IMAGE_CACHE = Objects.requireNonNull(Bukkit.getServicesManager().load(ImageCache.class));

    private int x;
    private int y;

    private @NotNull Path path;
    private @NotNull BufferedImage visual;

    private @NotNull BorderAttribute border;


    public Image(@NotNull Element element) throws IOException {
        this(Attribute.ID.get(element),
                Attribute.X.get(element),
                Attribute.Y.get(element),
                Attribute.PATH.get(element),
                new BorderAttribute(element));
    }

    public Image(@NotNull String id, int x, int y, @NotNull Path path, @NotNull BorderAttribute border) throws IOException {
        super(id);
        this.x = x;
        this.y = y;
        this.path = path;
        this.visual = IMAGE_CACHE.getOrLoad(path);
        this.border = border;
    }

    @Override
    public void draw(@NotNull Graphics<?> graphics, int parentX, int parentY, int parentWidth, int parentHeight) {
        int canvasX = parentX + this.x;
        int canvasY = parentY + this.y;
        int visibleWidth = Math.min(parentWidth - this.x, visual.getWidth());
        int visibleHeight = Math.min(parentHeight - this.y, visual.getHeight());

        if (visibleWidth <= 0 || visibleHeight <= 0) {
            return;
        }

        graphics.draw(visual, visibleWidth, visibleHeight, canvasX, canvasY);

        // TODO: 31/07/2023 check if visibleWidth/visibleHeight is needed instead
        border.draw(graphics, canvasX, canvasY, visual.getWidth(), visual.getHeight(), parentX, parentY, parentWidth, parentHeight);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public @NotNull Path getPath() {
        return path;
    }

    public void setPath(@NotNull Path path) {
        this.path = path;
    }

    public @NotNull BufferedImage getVisual() {
        return visual;
    }

    public void setVisual(@NotNull BufferedImage visual) {
        this.visual = visual;
    }

    public @NotNull BorderAttribute getBorder() {
        return border;
    }

    public void setBorder(@NotNull BorderAttribute border) {
        this.border = border;
    }

    @Override
    public String toString() {
        return "Image(%s, %d, %d, %s, %s, %s) {}".formatted(getId(), x, y, path, visual, border);
    }
}
