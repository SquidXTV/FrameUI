package me.squidxtv.frameui.core.itemframe;

import me.squidxtv.frameui.core.map.Map;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public interface ItemFrame<M extends Map> {

    @NotNull Location getLocation();
    void setLocation(@NotNull Location location);

    @NotNull M getMap();
    void setMap(@NotNull M map);

}
