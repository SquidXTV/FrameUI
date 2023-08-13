package me.squidxtv.frameui.core.attributes;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Element;

import java.awt.*;

public class FontAttribute {

    private @NotNull Font font;
    private @NotNull Color color;
    private int size;
    private int borderPadding;
    private boolean bold;
    private boolean italic;

    public FontAttribute(@NotNull Element element) {
        this(Attribute.FONT.get(element),
                Attribute.FONT_COLOR.get(element),
                Attribute.FONT_SIZE.get(element),
                Attribute.BORDER_PADDING.get(element),
                Attribute.BOLD.get(element),
                Attribute.ITALIC.get(element));
    }

    public FontAttribute(@NotNull Font font, @NotNull Color color, int size, int borderPadding, boolean bold, boolean italic) {
        this.font = font;
        this.color = color;
        this.size = size;
        this.borderPadding = borderPadding;
        this.bold = bold;
        this.italic = italic;
    }

    public @NotNull Font getFont() {
        return font;
    }

    public void setFont(@NotNull Font font) {
        this.font = font;
    }

    public @NotNull Color getColor() {
        return color;
    }

    public void setColor(@NotNull Color color) {
        this.color = color;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getBorderPadding() {
        return borderPadding;
    }

    public void setBorderPadding(int borderPadding) {
        this.borderPadding = borderPadding;
    }

    public boolean isBold() {
        return bold;
    }

    public void setBold(boolean bold) {
        this.bold = bold;
    }

    public boolean isItalic() {
        return italic;
    }

    public void setItalic(boolean italic) {
        this.italic = italic;
    }
}
