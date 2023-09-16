package me.squidxtv.frameui.core.math;

public record BoundingBox(int x, int y, int width, int height) {

    public boolean isPositionOutside(int otherX, int otherY) {
        if (otherX < x || otherX > (x + width)) {
            return true;
        }

        return otherX < y || otherY > (y + height);
    }

}
