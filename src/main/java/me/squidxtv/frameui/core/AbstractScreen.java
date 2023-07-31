package me.squidxtv.frameui.core;

import me.squidxtv.frameui.exceptions.ScreenRemovedException;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractScreen implements Screen {

    // TODO: 31/07/2023 implement screen manager and add remove/close method
//    protected static final ScreenManager SCREEN_MANAGER = Bukkit.getServicesManager().load(ScreenManager.class);

    private @Nullable Permission clickPermission = null;
    private @Nullable Permission scrollPermission = null;
    private @NotNull State state = State.CLOSED;

    @Override
    public boolean click(@NotNull Player player, int x, int y) {
        throwIfRemoved();
        if (state == State.CLOSED) {
            return false;
        }

        if (clickPermission != null && !player.hasPermission(clickPermission)) {
            return false;
        }

        // TODO: 27/07/2023 Perform click on content
        return true;
    }

    @Override
    public boolean scroll(@NotNull Player player, @NotNull ScrollDirection direction, int x, int y) {
        throwIfRemoved();
        if (state == State.CLOSED) {
            return false;
        }

        if (scrollPermission != null && !player.hasPermission(scrollPermission)) {
            return false;
        }

        // TODO: 27/07/2023 Perform scroll on content
        return true;
    }

    protected void throwIfRemoved() {
        if (state == State.REMOVED) {
            throw new ScreenRemovedException(this);
        }
    }

    public @Nullable Permission getClickPermission() {
        return clickPermission;
    }

    public @Nullable Permission getScrollPermission() {
        return scrollPermission;
    }

    public void setClickPermission(@Nullable Permission clickPermission) {
        this.clickPermission = clickPermission;
    }

    public void setScrollPermission(@Nullable Permission scrollPermission) {
        this.scrollPermission = scrollPermission;
    }

    public @NotNull State getState() {
        return state;
    }

    protected void setState(@NotNull State state) {
        this.state = state;
    }

    public enum State {
        OPEN,
        CLOSED,
        REMOVED
    }
}
