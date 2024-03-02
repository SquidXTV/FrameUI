package me.squidxtv.frameui.core.content;

import me.squidxtv.frameui.api.cache.ImageCache;
import me.squidxtv.frameui.core.attributes.Attribute;
import me.squidxtv.frameui.core.attributes.BorderAttribute;
import me.squidxtv.frameui.core.graphics.Graphics;
import me.squidxtv.frameui.core.math.BoundingBox;
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

    public Image(@NotNull Element element) throws IOException {
        this(Attribute.ID.get(element),
                Attribute.X.get(element),
                Attribute.Y.get(element),
                Attribute.PATH.get(element),
                new BorderAttribute(element));
    }

    public Image(@NotNull String id, int x, int y, @NotNull Path path, @NotNull BorderAttribute border) throws IOException {
        super(id, border);
        this.x = x;
        this.y = y;
        this.path = path;
        this.visual = IMAGE_CACHE.getOrLoad(path);
    }

    @Override
    public void draw(@NotNull Graphics<?> graphics, @NotNull BoundingBox parentBoundingBox) {
        BoundingBox absolutePosition = getAbsolutePosition(parentBoundingBox);

        if (absolutePosition.width() <= 0 || absolutePosition.height() <= 0) {
            return;
        }

        graphics.draw(visual, absolutePosition.width(), absolutePosition.height(), absolutePosition.x(), absolutePosition.y());
        drawBorder(graphics, absolutePosition.x(), absolutePosition.y(), getWidth(), getHeight(), parentBoundingBox);
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public int getWidth() {
        return visual.getWidth();
    }

    @Override
    public int getHeight() {
        return visual.getHeight();
    }

    public @NotNull Path getPath() {
        return path;
    }

    public @NotNull BufferedImage getVisual() {
        return visual;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setPath(@NotNull Path path) {
        this.path = path;
    }

    public void setVisual(@NotNull BufferedImage visual) {
        this.visual = visual;
    }

}
