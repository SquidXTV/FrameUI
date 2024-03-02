package me.squidxtv.frameui.core.content;

import me.squidxtv.frameui.core.attributes.Attribute;
import me.squidxtv.frameui.core.attributes.BorderAttribute;
import me.squidxtv.frameui.core.attributes.FontAttribute;
import me.squidxtv.frameui.core.attributes.Insets;
import me.squidxtv.frameui.core.graphics.Graphics;
import me.squidxtv.frameui.core.math.BoundingBox;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Element;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.util.Map;


public class Text extends AbstractContent {

    private static final FontRenderContext FONT_RENDER_CONTEXT = new FontRenderContext(null, false, true);

    private int x;
    private int y;

    private @NotNull String content;
    private @NotNull GlyphVector glyphVector;
    private @NotNull FontAttribute font;

    public Text(@NotNull Element element) {
        this(Attribute.ID.get(element),
                element.getTextContent(),
                Attribute.X.get(element),
                Attribute.Y.get(element),
                new FontAttribute(element),
                new BorderAttribute(element));
    }

    public Text(@NotNull String id, @NotNull String content, int x, int y, @NotNull FontAttribute font, @NotNull BorderAttribute border) {
        super(id, border);
        this.content = content;
        this.x = x;
        this.y = y;
        this.font = font;
        this.glyphVector = getDerivedFont().layoutGlyphVector(FONT_RENDER_CONTEXT, content.toCharArray(), 0, content.length(), Font.LAYOUT_LEFT_TO_RIGHT);
    }

    @Override
    public void draw(@NotNull Graphics<?> graphics, @NotNull BoundingBox parentBoundingBox) {
        BoundingBox absolutePosition = getAbsolutePosition(parentBoundingBox);

        if (absolutePosition.width() <= 0 || absolutePosition.height() <= 0) {
            return;
        }

        int imageWidth = getWidth();
        int imageHeight = getHeight();

        BufferedImage textImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D textGraphics = textImage.createGraphics();
        textGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                FONT_RENDER_CONTEXT.isAntiAliased() ? RenderingHints.VALUE_ANTIALIAS_ON : RenderingHints.VALUE_ANTIALIAS_OFF);
        textGraphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
                FONT_RENDER_CONTEXT.usesFractionalMetrics() ? RenderingHints.VALUE_FRACTIONALMETRICS_ON : RenderingHints.VALUE_FRACTIONALMETRICS_OFF);

        // Color.BLACK -> Transparent
        textGraphics.setColor(Color.BLACK);
        textGraphics.fillRect(0, 0, textImage.getWidth(), textImage.getHeight());

        Insets border = getBorderAttribute().insets();
        Insets padding = font.getPadding();
        Rectangle textBounds = glyphVector.getPixelBounds(FONT_RENDER_CONTEXT, 0, 0);
        int textX = border.west() + padding.west();
        int textY = border.north() - ((int) textBounds.getY()) + padding.north();

        textGraphics.setColor(font.getColor());
        textGraphics.drawGlyphVector(glyphVector, textX, textY);
        textGraphics.dispose();

        graphics.draw(textImage, absolutePosition.width(), absolutePosition.height(), absolutePosition.x(), absolutePosition.y());

        drawBorder(graphics, absolutePosition.x(), absolutePosition.y(), imageWidth, imageHeight, parentBoundingBox);
    }

    private @NotNull Font getDerivedFont() {
        Font derivedFont = font.getFont().deriveFont(Map.of(TextAttribute.SIZE, font.getSize()));

        if (font.isBold()) {
            derivedFont = derivedFont.deriveFont(Font.BOLD);
        }

        if (font.isItalic()) {
            derivedFont = derivedFont.deriveFont(Font.ITALIC);
        }

        return derivedFont;
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
        Insets border = getBorderAttribute().insets();
        Insets padding = font.getPadding();
        Rectangle textBounds = glyphVector.getPixelBounds(FONT_RENDER_CONTEXT, 0, 0);
        return textBounds.width + (border.west() + border.east()) + (padding.west() + padding.east());
    }

    @Override
    public int getHeight() {
        Insets border = getBorderAttribute().insets();
        Insets padding = font.getPadding();
        Rectangle textBounds = glyphVector.getPixelBounds(FONT_RENDER_CONTEXT, 0, 0);
        return textBounds.height + (border.north() + border.south()) + (padding.north() + padding.south());
    }

    public @NotNull String getContent() {
        return content;
    }

    public @NotNull FontAttribute getFont() {
        return font;
    }

    public void setContent(@NotNull String content) {
        this.content = content;
        this.glyphVector = getDerivedFont().layoutGlyphVector(FONT_RENDER_CONTEXT, content.toCharArray(), 0, content.length(), Font.LAYOUT_LEFT_TO_RIGHT);
    }

    public void setFont(@NotNull FontAttribute font) {
        this.font = font;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "Text{" +
                "id=" + getId() +
                ", textContent='" + content + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", font=" + font +
                '}';
    }
}
