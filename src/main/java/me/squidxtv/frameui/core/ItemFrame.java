/*
 * FrameUI: Minecraft plugin library designed to easily create screens within a server.
 * Copyright (C) 2023-2024 Connor Schweighöfer
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package me.squidxtv.frameui.core;

import io.github.retrooper.packetevents.util.SpigotReflectionUtil;
import org.bukkit.Location;

import me.squidxtv.frameui.math.Direction;

/**
 * The {@code ItemFrame} represents an item frame in the Minecraft World that
 * displays a {@link MapItem}.
 */
public class ItemFrame {

    private final int entityId = SpigotReflectionUtil.generateEntityId();

    private final Location location;
    private final Direction direction;
    private final MapItem mapItem;

    private boolean invisible;

    /**
     * Creates a new {@code ItemFrame}.
     * 
     * @param location  the location of the item frame
     * @param direction the direction the item frame is facing
     * @param mapItem   the {@link MapItem} to be displayed in the item frame
     * @param invisible {@code true} if the item frame should be invisible,
     *                  {@code false} otherwise
     */
    public ItemFrame(Location location, Direction direction, MapItem mapItem, boolean invisible) {
        this.location = location;
        this.direction = direction;
        this.mapItem = mapItem;
        this.invisible = invisible;
    }

    /**
     * Returns the entity ID of the item frame.
     * 
     * @return the entity ID of the item frame
     */
    public int getEntityId() {
        return entityId;
    }

    /**
     * Returns the location of the item frame.
     * 
     * @return the location of the item frame
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Returns the direction the item frame is facing.
     * 
     * @return the direction the item frame is facing
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * Returns the {@link MapItem} displayed in the item frame.
     * 
     * @return the {@link MapItem} displayed in the item frame
     */
    public MapItem getMapItem() {
        return mapItem;
    }

    /**
     * Returns {@code true} if this item frame is invisible.
     * 
     * @return {@code true} if the item frame is invisible, {@code false} otherwise
     */
    public boolean isInvisible() {
        return invisible;
    }

    /**
     * Sets the visibility of the item frame. Changes might not be directly visible
     * and could require a refresh to appear.
     * 
     * @param invisible the new visibility of the item frame
     */
    public void setInvisible(boolean invisible) {
        this.invisible = invisible;
    }

}
