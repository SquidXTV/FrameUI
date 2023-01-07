package me.squidxtv.frameui.core;

import me.squidxtv.frameui.FrameUI;
import me.squidxtv.frameui.core.content.ElementNode;
import me.squidxtv.frameui.packets.ItemFramePacket;
import me.squidxtv.frameui.packets.PacketManager;
import me.squidxtv.frameui.util.BufferedImageUtil;
import me.squidxtv.frameui.util.Direction;
import me.squidxtv.frameui.util.FrameRenderer;
import me.squidxtv.frameui.util.MapUtil;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 */
public final class Screen {

    private static final @NotNull Logger LOGGER = org.bukkit.plugin.java.JavaPlugin.getPlugin(FrameUI.class).getLogger();

    /**
     * Unique Identifier for this Screen.
     */
    private final @NotNull UUID uuid;

    /**
     * Identifier for this Screen.
     */
    private @NotNull String id;

    /**
     * Width in Minecraft blocks.
     */
    private int width;

    /**
     * Height in Minecraft blocks.
     */
    private int height;

    /**
     * Background image rendered on this Screen.
     */
    private BufferedImage background;

    /**
     * Content drawn on the background.
     */
    private ElementNode[] content;

    /**
     * Maps inside the item frames.
     */
    private ItemStack[][] maps;

    /**
     * Entity identifier for Item Frames. Used to send destroy
     * packets later.
     */
    private int[][] frameIDs;

    /**
     * Manager to send packets to the player.
     */
    private final @NotNull PacketManager packetManager;

    /**
     * Current packets send. Used to send same packets to new viewer.
     */
    private ItemFramePacket[][] packets;

    /**
     * Top-left's item frame location.
     */
    private @NotNull Location location;

    /**
     * Direction of Item frames.
     */
    private @NotNull Direction direction;

    /**
     * World of Item frames.
     */
    private @NotNull World world;

    /**
     * State of Screen.
     */
    private @NotNull State state;

    /**
     * Set of the current viewer.
     */
    private final @NotNull Set<Player> viewer;

    /**
     * Permission to restrict usage of clicking.
     */
    private Permission clickPermission;

    /**
     * Permission to restrict usage of scrolling.
     */
    private Permission scrollPermission;

    /**
     * State of Screen, either OPEN or CLOSED.
     */
    public enum State {
        OPENED,
        CLOSED
    }

    public Screen(@NotNull ScreenModel screenModel, @NotNull Location location, @NotNull World world, @NotNull Direction direction) {
        this.uuid = UUID.randomUUID();
        setXMLBlueprint(screenModel);
        this.location = location;
        this.direction = direction;
        this.world = world;
        this.viewer = new HashSet<>();
        this.state = State.CLOSED;
        clickPermission = null;
        scrollPermission = null;
        packetManager = org.bukkit.plugin.java.JavaPlugin.getPlugin(FrameUI.class).getPacketManager();

        this.frameIDs = new int[height][width];
        this.packets = new ItemFramePacket[height][width];
        this.maps = new ItemStack[height][width];
    }

    public Screen(@NotNull ScreenModel screenModel, @NotNull Location location, @NotNull World world) {
        this(screenModel, location, world, Direction.NORTH);
    }

    public synchronized void open() {
        synchronized (this) {
            if (state == State.OPENED) {
                close();
            }

            this.frameIDs = new int[height][width];
            this.packets = new ItemFramePacket[height][width];
            this.maps = new ItemStack[height][width];

            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    ItemStack map = MapUtil.construct(uuid, viewer, world, i, j);
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
            update();
        }
    }

    public synchronized void close() {
        synchronized (this) {
            if (state == State.CLOSED) {
                return;
            }

            packetManager.destroyItemFrame(viewer, frameIDs);
            frameIDs = new int[height][width];
            packets = new ItemFramePacket[height][width];

            state = State.CLOSED;
        }
    }

    public synchronized void update() {
        synchronized (this) {
            if (state == State.CLOSED) {
                return;
            }

            BufferedImage currentFrame = generateCurrentState();
            BufferedImage[][] sections = BufferedImageUtil.split(currentFrame, width, height);

            for (int i = 0; i < sections.length; i++) {
                for (int j = 0; j < sections[i].length; j++) {
                    BufferedImage part = sections[i][j];
                    ItemStack map = maps[i][j];

                    updateImage(map, part);
                }
            }
        }
    }

    private @NotNull BufferedImage generateCurrentState() {
        BufferedImage image = BufferedImageUtil.deepCopy(background);
        Graphics g = image.getGraphics();

        for (ElementNode element : content) {
            element.draw(g);
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

        for (ElementNode element : content) {
            element.click(x, y);
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

        for (ElementNode element : content) {
            element.scroll(direction, speed, x, y);
        }
    }

    public synchronized void addViewer(@NotNull Player player) {
        if (state == State.OPENED) {
            for (ItemFramePacket[] row : packets) {
                for (ItemFramePacket packet : row) {
                    packetManager.sendItemFramePacket(List.of(player), packet);
                }
            }
        }
        viewer.add(player);
    }

    public synchronized void addViewer(@NotNull Collection<Player> players) {
        if (state == State.OPENED) {
            for (ItemFramePacket[] row : packets) {
                for (ItemFramePacket packet : row) {
                    packetManager.sendItemFramePacket(players, packet);
                }
            }
        }
        viewer.addAll(players);
    }

    public synchronized void removeViewer(@NotNull Player player) {
        if (state == State.OPENED && player.isOnline()) {
            packetManager.destroyItemFrame(List.of(player), frameIDs);
        }
        viewer.remove(player);
    }

    public synchronized void removeViewer(@NotNull Collection<Player> players) {
        if (state == State.OPENED) {
            packetManager.destroyItemFrame(players, frameIDs);
        }
        viewer.removeAll(players);
    }

    public synchronized void clearViewer() {
        if (state == State.OPENED) {
            packetManager.destroyItemFrame(viewer, frameIDs);
        }
        viewer.clear();
    }

    public boolean containsViewer(@NotNull Player player) {
        return viewer.contains(player);
    }

    public synchronized void setLocation(@NotNull Location location) {
        this.location = location;
    }

    public synchronized void setDirection(@NotNull Direction direction) {
        this.direction = direction;
    }

    public synchronized void setWorld(@NotNull World world) {
        this.world = world;
    }

    public void setClickPermission(Permission clickPermission) {
        this.clickPermission = clickPermission;
    }

    public void setScrollPermission(Permission scrollPermission) {
        this.scrollPermission = scrollPermission;
    }

    public synchronized void setXMLBlueprint(@NotNull ScreenModel screenModel) {
        this.id = screenModel.getId();
        this.width = screenModel.getWidth();
        this.height = screenModel.getHeight();
        this.background = screenModel.getBackground();
        this.content = screenModel.getChildNodes();
    }

    public @NotNull String getId() {
        return id;
    }

    public @NotNull Location getLocation() {
        return location;
    }

    public @NotNull Direction getDirection() {
        return direction;
    }

    public @NotNull World getWorld() {
        return world;
    }

    public @NotNull State getState() {
        return state;
    }

    public List<ElementNode> getContentById(String id) {
        return Arrays.stream(content).filter(elementNode -> elementNode.getId().equals(id)).toList();
    }

    public @NotNull Set<Player> getViewer() {
        return viewer;
    }

    public Permission getClickPermission() {
        return clickPermission;
    }

    public Permission getScrollPermission() {
        return scrollPermission;
    }
}
