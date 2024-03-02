package me.squidxtv.frameui.core.attributes;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Element;

import java.awt.Color;

public record BorderAttribute(@NotNull Color color, @NotNull Insets insets) {

    public BorderAttribute {
        if (insets.north() < 0 || insets.south() < 0 || insets.west() < 0 || insets.east() < 0) {
            throw new IllegalArgumentException("Border sizes must be unsigned positive integers.");
        }
    }

    public BorderAttribute(@NotNull Element element) {
        this(Attribute.BORDER_COLOR.get(element),
                Attribute.BORDER_SIZE.get(element));
    }

}
