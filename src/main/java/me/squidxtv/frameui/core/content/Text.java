package me.squidxtv.frameui.core.content;

import me.squidxtv.frameui.core.attributes.Attribute;
import me.squidxtv.frameui.core.attributes.BorderAttribute;
import me.squidxtv.frameui.core.attributes.FontAttribute;
import me.squidxtv.frameui.core.graphics.Graphics;
import me.squidxtv.frameui.core.math.BoundingBox;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Element;

import java.awt.*;
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
    private @NotNull BorderAttribute border;

    public Text(@NotNull Element element) {
        this(Attribute.ID.get(element),
                element.getTextContent(),
                Attribute.X.get(element),
                Attribute.Y.get(element),
                new FontAttribute(element),
                new BorderAttribute(element));
    }

    public Text(@NotNull String id, @NotNull String content, int x, int y, @NotNull FontAttribute font, @NotNull BorderAttribute border) {
        super(id);
        this.content = content;
        this.x = x;
        this.y = y;
        this.font = font;
        this.border = border;

        this.glyphVector = getDerivedFont().layoutGlyphVector(FONT_RENDER_CONTEXT, content.toCharArray(), 0, content.length(), Font.LAYOUT_LEFT_TO_RIGHT);
    }

    @Override
    public void draw(@NotNull Graphics<?> graphics, BoundingBox parentBoundingBox) {
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

        textGraphics.setComposite(AlphaComposite.Clear);
        textGraphics.fillRect(0, 0, textImage.getWidth(), textImage.getHeight());
        textGraphics.setComposite(AlphaComposite.SrcOver);

        // TODO: test this and replace with custom static method Text#drawBorder(BorderAttribute)
        border.draw(textGraphics, 0, 0, imageWidth, imageHeight);

        Rectangle textBounds = glyphVector.getPixelBounds(FONT_RENDER_CONTEXT, 0, 0);
        int textX = border.getWidth() + font.getBorderPadding();
        int textY = border.getWidth() - ((int) textBounds.getY()) + font.getBorderPadding();

        textGraphics.setColor(font.getColor());
        textGraphics.drawGlyphVector(glyphVector, textX, textY);
        textGraphics.dispose();

        graphics.draw(textImage, absolutePosition.width(), absolutePosition.height(), absolutePosition.x(), absolutePosition.y());
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
        Rectangle textBounds = glyphVector.getPixelBounds(FONT_RENDER_CONTEXT, 0, 0);
        return textBounds.width + (border.getWidth() * 2) + (font.getBorderPadding() * 2);
    }

    @Override
    public int getHeight() {
        Rectangle textBounds = glyphVector.getPixelBounds(FONT_RENDER_CONTEXT, 0, 0);
        return textBounds.height + (border.getWidth() * 2) + (font.getBorderPadding() * 2);
    }

    // TODO: 24/08/2023 maybe rename to not cause confusion with Content class
    public @NotNull String getContent() {
        return content;
    }

    public @NotNull FontAttribute getFont() {
        return font;
    }

    public @NotNull BorderAttribute getBorder() {
        return border;
    }

    public void setContent(@NotNull String content) {
        this.content = content;
        this.glyphVector = getDerivedFont().layoutGlyphVector(FONT_RENDER_CONTEXT, content.toCharArray(), 0, content.length(), Font.LAYOUT_LEFT_TO_RIGHT);
    }

    public void setFont(@NotNull FontAttribute font) {
        this.font = font;
    }

    public void setBorder(@NotNull BorderAttribute border) {
        this.border = border;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "Text(%s, \"%s\", %d, %d, %s, %s)".formatted(getId(), content, x, y, font, border);
    }

}
