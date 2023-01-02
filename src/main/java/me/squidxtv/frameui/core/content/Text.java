package me.squidxtv.frameui.core.content;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Element;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Objects;

public class Text extends ElementNode {

    public static final Font MINECRAFT_FONT;

    static  {
        try {
            MINECRAFT_FONT = Font.createFont(Font.TRUETYPE_FONT, new File(Objects.requireNonNull(Text.class.getResource("/minecraft_font.ttf")).toURI()));
        } catch (FontFormatException | URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private @NotNull String text;

    private int x;
    private int y;

    private @NotNull Font font;

    public Text(@NotNull String id, int x, int y, @NotNull String text, @NotNull Font font) {
        super(id);
        this.text = text;
        this.x = x;
        this.y = y;
        this.font = font;
    }

    @Override
    public void draw(Graphics g) {
        if (text.isEmpty() || text.isBlank()) {
            return;
        }
        g.setFont(font);
        g.drawString(text, x, y);
    }

    public @NotNull String getText() {
        return text;
    }

    public void setText(@NotNull String text) {
        this.text = text;
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

    public @NotNull Font getFont() {
        return font;
    }

    public void setFont(@NotNull Font font) {
        this.font = font;
    }

    @Contract("_ -> new")
    public static @NotNull Text of(@NotNull Element element) {
        String id = element.getAttribute("id");
        String text = element.getTextContent();

        int x = Integer.parseInt(element.getAttribute("x"));
        int y = Integer.parseInt(element.getAttribute("y"));

        String fontName = element.getAttribute("font");
        Font font;

        if (fontName.isBlank() || fontName.isEmpty()) {
            font = MINECRAFT_FONT;
        } else {
            font = Font.decode(fontName);
        }

        float fontSize = Float.parseFloat(element.getAttribute("font-size"));
        font.deriveFont(fontSize);

        boolean bold = Boolean.parseBoolean(element.getAttribute("bold"));
        if (bold) {
            font.deriveFont(Font.BOLD);
        }

        return new Text(id, x, y, text, font);
    }
}
