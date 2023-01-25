package me.squidxtv.frameui.core;

import me.squidxtv.frameui.FrameUI;
import me.squidxtv.frameui.core.content.ElementNode;
import me.squidxtv.frameui.packets.ItemFramePacket;
import me.squidxtv.frameui.packets.PacketManager;
import me.squidxtv.frameui.util.BufferedImageUtil;
import me.squidxtv.frameui.core.properties.Direction;
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
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;
import java.util.logging.Logger;

/**
 * This class represents a Screen in the Minecraft World.
 * A screen consist of invisible item frames with maps inside.
 * These maps use a custom renderer {@link FrameRenderer} which enables custom
 * {@link BufferedImage} on these maps. One image gets split into multiple smaller with size of
 * 128x128 and then rendered on the correct position in the item frames.
 */
public class Screen {

    private static final @NotNull Logger LOGGER = JavaPlugin.getPlugin(FrameUI.class).getLogger();

    /**
     * Unique Identifier for this Screen.
     */
    private final @NotNull UUID uuid;

    /**
     * Identifier for this Screen specified in the xml file.
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
     * Width and height should be multiples of 128px, as
     * Minecraft maps can render 128x128 images.
     * You can replace not used space with black (0, 0, 0)
     * pixels because that is transparent.
     */
    private @NotNull BufferedImage background;

    /**
     * Content drawn on the background.
     */
    private @NotNull ElementNode[] content;

    /**
     * Maps inside the item frames.
     */
    private @NotNull ItemStack[][] maps;

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
    private @NotNull ItemFramePacket[][] packets;

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
    private @Nullable Permission clickPermission;

    /**
     * Permission to restrict usage of scrolling.
     */
    private @Nullable Permission scrollPermission;

    /**
     * State of Screen, either OPEN or CLOSED.
     */
    public enum State {
        OPENED,
        CLOSED
    }

    public Screen(ScreenModel screenModel, Location location, World world, Direction direction) {
        this.uuid = UUID.randomUUID();
        setXMLBlueprint(screenModel);
        this.location = location;
        this.direction = direction;
        this.world = world;
        this.viewer = new HashSet<>();
        this.state = State.CLOSED;
        clickPermission = null;
        scrollPermission = null;
        packetManager = JavaPlugin.getPlugin(FrameUI.class).getPacketManager();

        this.frameIDs = new int[height][width];
        this.packets = new ItemFramePacket[height][width];
        this.maps = new ItemStack[height][width];
    }

    public Screen(ScreenModel screenModel, Location location, World world) {
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
                    ItemStack map = MapUtil.construct(viewer, world);
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

            packetManager.removeItemFrames(viewer, frameIDs);
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

    private BufferedImage generateCurrentState() {
        BufferedImage image = BufferedImageUtil.deepCopy(background);
        Graphics g = image.getGraphics();

        for (ElementNode element : content) {
            element.draw(g);
        }

        return image;
    }

    private void updateImage(ItemStack map, BufferedImage updated) {
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

    public void click(Player player, int x, int y) {
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

    public void scroll(Player player, int direction, int speed, int x, int y) {
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

    public synchronized void addViewer(Player player) {
        if (state == State.OPENED) {
            for (ItemFramePacket[] row : packets) {
                for (ItemFramePacket packet : row) {
                    packetManager.sendItemFramePacket(List.of(player), packet);
                }
            }
        }
        viewer.add(player);
    }

    public synchronized void addViewer(Collection<Player> players) {
        if (state == State.OPENED) {
            for (ItemFramePacket[] row : packets) {
                for (ItemFramePacket packet : row) {
                    packetManager.sendItemFramePacket(players, packet);
                }
            }
        }
        viewer.addAll(players);
    }

    public synchronized void removeViewer(Player player) {
        if (state == State.OPENED && player.isOnline()) {
            packetManager.removeItemFrames(List.of(player), frameIDs);
        }
        viewer.remove(player);
    }

    public synchronized void removeViewer(Collection<Player> players) {
        if (state == State.OPENED) {
            packetManager.removeItemFrames(players, frameIDs);
        }
        viewer.removeAll(players);
    }

    public synchronized void clearViewer() {
        if (state == State.OPENED) {
            packetManager.removeItemFrames(viewer, frameIDs);
        }
        viewer.clear();
    }

    public boolean containsViewer(Player player) {
        return viewer.contains(player);
    }

    public synchronized void setLocation(Location location) {
        this.location = location;
    }

    public synchronized void setDirection(Direction direction) {
        this.direction = direction;
    }

    public synchronized void setWorld(World world) {
        this.world = world;
    }

    public void setClickPermission(@Nullable Permission clickPermission) {
        this.clickPermission = clickPermission;
    }

    public void setScrollPermission(@Nullable Permission scrollPermission) {
        this.scrollPermission = scrollPermission;
    }

    public final synchronized void setXMLBlueprint(ScreenModel screenModel) {
        this.id = screenModel.getId();
        this.width = screenModel.getWidth();
        this.height = screenModel.getHeight();
        this.background = screenModel.getBackground();
        this.content = screenModel.getChildNodes();
    }

    public String getId() {
        return id;
    }

    public Location getLocation() {
        return location;
    }

    public Direction getDirection() {
        return direction;
    }

    public World getWorld() {
        return world;
    }

    public State getState() {
        return state;
    }

    public List<ElementNode> getContentById(String id) {
        return Arrays.stream(content).filter(elementNode -> elementNode.getId().equals(id)).toList();
    }

    public Set<Player> getViewer() {
        return viewer;
    }

    public @Nullable Permission getClickPermission() {
        return clickPermission;
    }

    public @Nullable Permission getScrollPermission() {
        return scrollPermission;
    }
}
