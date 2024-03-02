package me.squidxtv.frameui.core.attributes;

import me.squidxtv.frameui.api.FrameAPI;
import me.squidxtv.frameui.api.cache.ImageCache;
import me.squidxtv.frameui.util.PathUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicesManager;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Element;

import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class Attribute<T> {

    private static final Font MINECRAFT_FONT;
    private static final ImageCache IMAGE_CACHE;

    static {
        ServicesManager servicesManager = Bukkit.getServicesManager();
        MINECRAFT_FONT = Objects.requireNonNull(servicesManager.load(FrameAPI.class)).getMinecraftFont();
        IMAGE_CACHE = Objects.requireNonNull(servicesManager.load(ImageCache.class));
    }

    private static final Font[] FONTS = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();

    private static final Predicate<String> COLOR_REGEX = Pattern.compile("#[0-9a-fA-F]{6}").asMatchPredicate();

    private static final AttributeSupplier<Color> COLOR_SUPPLIER = (element, hex) -> {
        if (!COLOR_REGEX.test(hex)) {
            throw new IllegalArgumentException("Provided color is not in hex format #rrggbb.");
        }

        int red = Integer.parseInt(hex.substring(1, 3), 16);
        int green = Integer.parseInt(hex.substring(3, 5), 16);
        int blue = Integer.parseInt(hex.substring(5, 7), 16);
        return new Color(red, green, blue);
    };

    public static final Attribute<String> ID = new Attribute<>("id", (element, result) -> result);
    public static final Attribute<Integer> WIDTH = new Attribute<>("width", (element, result) -> Integer.parseUnsignedInt(result));
    public static final Attribute<Integer> HEIGHT = new Attribute<>("height", (element, result) -> Integer.parseUnsignedInt(result));
    public static final Attribute<Integer> X = new Attribute<>("x", (element, result) -> Integer.parseInt(result));
    public static final Attribute<Integer> Y = new Attribute<>("y", (element, result) -> Integer.parseInt(result));

    public static final Attribute<Color> BORDER_COLOR = new Attribute<>("border-color", COLOR_SUPPLIER);
    public static final Attribute<Insets> BORDER_SIZE = new Attribute<>("border-size", (element, result) -> Insets.valueOf(result));

    public static final Attribute<Font> FONT = new Attribute<>("font", (element, fontName) -> {
        if (fontName.isEmpty()) {
            return MINECRAFT_FONT;
        }
        return getFontByName(fontName).orElse(MINECRAFT_FONT);
    });

    public static final Attribute<Integer> FONT_SIZE = new Attribute<>("font-size", (element, result) -> Integer.parseUnsignedInt(result));
    public static final Attribute<Color> FONT_COLOR = new Attribute<>("font-color", COLOR_SUPPLIER);
    public static final Attribute<Boolean> BOLD = new Attribute<>("bold", (element, result) -> Boolean.parseBoolean(result));
    public static final Attribute<Boolean> ITALIC = new Attribute<>("italic", (element, result) -> Boolean.parseBoolean(result));
    public static final Attribute<Insets> PADDING = new Attribute<>("padding-size", (element, result) -> Insets.valueOf(result));

    public static final Attribute<Integer> CLICK_RADIUS = new Attribute<>("click-radius", (element, result) -> Integer.parseUnsignedInt(result));
    public static final Attribute<Integer> SCROLL_RADIUS = new Attribute<>("scroll-radius", (element, result) -> Integer.parseUnsignedInt(result));
    public static final Attribute<Color> BACKGROUND_COLOR = new Attribute<>("background-color", COLOR_SUPPLIER);

    public static final Attribute<Optional<BufferedImage>> BACKGROUND_IMAGE = new Attribute<>("background-image", (element, result) -> {
        if (result.isEmpty() || result.isBlank()) {
            return Optional.empty();
        }

        URI documentLocation = URI.create(element.getOwnerDocument().getDocumentURI());
        Path path = PathUtils.convert(Path.of(documentLocation), result);
        try {
            return Optional.of(IMAGE_CACHE.getOrLoad(path));
        } catch (IOException e) {
            return Optional.empty();
        }
    });

    public static final Attribute<Path> PATH = new Attribute<>("path", (element, result) -> {
        URI documentLocation = URI.create(element.getOwnerDocument().getDocumentURI());
        return PathUtils.convert(Path.of(documentLocation), result);
    });

    private final @NotNull String name;
    private final @NotNull AttributeSupplier<T> supplier;

    private Attribute(@NotNull String name, @NotNull AttributeSupplier<T> supplier) {
        this.name = name;
        this.supplier = supplier;
    }

    public @NotNull T get(@NotNull Element element) {
        String result = element.getAttribute(this.name);
        return supplier.get(element, result);
    }

    private static @NotNull Optional<Font> getFontByName(@NotNull String name) {
        for (Font font : FONTS) {
            if (font.getFontName(Locale.getDefault()).equalsIgnoreCase(name)) {
                return Optional.of(font);
            }
        }
        return Optional.empty();
    }

    @FunctionalInterface
    private interface AttributeSupplier<T> {
        @NotNull T get(@NotNull Element element, @NotNull String result);

    }

}
