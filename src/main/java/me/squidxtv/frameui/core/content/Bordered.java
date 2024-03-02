package me.squidxtv.frameui.core.content;

import me.squidxtv.frameui.core.attributes.BorderAttribute;
import me.squidxtv.frameui.core.graphics.Graphics;
import me.squidxtv.frameui.core.math.BoundingBox;
import org.jetbrains.annotations.NotNull;

public interface Bordered {

    void drawBorder(@NotNull Graphics<?> graphics, int x, int y, int width, int height, BoundingBox parentBoundingBox);

    void setBorderAttribute(@NotNull BorderAttribute borderAttribute);
    @NotNull BorderAttribute getBorderAttribute();

}
