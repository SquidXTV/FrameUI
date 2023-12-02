package me.squidxtv.frameui.core.math;

import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public enum Direction {

    NORTH(2, 0, 180, new Vector(0, 0, -1)),
    SOUTH(3,0, 0, new Vector(0, 0, 1)),
    WEST(4, 0, 90,new Vector(-1, 0, 0)),
    EAST(5, 0, 270,new Vector(1, 0, 0));

    private final int packetValue;
    private final @NotNull Vector normal;

    private final int multiplierX;
    private final int multiplierZ;

    private final int pitch;
    private final int yaw;

    Direction(int packetValue, int pitch, int yaw, @NotNull Vector normal) {
        this.packetValue = packetValue;
        this.normal = normal;
        this.multiplierX = normal.getBlockZ();
        this.multiplierZ = normal.getBlockX() * -1;
        this.pitch = pitch;
        this.yaw = yaw;
    }

    public int getPacketValue() {
        return packetValue;
    }

    public @NotNull Vector getNormal() {
        return normal;
    }

    public int getMultiplierX() {
        return multiplierX;
    }

    public int getMultiplierZ() {
        return multiplierZ;
    }

    public int getPitch() {
        return pitch;
    }

    public int getYaw() {
        return yaw;
    }

}
