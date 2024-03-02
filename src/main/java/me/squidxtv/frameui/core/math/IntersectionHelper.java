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

import java.awt.Point;
import java.util.Objects;
import java.util.Optional;

public final class IntersectionHelper {

    public static final double PIXEL_LENGTH = 0.0078125;
    private static final ScreenRegistry SCREEN_REGISTRY = Objects.requireNonNull(Bukkit.getServicesManager().load(ScreenRegistry.class));

    private IntersectionHelper() {
        throw new UnsupportedOperationException("Helper class, construction not supported.");
    }

    public static @NotNull Optional<Intersection> getScreenIntersection(@NotNull Player player, @NotNull ActionType type) {
        Location playerEye = player.getEyeLocation();
        Vector eye = playerEye.toVector();
        Vector lookDirection = playerEye.getDirection();

        // ToDo: needs to check if entity exists in players world (?), remove player from viewers if switching world, additionally remove from viewer list if leaving
        return SCREEN_REGISTRY.get(player)
                .filter(screen -> screen.getState() == Screen.State.OPEN)
                .map(screen -> getIntersection(screen, type, eye, lookDirection))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .reduce((i1, i2) -> {
                    if (i1.distanceSquared < i2.distanceSquared) {
                        return i1;
                    }
                    return i2;
                });
    }

    private static Optional<Intersection> getIntersection(VirtualScreen screen, ActionType type, Vector eye, Vector lookDirection) {
        Vector topLeftPixelPosition = screen.getTopLeftPixelPosition();
        Vector intersection = calculateIntersection(screen.getDirection(), topLeftPixelPosition.clone(), eye.clone(), lookDirection.clone());
        if (intersection == null) {
            return Optional.empty();
        }

        double distanceSquared = intersection.distanceSquared(eye);
        int maxIntersectionRadius = switch (type) {
            case CLICK -> screen.getModel().getClickRadius();
            case SCROLL -> screen.getModel().getScrollRadius();
        };


        if (distanceSquared > (maxIntersectionRadius * maxIntersectionRadius)) {
            return Optional.empty();
        }

        Point pixel = getPixel(screen.getDirection(), intersection.clone(), topLeftPixelPosition.clone());
        if (pixel.x < 0 || pixel.y < 0 || pixel.x >= screen.getModel().getWidth() || pixel.y >= screen.getModel().getHeight()) {
            return Optional.empty();
        }
        return Optional.of(new Intersection(screen, pixel, distanceSquared));
    }

    private static @Nullable Vector calculateIntersection(@NotNull Direction direction, @NotNull Vector topLeftPixelLocation, @NotNull Vector eye, @NotNull Vector lookDirection) {
        Vector planeNormal = direction.getNormal();

        double parallel = lookDirection.dot(planeNormal);
        if (parallel >= 0) {
            return null;
        }

        double d = planeNormal.dot(topLeftPixelLocation.subtract(eye));
        d /= parallel;
        return eye.add(lookDirection.multiply(d));
    }

    private static @NotNull Point getPixel(@NotNull Direction direction, @NotNull Vector intersection, @NotNull Vector topLeftPixelLocation) {
        double y = Math.floor((topLeftPixelLocation.getY() - intersection.getY()) / PIXEL_LENGTH);

        int multiplierX = direction.getMultiplierX();
        int multiplierZ = direction.getMultiplierZ();

        double xz = multiplierX * (intersection.getX() - topLeftPixelLocation.getX());
        xz += multiplierZ * (intersection.getZ() - topLeftPixelLocation.getZ());
        xz = Math.floor(xz / PIXEL_LENGTH);

        return new Point((int) xz, (int) y);
    }

    public record Intersection(@NotNull Screen<?> screen, @NotNull Point pixel, double distanceSquared) {
    }

}
