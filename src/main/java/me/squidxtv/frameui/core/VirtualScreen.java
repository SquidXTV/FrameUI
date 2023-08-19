package me.squidxtv.frameui.core;

import me.squidxtv.frameui.core.actions.initiator.ActionInitiator;
import me.squidxtv.frameui.core.actions.initiator.PlayerInitiator;
import me.squidxtv.frameui.core.content.ScreenModel;
import me.squidxtv.frameui.core.graphics.VirtualGraphics;
import me.squidxtv.frameui.core.math.Direction;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class VirtualScreen extends AbstractScreen<VirtualGraphics> {

    private final Set<Player> viewer = new HashSet<>();
    private @NotNull World world;
    private @NotNull Location topLeftFrameLocation;
    private @NotNull Direction direction;

    public VirtualScreen(@NotNull JavaPlugin plugin,
                         @NotNull ScreenModel model,
                         @NotNull World world,
                         @NotNull Location topLeftFrameLocation,
                         @NotNull Direction direction) {
        super(plugin, model, new VirtualGraphics(world, topLeftFrameLocation, direction, model.getWidth(), model.getHeight()));
        this.world = world;
        this.topLeftFrameLocation = topLeftFrameLocation;
        this.direction = direction;
    }

    @Override
    public boolean click(@NotNull ActionInitiator<?> initiator, int x, int y) {
        if (!super.click(initiator, x, y)) {
            return false;
        }

        if (initiator instanceof PlayerInitiator playerInitiator && (!containsViewer(playerInitiator.getInitiator()))) {
            return false;
        }

        return getModel().click(initiator, x, y);
    }

    @Override
    public boolean scroll(@NotNull ActionInitiator<?> initiator, @NotNull ScrollDirection direction, int x, int y) {
        if (!super.scroll(initiator, direction, x, y)) {
            return false;
        }

        if (initiator instanceof PlayerInitiator playerInitiator && (!containsViewer(playerInitiator.getInitiator()))) {
            return false;
        }

        return getModel().scroll(initiator, direction, x, y);
    }

    public @NotNull World getWorld() {
        return world;
    }

    public void setWorld(@NotNull World world) {
        this.world = world;
        getGraphics().setWorld(world);
    }

    public @NotNull Location getTopLeftFrameLocation() {
        return topLeftFrameLocation;
    }

    public void setTopLeftFrameLocation(@NotNull Location topLeftFrameLocation) {
        this.topLeftFrameLocation = topLeftFrameLocation;
        getGraphics().setLocation(topLeftFrameLocation);
    }

    public @NotNull Direction getDirection() {
        return direction;
    }

    public void setDirection(@NotNull Direction direction) {
        this.direction = direction;
        getGraphics().setDirection(direction);
    }

    public boolean containsViewer(@NotNull Player player) {
        return viewer.contains(player);
    }
}
