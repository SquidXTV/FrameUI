package me.squidxtv.frameui.math;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.NumberConversions;
import org.bukkit.util.Vector;

import me.squidxtv.frameui.core.Screen;
import me.squidxtv.frameui.core.ScreenLocation;

import java.awt.Point;
import java.util.List;
import java.util.Optional;
import java.util.function.ToDoubleFunction;

/**
 * Helper class for calculating intersections between players and screens.
 */
public final class IntersectionHelper {

    /**
     * The length of a single pixel on the screen.
     */
    public static final double PIXEL_LENGTH = 1.0 / 128.0;

    private IntersectionHelper() {
        throw new UnsupportedOperationException("Helper class, construction not supported.");
    }

    /**
     * Calculates the nearest intersection between a player and multiple screens.
     * Returns the closest interaction from all screens.
     * 
     * @param screens   the list of screens to check for intersections
     * @param player    the player of the interaction
     * @param maxRadius function to gather the maximum radius from the screen
     * @return the closest intersection from all
     */
    public static Optional<Intersection> getNearestIntersection(List<Screen> screens, Player player, ToDoubleFunction<Screen> maxRadius) {
        return screens.stream()
                .filter(screen -> screen.getState() == Screen.State.OPEN)
                .map(screen -> getIntersection(screen, player, NumberConversions.square(maxRadius.applyAsDouble(screen))))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .reduce((intersection, intersection2) -> {
                    if (intersection.distanceSquared() < intersection2.distanceSquared()) {
                        return intersection;
                    }
                    return intersection2;
                });
    }

    /**
     * Calculates the possible intersection between a player and a {@link Screen}.
     * 
     * @param screen             the screen to check for an intersection
     * @param player             the player of the interaction
     * @param maxDistanceSquared the maximum squared distance
     * @return a possible intersection
     */
    public static Optional<Intersection> getIntersection(Screen screen, Player player, double maxDistanceSquared) {
        Line eye = new Line(player.getEyeLocation());
        ScreenLocation location = screen.getLocation();
        Plane screenPlane = new Plane(location);

        Vector intersection = getIntersection(eye, screenPlane);
        if (intersection == null) {
            return Optional.empty();
        }

        double distanceSquared = intersection.distanceSquared(eye.point);
        if (distanceSquared > maxDistanceSquared) {
            return Optional.empty();
        }

        Point pixel = getPixel(intersection, location.originPixel(), location.direction());
        if (pixel.x < 0 || pixel.x >= screen.getPixelWidth()) {
            return Optional.empty();
        }

        if (pixel.y < 0 || pixel.y >= screen.getPixelHeight()) {
            return Optional.empty();
        }

        return Optional.of(new Intersection(screen, pixel, distanceSquared));
    }

    private static Vector getIntersection(Line line, Plane plane) {
        double denominator = line.direction.dot(plane.normal);

        // must be negative
        // = 0 means they are parallel
        // > 0 means that the player is not looking at the screen or looking at the back of it
        if (denominator >= 0) {
            return null;
        }

        double scalar = copy(plane.point).subtract(line.point).dot(plane.normal);
        scalar /= denominator;

        return line.evaluate(scalar);
    }

    private static Point getPixel(Vector intersection, Vector originPixel, Direction direction) {
        double y = Math.floor((originPixel.getY() - intersection.getY()) / PIXEL_LENGTH);

        double xz = direction.getMultiplierX() * (intersection.getX() - originPixel.getX());
        xz += direction.getMultiplierZ() * (intersection.getZ() - originPixel.getZ());
        xz = Math.floor(xz / PIXEL_LENGTH);

        return new Point((int) xz, (int) y);
    }

    /**
     * Represents a line in three-dimensional space defined by a point and a
     * direction.
     * 
     * @param point     point on the line
     * @param direction direction of the line
     */
    private record Line(Vector point, Vector direction) {

        public Line(Vector point, Vector direction) {
            this.point = copy(point);
            this.direction = copy(direction);
        }

        public Line(Location location) {
            this(location.toVector(), location.getDirection());
        }

        public Vector evaluate(double scalar) {
            return copy(this.point).add(copy(direction).multiply(scalar));
        }

    }

    /**
     * Represents a plane in three-dimensional space defined by a normal vector and
     * a point on the plane.
     * 
     * @param normal normal vector of plane
     * @param point  point on the plane
     */
    private record Plane(Vector normal, Vector point) {

        public Plane(Vector normal, Vector point) {
            this.normal = copy(normal);
            this.point = copy(point);
        }

        public Plane(ScreenLocation screenLocation) {
            this(screenLocation.direction().getNormal(), screenLocation.originPixel());
        }

    }

    private static Vector copy(Vector vector) {
        return new Vector(vector.getX(), vector.getY(), vector.getZ());
    }

}
