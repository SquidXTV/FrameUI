package me.squidxtv.frameui.math;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.ServicesManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import me.squidxtv.frameui.api.ScreenRegistry;
import me.squidxtv.frameui.api.ScreenRegistryImpl;
import me.squidxtv.frameui.core.Screen;
import me.squidxtv.frameui.core.ScreenLocation;

import java.awt.Point;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

class IntersectionHelperTest {

    private static MockedStatic<Bukkit> bukkit;

    @BeforeAll
    public static void setUp() {
        bukkit = mockStatic(Bukkit.class);
        ServicesManager manager = mock(ServicesManager.class);
        when(manager.load(ScreenRegistry.class)).thenReturn(new ScreenRegistryImpl());
        when(Bukkit.getServicesManager()).thenReturn(manager);
    }

    @AfterAll
    public static void tearDown() {
        bukkit.close();
    }

    @Test
    void testGetNearestIntersection_noScreens() {
        Player player = mock();
        List<Screen> screens = Collections.emptyList();
        Optional<Intersection> nearestIntersection = IntersectionHelper.getNearestIntersection(screens, player, _ -> 0);
        assertTrue(nearestIntersection.isEmpty());
    }

    @Test
    void testGetIntersection_north() {
        Screen screen = mock(Screen.class);
        Location origin = new Location(mock(), 0, 0, 0);
        ScreenLocation location = new ScreenLocation(origin, Direction.NORTH);
        when(screen.getLocation()).thenReturn(location);
        when(screen.getPixelWidth()).thenReturn(128);
        when(screen.getPixelHeight()).thenReturn(128);

        Player player = mock(Player.class);

        Location eyeLocation = new Location(mock(), 1, 1, 0, 0, 0);
        when(player.getEyeLocation()).thenReturn(eyeLocation);
        testIntersection(screen, player, new Point(0, 0));

        eyeLocation = new Location(mock(), IntersectionHelper.PIXEL_LENGTH, IntersectionHelper.PIXEL_LENGTH, 0, 0, 0);
        when(player.getEyeLocation()).thenReturn(eyeLocation);
        testIntersection(screen, player, new Point(127, 127));

        // from behind
        eyeLocation = new Location(mock(), 1, 1, 2, 180, 0);
        when(player.getEyeLocation()).thenReturn(eyeLocation);
        Optional<Intersection> intersection = IntersectionHelper.getIntersection(screen, player, Double.POSITIVE_INFINITY);
        assertTrue(intersection.isEmpty());
    }

    @Test
    void testGetIntersection_south() {
        Screen screen = mock(Screen.class);
        Location origin = new Location(mock(), 0, 0, 0);
        ScreenLocation location = new ScreenLocation(origin, Direction.SOUTH);
        when(screen.getLocation()).thenReturn(location);
        when(screen.getPixelWidth()).thenReturn(128);
        when(screen.getPixelHeight()).thenReturn(128);

        Player player = mock(Player.class);

        Location eyeLocation = new Location(mock(), 0, 1, 0, 180, 0);
        when(player.getEyeLocation()).thenReturn(eyeLocation);
        testIntersection(screen, player, new Point(0, 0));

        eyeLocation = new Location(mock(), 1 - IntersectionHelper.PIXEL_LENGTH, IntersectionHelper.PIXEL_LENGTH, 0, 180, 0);
        when(player.getEyeLocation()).thenReturn(eyeLocation);
        testIntersection(screen, player, new Point(127, 127));

        // from behind
        eyeLocation = new Location(mock(), 0, 1, -1, 0, 0);
        when(player.getEyeLocation()).thenReturn(eyeLocation);
        Optional<Intersection> intersection = IntersectionHelper.getIntersection(screen, player, Double.POSITIVE_INFINITY);
        assertTrue(intersection.isEmpty());
    }

    @Test
    void testGetIntersection_west() {
        Screen screen = mock(Screen.class);
        Location origin = new Location(mock(), 0, 0, 0);
        ScreenLocation location = new ScreenLocation(origin, Direction.WEST);
        when(screen.getLocation()).thenReturn(location);
        when(screen.getPixelWidth()).thenReturn(128);
        when(screen.getPixelHeight()).thenReturn(128);

        Player player = mock(Player.class);

        Location eyeLocation = new Location(mock(), 0, 1, 0, -90, 0);
        when(player.getEyeLocation()).thenReturn(eyeLocation);
        testIntersection(screen, player, new Point(0, 0));

        eyeLocation = new Location(mock(), 0, IntersectionHelper.PIXEL_LENGTH, 1 - IntersectionHelper.PIXEL_LENGTH, -90, 0);
        when(player.getEyeLocation()).thenReturn(eyeLocation);
        testIntersection(screen, player, new Point(127, 127));

        // from behind
        eyeLocation = new Location(mock(), 2, 1, 0, 90, 0);
        when(player.getEyeLocation()).thenReturn(eyeLocation);
        Optional<Intersection> intersection = IntersectionHelper.getIntersection(screen, player, Double.POSITIVE_INFINITY);
        assertTrue(intersection.isEmpty());
    }

    @Test
    void testGetIntersection_east() {
        Screen screen = mock(Screen.class);
        Location origin = new Location(mock(), 0, 0, 0);
        ScreenLocation location = new ScreenLocation(origin, Direction.EAST);
        when(screen.getLocation()).thenReturn(location);
        when(screen.getPixelWidth()).thenReturn(128);
        when(screen.getPixelHeight()).thenReturn(128);

        Player player = mock(Player.class);

        Location eyeLocation = new Location(mock(), 1, 1, 1, 90, 0);
        when(player.getEyeLocation()).thenReturn(eyeLocation);
        testIntersection(screen, player, new Point(0, 0));

        eyeLocation = new Location(mock(), 0, IntersectionHelper.PIXEL_LENGTH, IntersectionHelper.PIXEL_LENGTH, 90, 0);
        when(player.getEyeLocation()).thenReturn(eyeLocation);
        testIntersection(screen, player, new Point(127, 127));

        // from behind
        eyeLocation = new Location(mock(), -1, 1, 1, -90, 0);
        when(player.getEyeLocation()).thenReturn(eyeLocation);
        Optional<Intersection> intersection = IntersectionHelper.getIntersection(screen, player, Double.POSITIVE_INFINITY);
        assertTrue(intersection.isEmpty());
    }

    @Test
    void testGetIntersection_outOfBounds() {
        Screen screen = mock(Screen.class);
        Location origin = new Location(mock(), 0, 0, 0);
        ScreenLocation location = new ScreenLocation(origin, Direction.NORTH);
        when(screen.getLocation()).thenReturn(location);
        when(screen.getPixelWidth()).thenReturn(128);
        when(screen.getPixelHeight()).thenReturn(128);

        Player player = mock(Player.class);

        Location eyeLocation = new Location(mock(), 1 + IntersectionHelper.PIXEL_LENGTH, 1, 0, 0, 0);
        when(player.getEyeLocation()).thenReturn(eyeLocation);
        Optional<Intersection> intersection1 = IntersectionHelper.getIntersection(screen, player, Double.POSITIVE_INFINITY);
        assertTrue(intersection1.isEmpty());

        eyeLocation = new Location(mock(), 1, 1 + IntersectionHelper.PIXEL_LENGTH, 0, 0, 0);
        when(player.getEyeLocation()).thenReturn(eyeLocation);
        Optional<Intersection> intersection2 = IntersectionHelper.getIntersection(screen, player, Double.POSITIVE_INFINITY);
        assertTrue(intersection2.isEmpty());

        eyeLocation = new Location(mock(), 0 + IntersectionHelper.PIXEL_LENGTH, 0, 0, 0, 0);
        when(player.getEyeLocation()).thenReturn(eyeLocation);
        Optional<Intersection> intersection3 = IntersectionHelper.getIntersection(screen, player, Double.POSITIVE_INFINITY);
        assertTrue(intersection3.isEmpty());

        eyeLocation = new Location(mock(), 0, 0 + IntersectionHelper.PIXEL_LENGTH, 0, 0, 0);
        when(player.getEyeLocation()).thenReturn(eyeLocation);
        Optional<Intersection> intersection4 = IntersectionHelper.getIntersection(screen, player, Double.POSITIVE_INFINITY);
        assertTrue(intersection4.isEmpty());
    }

    @Test
    void testGetIntersection_outOfRange() {
        Screen screen = mock(Screen.class);
        Location origin = new Location(mock(), 0, 0, 4);
        ScreenLocation location = new ScreenLocation(origin, Direction.NORTH);
        when(screen.getLocation()).thenReturn(location);
        when(screen.getPixelWidth()).thenReturn(128);
        when(screen.getPixelHeight()).thenReturn(128);

        Player player = mock(Player.class);

        Location eyeLocation = new Location(mock(), 1, 1, 0, 0, 0);
        when(player.getEyeLocation()).thenReturn(eyeLocation);

        Optional<Intersection> outOfRange = IntersectionHelper.getIntersection(screen, player, 3 * 3);
        assertTrue(outOfRange.isEmpty());

        Optional<Intersection> inRange = IntersectionHelper.getIntersection(screen, player, 5 * 5);
        assertTrue(inRange.isPresent());
    }

    @Test
    void testGetNearestIntersection() {
        Screen first = mock(Screen.class);
        Location firstOrigin = new Location(mock(), 0, 0, 0);
        ScreenLocation firstLocation = new ScreenLocation(firstOrigin, Direction.NORTH);
        when(first.getState()).thenReturn(Screen.State.OPEN);
        when(first.getLocation()).thenReturn(firstLocation);
        when(first.getPixelWidth()).thenReturn(128);
        when(first.getPixelHeight()).thenReturn(128);

        Screen second = mock(Screen.class);
        Location secondOrigin = new Location(mock(), 0, 0, 1);
        ScreenLocation secondLocation = new ScreenLocation(secondOrigin, Direction.NORTH);
        when(second.getState()).thenReturn(Screen.State.OPEN);
        when(second.getLocation()).thenReturn(secondLocation);
        when(second.getPixelWidth()).thenReturn(128);
        when(second.getPixelHeight()).thenReturn(128);

        Player player = mock(Player.class);
        Location eyeLocation = new Location(mock(), 1, 1, 0, 0, 0);
        when(player.getEyeLocation()).thenReturn(eyeLocation);

        Optional<Intersection> nearest = IntersectionHelper.getNearestIntersection(List.of(first, second), player,
                _ -> Double.POSITIVE_INFINITY);
        assertTrue(nearest.isPresent());
        assertEquals(first, nearest.get().screen());
    }

    private static void testIntersection(Screen screen, Player player, Point expected) {
        Optional<Intersection> optional = IntersectionHelper.getIntersection(screen, player, Double.POSITIVE_INFINITY);
        assertTrue(optional.isPresent());
        Intersection intersection = optional.get();
        assertEquals(expected, intersection.pixel());
    }

}
