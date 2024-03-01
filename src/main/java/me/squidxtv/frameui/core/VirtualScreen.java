package me.squidxtv.frameui.core;

import me.squidxtv.frameui.core.actions.initiator.ActionInitiator;
import me.squidxtv.frameui.core.actions.initiator.PlayerInitiator;
import me.squidxtv.frameui.core.actions.scroll.ScrollDirection;
import me.squidxtv.frameui.core.content.ScreenModel;
import me.squidxtv.frameui.core.graphics.VirtualGraphics;
import me.squidxtv.frameui.core.itemframe.VirtualItemFrame;
import me.squidxtv.frameui.core.math.BoundingBox;
import me.squidxtv.frameui.core.math.Direction;
import me.squidxtv.frameui.core.math.IntersectionHelper;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class VirtualScreen extends AbstractScreen<VirtualGraphics> {

    private final Set<Player> viewer = new HashSet<>();
    private @NotNull World world;
    private @NotNull Location topLeftFrameLocation;
    private @NotNull Vector topLeftPixelPosition;
    private @NotNull Direction direction;

    public VirtualScreen(@NotNull JavaPlugin plugin,
                         @NotNull ScreenModel model,
                         @NotNull World world,
                         @NotNull Location topLeftFrameLocation,
                         @NotNull Direction direction) {
        super(plugin, model, new VirtualGraphics(model, world, topLeftFrameLocation, direction, model.getBlockWidth(), model.getBlockHeight()));
        this.world = world;
        this.topLeftFrameLocation = new Location(topLeftFrameLocation.getWorld(), topLeftFrameLocation.getBlockX(), topLeftFrameLocation.getBlockY(), topLeftFrameLocation.getBlockZ());
        this.direction = direction;
        this.topLeftPixelPosition = this.topLeftFrameLocation.toVector().add(direction.getTopLeftPixelOffset());
        this.topLeftPixelPosition.add(direction.getNormal().multiply(IntersectionHelper.PIXEL_LENGTH));
    }

    @Override
    public void open() {
        super.open();
        update();
        for (VirtualItemFrame frame : getGraphics().getItemFrames()) {
            frame.send(viewer);
        }
    }

    @Override
    public void close() {
        throwIfTerminated();
        VirtualItemFrame.destroy(getGraphics().getItemFrames(), viewer);
        super.close();
    }

    @Override
    public void update() {
        super.update();
        for (VirtualItemFrame frame : getGraphics().getItemFrames()) {
            frame.update(viewer);
        }
    }

    @Override
    public void terminate() {
        throwIfTerminated();
        VirtualItemFrame.destroy(getGraphics().getItemFrames(), viewer);
        super.terminate();
    }

    @Override
    public boolean click(@NotNull ActionInitiator initiator, int x, int y) {
        if (!super.click(initiator, x, y)) {
            return false;
        }

        if (initiator instanceof PlayerInitiator playerInitiator && !containsViewer(playerInitiator.player())) {
            return false;
        }

        getModel().click(initiator, x, y, new BoundingBox(0, 0, getGraphics().getPixelWidth(), getGraphics().getPixelHeight()));
        return true;
    }

    @Override
    public boolean scroll(@NotNull ActionInitiator initiator, @NotNull ScrollDirection direction, int x, int y) {
        if (!super.scroll(initiator, direction, x, y)) {
            return false;
        }

        if (initiator instanceof PlayerInitiator playerInitiator && !containsViewer(playerInitiator.player())) {
            return false;
        }

        getModel().scroll(initiator, direction, x, y, new BoundingBox(0, 0, getGraphics().getPixelWidth(), getGraphics().getPixelHeight()));
        return true;
    }

    @Override
    public @NotNull World getWorld() {
        return world;
    }

    @Override
    public void setWorld(@NotNull World world) {
        this.world = world;
        getGraphics().setWorld(world);
    }

    public @NotNull Location getTopLeftFrameLocation() {
        return topLeftFrameLocation.clone();
    }

    public void setTopLeftFrameLocation(@NotNull Location topLeftFrameLocation) {
        this.topLeftFrameLocation = topLeftFrameLocation;
        setTopLeftPixelPosition();
        getGraphics().setLocation(topLeftFrameLocation);
    }

    public @NotNull Vector getTopLeftPixelPosition() {
        return topLeftPixelPosition.clone();
    }

    private void setTopLeftPixelPosition() {
        topLeftPixelPosition = topLeftFrameLocation.toVector().add(direction.getTopLeftPixelOffset());
        topLeftPixelPosition.add(direction.getNormal().multiply(IntersectionHelper.PIXEL_LENGTH));
    }

    public @NotNull Direction getDirection() {
        return direction;
    }

    public void setDirection(@NotNull Direction direction) {
        this.direction = direction;
        setTopLeftPixelPosition();
        getGraphics().setDirection(direction);
    }

    @UnmodifiableView
    public @NotNull Collection<Player> getViewer() {
        return Collections.unmodifiableSet(viewer);
    }

    public boolean containsViewer(@NotNull Player player) {
        return viewer.contains(player);
    }

    public void addViewer(@NotNull Player player) {
        throwIfTerminated();

        if (!viewer.add(player)) {
            return;
        }

        if (getState() == State.CLOSED) {
            return;
        }

        for (VirtualItemFrame frame : getGraphics().getItemFrames()) {
            frame.send(player);
        }
    }

    public void addViewer(@NotNull Collection<Player> players) {
        throwIfTerminated();

        if (!viewer.addAll(players)) {
            return;
        }

        if (getState() == State.CLOSED) {
            return;
        }

        for (VirtualItemFrame frame : getGraphics().getItemFrames()) {
            frame.send(players);
        }
    }

    public void removeViewer(@NotNull Player player) {
        throwIfTerminated();

        if (!viewer.remove(player)) {
            return;
        }

        if (getState() == State.CLOSED) {
            return;
        }

        VirtualItemFrame.destroy(getGraphics().getItemFrames(), player);
    }

    public void removeViewer(@NotNull Collection<Player> players) {
        throwIfTerminated();

        if (!viewer.removeAll(players)) {
            return;
        }

        if (getState() == State.CLOSED) {
            return;
        }

        VirtualItemFrame.destroy(getGraphics().getItemFrames(), players);
    }

    public void clearViewer() {
        throwIfTerminated();

        if (getState() == State.OPEN) {
            VirtualItemFrame.destroy(getGraphics().getItemFrames(), viewer);
        }

        viewer.clear();
    }

}
