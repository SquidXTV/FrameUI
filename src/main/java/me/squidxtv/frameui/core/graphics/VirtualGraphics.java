package me.squidxtv.frameui.core.graphics;

import me.squidxtv.frameui.core.content.ScreenModel;
import me.squidxtv.frameui.core.itemframe.VirtualItemFrame;
import me.squidxtv.frameui.core.math.Direction;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class VirtualGraphics extends AbstractGraphics<VirtualItemFrame> {

    private @NotNull World world;
    private @NotNull Location location;
    private @NotNull Direction direction;

    public VirtualGraphics(@NotNull ScreenModel model, @NotNull World world, @NotNull Location location, @NotNull Direction direction) {
        super(model, new VirtualItemFrame[model.getBlockWidth() * model.getBlockHeight()]);
        this.world = world;
        this.location = location;
        this.direction = direction;
    }

    @Override
    public void open() {
        super.open();
        initializeFrames();
    }

    @Override
    public void close() {
        super.close();
        closeFrames();
    }


    @Override
    public void terminate() {
        super.terminate();
        closeFrames();
    }

    private void initializeFrames() {
        VirtualItemFrame[] frames = getItemFrames();
        for (int i = 0; i < getModel().getBlockHeight(); i++) {
            for (int j = 0; j < getModel().getBlockWidth(); j++) {
                int x = location.getBlockX() + (j * direction.getMultiplierX());
                int y = location.getBlockY() - i;
                int z = location.getBlockZ() + (j * direction.getMultiplierZ());

                int index = j + i * getModel().getBlockWidth();
                frames[index] = new VirtualItemFrame(world, new Location(world, x, y, z), direction);
            }
        }
    }

    private void closeFrames() {
        Arrays.fill(getItemFrames(), null);
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
        VirtualItemFrame[] frames = getItemFrames();
        for (int i = 0; i < getModel().getBlockHeight(); i++) {
            for (int j = 0; j < getModel().getBlockWidth(); j++) {
                int x = location.getBlockX() + (i * direction.getMultiplierX());
                int y = location.getBlockY() - j;
                int z = location.getBlockZ() + (i * direction.getMultiplierZ());

                int index = i + j * getModel().getBlockWidth();
                frames[index].setLocation(new Location(world, x, y, z));
            }
        }
    }

    public @NotNull Direction getDirection() {
        return direction;
    }

    public void setDirection(@NotNull Direction direction) {
        this.direction = direction;
        for (VirtualItemFrame frame : getItemFrames()) {
            frame.setDirection(direction);
        }
    }

}
