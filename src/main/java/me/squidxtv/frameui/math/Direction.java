package me.squidxtv.frameui.math;

import org.bukkit.util.Vector;

/**
 * The {@code Direction} enum represents the four possible cardinal directions
 * in a Minecraft world.
 * 
 * @see <a href="https://wiki.vg/Protocol#Spawn_Entity">Spawn Entity Packet</a>
 * @see <a href=
 *      "https://wiki.vg/Entity_metadata#Metadata_type:Direction">Direction
 *      ids</a>
 */
public enum Direction {

    /**
     * Represents the north direction in a Minecraft World.
     */
    NORTH(2, 0, 180, new Vector(0, 0, -1), new Vector(1, 1, 1)),
    /**
     * Represents the south direction in a Minecraft World.
     */
    SOUTH(3, 0, 0, new Vector(0, 0, 1), new Vector(0, 1, 0)),
    /**
     * Represents the west direction in a Minecraft World.
     */
    WEST(4, 0, 90, new Vector(-1, 0, 0), new Vector(1, 1, 0)),
    /**
     * Represents the east direction in a Minecraft World.
     */
    EAST(5, 0, 270, new Vector(1, 0, 0), new Vector(0, 1, 1)),;

    private final int packetId;
    private final Vector normal;

    private final Vector topLeftPixelOffset;
    private final int multiplierX;
    private final int multiplierZ;

    private final int pitch;
    private final int yaw;

    Direction(int packetId, int pitch, int yaw, Vector normal, Vector topLeftPixelOffset) {
        this.packetId = packetId;
        this.normal = normal;

        this.topLeftPixelOffset = topLeftPixelOffset;
        this.multiplierX = normal.getBlockZ();
        this.multiplierZ = -normal.getBlockX();

        this.pitch = pitch;
        this.yaw = yaw;
    }

    /**
     * Returns the id used by packets to identify a direction.
     * 
     * @return the packet id
     * @see <a href=
     *      "https://wiki.vg/Entity_metadata#Metadata_type:Direction">Direction
     *      ids</a>
     */
    public int getPacketId() {
        return packetId;
    }

    /**
     * Returns a copy of the normal vector representing the direction
     * 
     * @return the normal vector
     */
    public Vector getNormal() {
        return new Vector(normal.getX(), normal.getY(), normal.getZ());
    }

    /**
     * Returns a copy of the vector offset for the top-left pixel.
     * 
     * @return the top-left pixel offset vector
     */
    public Vector getTopLeftPixelOffset() {
        return new Vector(topLeftPixelOffset.getX(), topLeftPixelOffset.getY(), topLeftPixelOffset.getZ());
    }

    /**
     * Returns the X-axis multiplier for this direction.
     * 
     * @return the X-axis multiplier
     */
    public int getMultiplierX() {
        return multiplierX;
    }

    /**
     * Returns the Z-axis multiplier for this direction.
     * 
     * @return the Z-axis multiplier
     */
    public int getMultiplierZ() {
        return multiplierZ;
    }

    /**
     * Returns the pitch angle for this direction.
     * 
     * @return the pitch angle
     * @see <a href="https://wiki.vg/Protocol#Spawn_Entity">Spawn Entity Packet</a>
     */
    public int getPitch() {
        return pitch;
    }

    /**
     * Returns the yaw angle for this direction.
     * 
     * @return the yaw angle
     * @see <a href="https://wiki.vg/Protocol#Spawn_Entity">Spawn Entity Packet</a>
     */
    public int getYaw() {
        return yaw;
    }

}
