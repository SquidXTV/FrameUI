package me.squidxtv.frameui.core.properties;

import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public enum Direction {
    NORTH(2, new Vector(0, 0, -1), -1, 0),
    SOUTH(3, new Vector(0, 0, 1), 1, 0),
    WEST(4, new Vector(-1, 0, 0), 0, 1),
    EAST(5, new Vector(1, 0, 0), 0, -1);


    /**
     * Direction packet-based
     * @see <a href="https://wiki.vg/Object_Data#Item_Frame">wiki.vg</a>
     */
    private final int internalDirection;

    /**
     * Normal vector of plane
     * @see <a href="https://en.wikipedia.org/wiki/Line%E2%80%93plane_intersection">en.wikipedia.org</a>
     */
    private final @NotNull Vector normal;

    private final int multiplierX;
    private final int multiplierZ;

    Direction(int internalDirection, Vector normal, int multiplierX, int multiplierY) {
        this.internalDirection = internalDirection;
        this.normal = normal;
        this.multiplierX = multiplierX;
        this.multiplierZ = multiplierY;
    }

    public Vector getNormal() {
        return normal.clone();
    }

    public int getInternalDirection() {
        return internalDirection;
    }

    public int getMultiplierX() {
        return multiplierX;
    }

    public int getMultiplierZ() {
        return multiplierZ;
    }

}
