package me.squidxtv.frameui.core.actions.initiator;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public record PlayerInitiator(@NotNull Player player) implements ActionInitiator {
}
