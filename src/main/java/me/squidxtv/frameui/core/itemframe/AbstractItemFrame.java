package me.squidxtv.frameui.core.itemframe;

import me.squidxtv.frameui.core.map.Map;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractItemFrame<M extends Map> implements ItemFrame<M>{

    protected @NotNull Location location;
    protected @NotNull M map;

    protected AbstractItemFrame(@NotNull Location location, @NotNull M map) {
        this.location = location;
        this.map = map;
    }

    @Override
    public @NotNull Location getLocation() {
        return location;
    }

    @Override
    public void setLocation(@NotNull Location location) {
        this.location = location;
    }

    @Override
    public @NotNull M getMap() {
        return map;
    }

    @Override
    public void setMap(@NotNull M map) {
        this.map = map;
    }

}
