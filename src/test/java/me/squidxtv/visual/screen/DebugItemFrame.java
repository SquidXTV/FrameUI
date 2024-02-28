package me.squidxtv.visual.screen;

import me.squidxtv.frameui.core.itemframe.ItemFrame;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public class DebugItemFrame implements ItemFrame<DebugMap> {

    private DebugMap map = new DebugMap();

    @Override
    public @NotNull Location getLocation() {
        return null;
    }

    @Override
    public void setLocation(@NotNull Location location) {

    }

    @Override
    public @NotNull DebugMap getMap() {
        return map;
    }

    @Override
    public void setMap(@NotNull DebugMap map) {
        this.map = map;
    }

}
