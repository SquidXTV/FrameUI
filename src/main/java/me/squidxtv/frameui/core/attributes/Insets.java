package me.squidxtv.frameui.core.attributes;

import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;
import java.util.regex.Pattern;

public record Insets(int north, int south, int west, int east) {

    private static @NotNull Predicate<String> INSETS_REGEX = Pattern.compile("^\\d+$|^\\d+,\\d+,\\d+,\\d+$|^\\d+,\\d+$").asMatchPredicate();

    public static @NotNull Insets valueOf(String input) {
        input = input.trim();
        if (!INSETS_REGEX.test(input)) {
            throw new IllegalArgumentException("Provided border width is in invalid format.");
        }

        String[] numbers = input.split(",");
        if (numbers.length == 1) {
            int all = Integer.parseUnsignedInt(numbers[0]);
            return new Insets(all, all, all, all);
        }
        if (numbers.length == 2) {
            int northAndSouth = Integer.parseUnsignedInt(numbers[0]);
            int westAndEast = Integer.parseUnsignedInt(numbers[1]);
            return new Insets(northAndSouth, northAndSouth, westAndEast, westAndEast);
        }

        int north = Integer.parseUnsignedInt(numbers[0]);
        int south = Integer.parseUnsignedInt(numbers[1]);
        int west = Integer.parseUnsignedInt(numbers[2]);
        int east = Integer.parseUnsignedInt(numbers[3]);
        return new Insets(north, south, west, east);
    }
}
