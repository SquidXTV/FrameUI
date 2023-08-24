package me.squidxtv.frameui.core.content;

import me.squidxtv.frameui.core.attributes.Attribute;
import me.squidxtv.frameui.core.attributes.BorderAttribute;
import me.squidxtv.frameui.core.attributes.FontAttribute;
import me.squidxtv.frameui.core.graphics.Graphics;
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
    }

    @Override
    public void draw(@NotNull Graphics<?> graphics, int parentX, int parentY, int parentWidth, int parentHeight) {
        Font derivedFont = getDerivedFont();
        GlyphVector glyphVector = derivedFont.layoutGlyphVector(FONT_RENDER_CONTEXT, content.toCharArray(), 0, content.length(), Font.LAYOUT_LEFT_TO_RIGHT);
        Rectangle textBounds = glyphVector.getPixelBounds(FONT_RENDER_CONTEXT, 0, 0);

        int borderWidth = border.getWidth();
        int imageWidth = textBounds.width + (borderWidth*2);
        int imageHeight = textBounds.height + (borderWidth*2);
        int textX = borderWidth;
        int textY = borderWidth - ((int) textBounds.getY());

        if (borderWidth > 0) {
            int borderPadding = font.getBorderPadding();
            imageWidth += borderPadding *2;
            imageHeight += borderPadding *2;
            textX += borderPadding;
            textY += borderPadding;
        }

        int canvasX = parentX + this.x;
        int canvasY = parentY + this.y;
        int visibleWidth = Math.min(parentWidth - this.x, imageWidth);
        int visibleHeight = Math.min(parentHeight - this.y, imageHeight);

        if (visibleWidth <= 0 || visibleHeight <= 0) {
            return;
        }

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

        textGraphics.setColor(font.getColor());
        textGraphics.drawGlyphVector(glyphVector, textX, textY);
        textGraphics.dispose();

        graphics.draw(textImage, visibleWidth, visibleHeight, canvasX, canvasY);
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

    // TODO: 24/08/2023 maybe rename to not cause confusion with Content class
    public @NotNull String getContent() {
        return content;
    }

    public void setContent(@NotNull String content) {
        this.content = content;
    }

    public @NotNull FontAttribute getFont() {
        return font;
    }

    public void setFont(@NotNull FontAttribute font) {
        this.font = font;
    }

    public @NotNull BorderAttribute getBorder() {
        return border;
    }

    public void setBorder(@NotNull BorderAttribute border) {
        this.border = border;
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

    @Override
    public String toString() {
        return "Text(%s, \"%s\", %d, %d, %s, %s)".formatted(getId(), content, x, y, font, border);
    }
}
