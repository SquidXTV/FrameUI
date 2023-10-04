package me.squidxtv.frameui.core.math;

import me.squidxtv.frameui.api.registry.ScreenRegistry;
import me.squidxtv.frameui.core.Screen;
import me.squidxtv.frameui.core.VirtualScreen;
import me.squidxtv.frameui.core.actions.ActionType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.Objects;
import java.util.Optional;

public final class IntersectionHelper {

    private static final ScreenRegistry SCREEN_REGISTRY = Objects.requireNonNull(Bukkit.getServicesManager().load(ScreenRegistry.class));
    private static final double PIXEL_LENGTH = 0.0078125;
    private static final double ITEM_FRAME_BLOCK_WIDTH = 0.0625;


    private IntersectionHelper() {
        throw new UnsupportedOperationException("Helper class, construction not supported.");
    }

    public static @NotNull Optional<Intersection> getScreenIntersection(@NotNull Player player, @NotNull ActionType type) {
        Location playerEye = player.getEyeLocation();
        Vector eye = playerEye.toVector();
        Vector lookDirection = playerEye.getDirection();

        Intersection nearestIntersection = null;
        double nearestDistance = Double.POSITIVE_INFINITY;

        for (VirtualScreen screen : SCREEN_REGISTRY.get(player)) {
            if (screen.getState() != Screen.State.OPEN) {
                continue;
            }

            Vector topLeftPixelLocation = getTopLeftPixelLocation(screen);
            Vector intersection = calculateIntersection(screen, topLeftPixelLocation, eye.clone(), lookDirection.clone());
            if (intersection == null) {
                continue;
            }

            double distance = intersection.distanceSquared(eye);
            int maxIntersectionRadiusSquared = switch (type) {
                case CLICK -> screen.getModel().getClickRadius();
                case SCROLL -> screen.getModel().getScrollRadius();
            };

            if (distance > maxIntersectionRadiusSquared || distance > nearestDistance) {
                continue;
            }

            Point pixel = getPixel(screen, intersection, topLeftPixelLocation);
            if (pixel.x < 0 || pixel.y < 0) {
                continue;
            }

            if (pixel.x >= screen.getGraphics().getPixelWidth() || pixel.y >= screen.getGraphics().getPixelHeight()) {
                continue;
            }

            nearestDistance = distance;
            nearestIntersection = new Intersection(screen, pixel);
        }

        return Optional.ofNullable(nearestIntersection);
    }


    private static @NotNull Vector getTopLeftPixelLocation(@NotNull VirtualScreen screen) {
        Location location = screen.getTopLeftFrameLocation();
        Direction direction = screen.getDirection();

        Vector offset = switch (direction) {
            case NORTH -> new Vector(1, 1, 1);
            case SOUTH -> new Vector(0, 1, 0);
            case WEST -> new Vector(1, 1, 0);
            case EAST -> new Vector(0, 1, 1);
        };

        Vector topLeftPixelLocation = location.toVector().add(offset);
        topLeftPixelLocation.add(direction.getNormal().multiply(ITEM_FRAME_BLOCK_WIDTH));
        topLeftPixelLocation.add(direction.getNormal().multiply(ITEM_FRAME_BLOCK_WIDTH / 8.0)); // why this
        return topLeftPixelLocation;
    }

    private static @Nullable Vector calculateIntersection(@NotNull VirtualScreen screen, @NotNull Vector topLeftPixelLocation, @NotNull Vector eye, @NotNull Vector lookDirection) {
        Vector planeNormal = screen.getDirection().getNormal();

        double parallel = lookDirection.dot(planeNormal);
        if (parallel >= 0.0) { // this seems arbitrary. I think this was only for North, testing needed
            return null;
        }

        double t = planeNormal.dot(topLeftPixelLocation) - planeNormal.dot(eye);
        t /= planeNormal.dot(lookDirection);
        return eye.add(lookDirection.multiply(t));
    }

    private static @NotNull Point getPixel(@NotNull VirtualScreen screen, @NotNull Vector intersection, @NotNull Vector topLeftPixelLocation) {
        double y = Math.floor((topLeftPixelLocation.getY() - intersection.getY()) / PIXEL_LENGTH);

        int multiplierX = screen.getDirection().getMultiplierX();
        int multiplierZ = screen.getDirection().getMultiplierZ();
        double xz = multiplierX * (intersection.getX() - topLeftPixelLocation.getX()) + multiplierZ * (intersection.getZ() - topLeftPixelLocation.getZ());
        xz = Math.floor(xz / PIXEL_LENGTH);

        return new Point((int) xz, (int) y);
    }

    public record Intersection(@NotNull Screen<?> screen, @NotNull Point pixel) {
    }

}
