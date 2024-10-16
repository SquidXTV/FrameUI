package me.squidxtv.frameui.core;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import me.squidxtv.frameui.math.Direction;
import me.squidxtv.frameui.math.IntersectionHelper;

/**
 * The {@code ScreenLocation} stores information about the location and
 * orientation of a {@link Screen} in the Minecraft World.
 * 
 * @param origin      the origin {@link Location} of the screen. This is defined
 *                    to be the top left item frame location
 * @param originPixel the origin pixel location. This is defined to be the top
 *                    left pixel (0, 0)
 * @param direction   the {@link Direction} the screen is facing
 */
public record ScreenLocation(Location origin, Vector originPixel, Direction direction) {

    /**
     * Creates a new {@code ScreenLocation}.
     * 
     * @param origin      the origin {@link Location} of the screen. This is defined
     *                    to be the top left item frame location
     * @param originPixel the origin pixel location. This is defined to be the top
     *                    left pixel (0, 0)
     * @param direction   the {@link Direction} the screen is facing
     * @apiNote Prefer using
     *          {@link ScreenLocation#ScreenLocation(Location, Direction)}.
     */
    public ScreenLocation {}

    /**
     * Creates a new {@code  ScreenLocation}.
     *
     * @param origin    the origin {@link Location} of the screen. This is defined
     *                  to be the top left item frame location
     * @param direction the {@link Direction} the screen is facing
     * @implNote Creates a screen location that calculates the origin pixel based on
     *           the origin location and direction automatically.
     */
    public ScreenLocation(Location origin, Direction direction) {
        this(toBlockLocation(origin), calculateOriginPixel(origin, direction), direction);
    }

    private static Vector calculateOriginPixel(Location origin, Direction direction) {
        return toBlockLocation(origin).toVector()
                .add(direction.getTopLeftPixelOffset())
                .add(direction.getNormal().multiply(IntersectionHelper.PIXEL_LENGTH));
    }

    private static Location toBlockLocation(Location loc) {
        return new Location(loc.getWorld(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
    }

}
