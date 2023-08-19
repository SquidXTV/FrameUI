package me.squidxtv.frameui.core.graphics;

import me.squidxtv.frameui.core.map.VirtualMap;
import me.squidxtv.frameui.core.math.Direction;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class VirtualGraphics extends AbstractGraphics<VirtualMap> {

    private @NotNull World world;
    private @NotNull Location location;
    private @NotNull Direction direction;

    public VirtualGraphics(@NotNull World world, @NotNull Location location, @NotNull Direction direction, int width, int height) {
        super(new VirtualMap[width * height], width, height);
        this.world = world;
        this.location = location;
        this.direction = direction;
    }

    @Override
    public void open() {
        super.open();
        initializeMaps();
    }

    @Override
    public void close() {
        super.close();
        closeMaps();
    }

    @Override
    public void remove() {
        super.remove();
        closeMaps();
    }

    private void initializeMaps() {
        VirtualMap[] maps = getMaps();
        for (int i = 0; i < getWidth(); i++) {
            for (int j = 0; j < getHeight(); j++) {
                int x = location.getBlockX() + (i * direction.getMultiplierX());
                int y = location.getBlockY() - j;
                int z = location.getBlockZ() + (i * direction.getMultiplierZ());

                int index = i + j * getWidth();
                maps[index] = new VirtualMap(world, new Location(world, x, y, z), direction);
            }
        }
    }

    private void closeMaps() {
        VirtualMap[] maps = getMaps();
        Arrays.fill(maps, null);
    }

    public @NotNull World getWorld() {
        return world;
    }

    public void setWorld(@NotNull World world) {
        this.world = world;
    }

    public @NotNull Location getLocation() {
        return location;
    }

    public void setLocation(@NotNull Location location) {
        this.location = location;
    }

    public @NotNull Direction getDirection() {
        return direction;
    }

    public void setDirection(@NotNull Direction direction) {
        this.direction = direction;
    }
}
