package me.squidxtv.frameui.util;

import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public final class PathUtils {

    /**
     * The regular expression pattern for matching Windows-style paths.
     */
    private static final Predicate<String> WINDOWS_PATH = Pattern.compile("^[A-Z]:\\\\.+$").asMatchPredicate();

    /**
     * The regular expression pattern for matching Unix-style paths.
     */
    private static final Predicate<String> UNIX_PATH = Pattern.compile("^/.+$").asMatchPredicate();

    /**
     * The regular expression pattern for matching relative paths.
     */
    private static final Predicate<String> RELATIVE_PATH = Pattern.compile("^@\\{.+}$").asMatchPredicate();

    private PathUtils() {
        throw new UnsupportedOperationException("Utils class, construction not supported.");
    }

    public static @NotNull Path convert(Path xml, String path) {
        if (RELATIVE_PATH.test(path)) {
            return xml.getParent().resolve(path.substring(2, path.length() - 1));
        }

        if (WINDOWS_PATH.test(path) || UNIX_PATH.test(path)) {
            return Path.of(path);
        }

        throw new IllegalArgumentException("Invalid path format: " + path);
    }
}
