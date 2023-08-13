package me.squidxtv.frameui.exceptions;

import me.squidxtv.frameui.core.Screen;
import org.jetbrains.annotations.NotNull;

public class ScreenRemovedException extends IllegalStateException {

    public ScreenRemovedException(@NotNull Screen<?> screen) {
        super("The screen " + screen + " has already been removed.");
    }

}