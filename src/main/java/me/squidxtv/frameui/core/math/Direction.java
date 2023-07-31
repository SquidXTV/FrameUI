package me.squidxtv.frameui.core.math;

import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public enum Direction {

    NORTH(2, new Vector(0, 0, -1)),
    SOUTH(3, new Vector(0, 0, 1)),
    WEST(4, new Vector(-1, 0, 0)),
    EAST(5, new Vector(1, 0, 0));

    private final int packetDirection;
    private final @NotNull Vector normal;
    private final int multiplierX;
    private final int multiplierZ;

    Direction(int packetDirection, @NotNull Vector normal) {
        this.packetDirection = packetDirection;
        this.normal = normal;
        this.multiplierX = normal.getBlockZ();
        this.multiplierZ = normal.getBlockX() * -1;
    }

    public int getPacketDirection() {
        return packetDirection;
    }

    public Vector getNormal() {
        return normal;
    }

    public int getMultiplierX() {
        return multiplierX;
    }

    public int getMultiplierZ() {
        return multiplierZ;
    }
}
