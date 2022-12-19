package com.squidxtv.frameui.util;

import com.squidxtv.frameui.core.Screen;
import com.squidxtv.frameui.core.ScreenManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

/**
 * Utility class for handling clicks on screens in Minecraft.
 */
public final class ScreenUtil {

    private static final double PIXEL_LENGTH = 1.0 / 128.0;

    private ScreenUtil() {}

    /**
     * Calculates the intersection point between a screen and a player's eye location and look direction.
     *
     * @param screen the screen to check for intersection
     * @param playerEye the player's eye location
     * @param lookDirection the player's look direction
     * @return the intersection point, or null if no intersection is found
     */
    public static @Nullable Vector getIntersectionPoint(@NotNull Screen screen, @NotNull Vector playerEye, @NotNull Vector lookDirection) {
        Vector planeNormal = screen.getDirection().getNormal();
        Vector topLeft = getTopLeft(screen);

        double parallel = lookDirection.dot(planeNormal);

        if (parallel == 0.0) {
            return null;
        }

        // only for north (needs testing).
        // checks if player can only "see" back of the screen
        if (parallel > 0) {
            return null;
        }

        double t = (planeNormal.dot(topLeft) - planeNormal.dot(playerEye)) / planeNormal.dot(lookDirection);
        Vector intersection = playerEye.add(lookDirection.multiply(t));

        if (intersection.distanceSquared(playerEye) > 6*6) {
            return null;
        }

        return intersection;
    }

    private static @NotNull Vector getTopLeft(@NotNull Screen screen) {
        Location loc = screen.getLocation();
        Direction direction = screen.getDirection();

        int multiX = direction.getMultiplierX();
        int multiZ = direction.getMultiplierZ();

        int offsetX = multiX == -1 || multiZ == 1 ? 1 : 0;
        int offsetY = 1;
        int offsetZ = multiX == -1 || multiZ ==-1 ? 1 : 0;

        double minX = loc.getX() + offsetX;
        double minY = loc.getY() + offsetY;
        double minZ = loc.getZ() + offsetZ;

        return new Vector(minX, minY, minZ);
    }

    /**
     * Calculates the pixel coordinates of a point on a screen given its 3D coordinates in the Minecraft world and the screen's 3D position and orientation.
     *
     * @param screen the screen to calculate pixel coordinates for
     * @param intersection the 3D coordinates of the point on the screen
     * @return the pixel coordinates of the point on the screen
     */
    public static @NotNull Point getPixel(@NotNull Screen screen, @NotNull Vector intersection) {
        // FIXME: 19.12.2022 check direction for proper calculation (pixel area depends on screen direction)
        Vector topLeft = getTopLeft(screen);

        double y = Math.round(Math.abs((intersection.getY() - topLeft.getY()) / PIXEL_LENGTH));

        double xz = intersection.getX() - topLeft.getX();
        if (screen.getDirection().getMultiplierZ() != 0) {
            xz = intersection.getZ() - topLeft.getZ();
        }
        xz = Math.round(Math.abs(xz / PIXEL_LENGTH));

        return new Point((int) xz, (int) y);
    }

    public static @Nullable ScreenIntersection getScreenIntersection(@NotNull Player player) {
        Location playerEye = player.getEyeLocation().clone();
        Vector eye = playerEye.toVector();
        Vector lookDirection = playerEye.getDirection();

        Screen nearestScreen = null;
        Vector nearestIntersection = null;
        double nearestDistance = Double.POSITIVE_INFINITY;

        for (Screen screen : ScreenManager.getInstance().getByPlayer(player)) {
            Vector intersection = ScreenUtil.getIntersectionPoint(screen, eye.clone(), lookDirection.clone());
            if (intersection == null) {
                continue;
            }
            double distance = intersection.distance(eye.clone());
            if (distance < nearestDistance) {
                nearestDistance = distance;
                nearestScreen = screen;
                nearestIntersection = intersection;
            }
        }

        if (nearestScreen == null) {
            return null;
        }
        return new ScreenIntersection(nearestScreen, nearestIntersection);
    }

}
