package me.squidxtv.frameui.core.attributes;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Element;

import java.awt.Color;
import java.awt.Font;


public class FontAttribute {

    private @NotNull Font font;
    private @NotNull Color color;
    private int size;
    private @NotNull Insets padding;
    private boolean bold;
    private boolean italic;

    public FontAttribute(@NotNull Element element) {
        this(Attribute.FONT.get(element),
                Attribute.FONT_COLOR.get(element),
                Attribute.FONT_SIZE.get(element),
                Attribute.PADDING.get(element),
                Attribute.BOLD.get(element),
                Attribute.ITALIC.get(element));
    }

    public FontAttribute(@NotNull Font font, @NotNull Color color, int size, @NotNull Insets padding, boolean bold, boolean italic) {
        this.font = font;
        this.color = color;
        this.size = size;
        this.padding = padding;
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

    public @NotNull Insets getPadding() {
        return padding;
    }

    public void setPadding(@NotNull Insets padding) {
        this.padding = padding;
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
