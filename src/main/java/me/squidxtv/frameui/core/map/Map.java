package me.squidxtv.frameui.core.map;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public interface Map {

    int WIDTH = 128;
    int HEIGHT = 128;

    void update(@NotNull Color pixel, int x, int y);

    @NotNull ItemStack getAsItemStack();

}