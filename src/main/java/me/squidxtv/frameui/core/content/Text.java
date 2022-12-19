package me.squidxtv.frameui.core.content;

import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class Text {

    private @NotNull String string;
    private @NotNull Font font;
    private @NotNull Alignment alignment;

    private int offsetX;
    private int offsetY;

    public Text(@NotNull String text, @NotNull Font font, @NotNull Alignment alignment, int offsetX, int offsetY) {
        this.string = text;
        this.font = font;
        this.alignment = alignment;

        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    public Text(@NotNull String text, @NotNull Font font, @NotNull Alignment alignment) {
        this(text, font, alignment, 0, 0);
    }

    public Text(@NotNull String text, @NotNull Font font) {
        this(text, font, Alignment.TOP_LEFT);
    }

    public @NotNull Point getDrawPoint(@NotNull Component component, @NotNull Graphics graphics) {
        FontMetrics metrics = graphics.getFontMetrics(this.font);
        int x = switch (alignment) {
            case TOP_RIGHT, RIGHT, BOTTOM_RIGHT -> component.x + (component.width - metrics.stringWidth(string))
                    + 1 + offsetX;
            case TOP, CENTER, BOTTOM -> component.x + (component.width - metrics.stringWidth(string)) / 2 + 1 + offsetX;
            case TOP_LEFT, LEFT, BOTTOM_LEFT -> component.x - 1 + offsetX;
        };
        int y = switch (alignment) {
            case TOP_RIGHT, TOP, TOP_LEFT -> component.y + (metrics.getHeight() / 2) + 1 + offsetY;
            case RIGHT, CENTER, LEFT -> component.y + ((component.height - metrics.getHeight()) / 2)
                    + metrics.getAscent() + offsetY;
            case BOTTOM_RIGHT, BOTTOM, BOTTOM_LEFT -> component.y + component.height + offsetY;
        };

        return new Point(x, y);
    }

    public @NotNull String getString() {
        return string;
    }

    public void setString(@NotNull String string) {
        this.string = string;
    }

    public @NotNull Font getFont() {
        return font;
    }

    public void setFont(@NotNull Font font) {
        this.font = font;
    }

    public @NotNull Alignment getAlignment() {
        return alignment;
    }

    public void setAlignment(@NotNull Alignment alignment) {
        this.alignment = alignment;
    }

    public int getOffsetX() {
        return offsetX;
    }

    public void setOffsetX(int offsetX) {
        this.offsetX = offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(int offsetY) {
        this.offsetY = offsetY;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Text text = (Text) o;

        if (offsetX != text.offsetX) return false;
        if (offsetY != text.offsetY) return false;
        if (!string.equals(text.string)) return false;
        if (!font.equals(text.font)) return false;
        return alignment == text.alignment;
    }

    @Override
    public int hashCode() {
        int result = string.hashCode();
        result = 31 * result + font.hashCode();
        result = 31 * result + alignment.hashCode();
        result = 31 * result + offsetX;
        result = 31 * result + offsetY;
        return result;
    }
}
