package com.squidxtv.frameui.core;

import com.squidxtv.frameui.FrameUI;
import com.squidxtv.frameui.core.content.Component;
import com.squidxtv.frameui.packets.ItemFramePacket;
import com.squidxtv.frameui.packets.PacketManager;
import com.squidxtv.frameui.util.BufferedImageUtil;
import com.squidxtv.frameui.util.Direction;
import com.squidxtv.frameui.util.FrameRenderer;
import com.squidxtv.frameui.util.MapUtil;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.image.*;
import java.util.*;
import java.util.List;
import java.util.logging.Logger;

public final class Screen {

    private static final @NotNull Logger LOGGER = JavaPlugin.getPlugin(FrameUI.class).getLogger();

    private final @NotNull UUID id;

    private final @NotNull PacketManager packetManager;
    private ItemFramePacket[][] packets;

    // location
    /**
     * Top-left's item frame location
     */
    private @NotNull Location location;
    private @NotNull Direction direction;
    private @NotNull World world;

    // size
    private final int width;
    private final int height;

    // content
    private @NotNull BufferedImage background;
    private final @NotNull List<Component> content;
    private ItemStack[][] maps;
    private int[][] frameIDs;

    private @NotNull State state;
    private final @NotNull Set<Player> viewer;
    private Permission clickPermission;
    private Permission scrollPermission;

    public enum State {
        OPENED,
        CLOSED
    }

    public static class Builder {
        private final @NotNull Location location;
        private final @NotNull BufferedImage background;
        private final @NotNull World world;
        private final @NotNull Set<Player> viewer;

        private Permission clickPermission;

        private @NotNull Direction direction = Direction.NORTH;
        private int width = 5;
        private int height = 3;

        public Builder(@NotNull Location location, @NotNull BufferedImage background, @NotNull World world, @NotNull Collection<Player> viewer) {
            this.location = location;
            this.background = background;
            this.world = world;
            this.viewer = new HashSet<>(viewer);
        }

        public @NotNull Builder direction(@NotNull Direction direction) {
            this.direction = direction;
            return this;
        }

        public @NotNull Builder width(int width) {
            this.width = width;
            return this;
        }

        public @NotNull Builder height(int height) {
            this.height = height;
            return this;
        }

        public @NotNull Builder clickPermission(Permission clickPermission) {
            this.clickPermission = clickPermission;
            return this;
        }

        public @NotNull Screen build() {
            return new Screen(this);
        }
    }

    private Screen(@NotNull Builder builder) {
        this.packetManager = JavaPlugin.getPlugin(FrameUI.class).getPacketManager();
        this.id = UUID.randomUUID();
        this.state = State.CLOSED;

        this.location = builder.location;
        this.direction = builder.direction;
        this.world = builder.world;

        this.width = builder.width;
        this.height = builder.height;

        this.background = builder.background;
        this.content = new ArrayList<>();
        this.frameIDs = new int[height][width];
        this.packets = new ItemFramePacket[height][width];
        this.maps = new ItemStack[height][width];

        this.viewer = builder.viewer;
        this.clickPermission = builder.clickPermission;
    }

    public void open() {
        if (state == State.OPENED) {
            close();
        }

        this.frameIDs = new int[height][width];
        this.packets = new ItemFramePacket[height][width];
        this.maps = new ItemStack[height][width];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                ItemStack map = MapUtil.construct(id, viewer, world, i, j);
                maps[i][j] = map;

                int x = location.getBlockX() + (j * direction.getMultiplierX());
                int y = location.getBlockY() - i;
                int z = location.getBlockZ() + (j * direction.getMultiplierZ());

                ItemFramePacket packet = packetManager.createItemFrame(map, x, y, z, true, direction);
                packets[i][j] = packet;
                frameIDs[i][j] = packet.entityID();
                packetManager.sendItemFramePacket(viewer, packet);
            }
        }

        state = State.OPENED;
        render();
    }

    public void close() {
        if (state == State.CLOSED) {
            return;
        }

        packetManager.destroyItemFrame(viewer, frameIDs);
        frameIDs = new int[height][width];
        packets = new ItemFramePacket[height][width];

        state = State.CLOSED;
    }

    public synchronized void render() {
        if (state == State.CLOSED) {
            return;
        }

        BufferedImage currentFrame = calculateFrame();
        BufferedImage[][] sections = BufferedImageUtil.split(currentFrame, width, height);

        for (int i = 0; i < sections.length; i++) {
            for (int j = 0; j < sections[i].length; j++) {
                BufferedImage part = sections[i][j];
                ItemStack map = maps[i][j];

                updateImage(map, part);
            }
        }
    }

    public void click(@NotNull Player player, int x, int y) {
        if (state == State.CLOSED) {
            return;
        }

        if (!viewer.contains(player)) {
            return;
        }

        if (clickPermission != null && !player.hasPermission(clickPermission)) {
            return;
        }

        for (Component component : content) {
            component.click(x, y);
        }
    }

    public void scroll(@NotNull Player player, int direction, int speed, int x, int y) {
        if (state == State.CLOSED) {
            return;
        }

        if (!viewer.contains(player)) {
            return;
        }

        if (scrollPermission != null && !player.hasPermission(scrollPermission)) {
            return;
        }

        for (Component component : content) {
            component.scroll(direction, speed, x, y);
        }
    }

    private @NotNull BufferedImage calculateFrame() {
        BufferedImage image = BufferedImageUtil.deepCopy(background);
        Graphics g = image.getGraphics();

        for (Component component : content) {
            component.draw(g);
        }

        return image;
    }

    private void updateImage(@NotNull ItemStack map, @NotNull BufferedImage updated) {
        ItemMeta meta = map.getItemMeta();

        if (meta == null) {
            LOGGER.warning("Could not update image, because meta is null.");
            return;
        }

        MapView view = ((MapMeta) meta).getMapView();

        if (view == null) {
            LOGGER.warning("Could not update image, because view is null.");
            return;
        }

        List<MapRenderer> renderers = view.getRenderers();

        FrameRenderer renderer = new FrameRenderer();
        for (MapRenderer mapRenderer : renderers) {
            if (mapRenderer instanceof FrameRenderer imageRenderer) {
                renderer = imageRenderer;
                break;
            }
        }
        renderer.setImage(updated);
    }

    public @NotNull Collection<Component> getContent() {
        return Collections.unmodifiableList(content);
    }

    public void addContent(@NotNull Collection<Component> components) {
        content.addAll(components);
    }

    public void addContent(@NotNull Component component) {
        content.add(component);
    }

    public void removeContent(@NotNull Collection<Component> components) {
        content.removeAll(components);
    }

    public void removeContent(@NotNull Component component) {
        content.remove(component);
    }

    public void clearContent() {
        content.clear();
    }

    public void addViewer(@NotNull Player player) {
        if (state == State.OPENED) {
            for (ItemFramePacket[] row : packets) {
                for (ItemFramePacket packet : row) {
                    packetManager.sendItemFramePacket(List.of(player), packet);
                }
            }
        }
        viewer.add(player);
    }

    public void addViewer(@NotNull Collection<Player> players) {
        if (state == State.OPENED) {
            for (ItemFramePacket[] row : packets) {
                for (ItemFramePacket packet : row) {
                    packetManager.sendItemFramePacket(players, packet);
                }
            }
        }
        viewer.addAll(players);
    }

    public void removeViewer(@NotNull Player player) {
        if (state == State.OPENED) {
            packetManager.destroyItemFrame(List.of(player), frameIDs);
        }
        viewer.remove(player);
    }

    public void removeViewer(@NotNull Collection<Player> players) {
        if (state == State.OPENED) {
            packetManager.destroyItemFrame(players, frameIDs);
        }
        viewer.removeAll(players);
    }

    public void clearViewer() {
        if (state == State.OPENED) {
            packetManager.destroyItemFrame(viewer, frameIDs);
        }
        viewer.clear();
    }

    public boolean containsViewer(@NotNull Player player) {
        return viewer.contains(player);
    }

    public void setLocation(@NotNull Location location) {
        this.location = location;
    }

    public void setDirection(@NotNull Direction direction) {
        this.direction = direction;
    }

    public void setWorld(@NotNull World world) {
        this.world = world;
    }

    public void setBackground(@NotNull BufferedImage background) {
        this.background = background;
    }

    public void setClickPermission(@NotNull Permission clickPermission) {
        this.clickPermission = clickPermission;
    }

    public void setScrollPermission(Permission scrollPermission) {
        this.scrollPermission = scrollPermission;
    }


    public @NotNull State getState() {
        return this.state;
    }

    public @NotNull Direction getDirection() {
        return direction;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public @NotNull UUID getId() {
        return id;
    }

    public @NotNull Location getLocation() {
        return location;
    }

    public @NotNull World getWorld() {
        return world;
    }
}
