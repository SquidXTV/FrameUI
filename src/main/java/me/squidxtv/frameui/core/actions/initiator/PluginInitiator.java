package me.squidxtv.frameui.core.actions.initiator;

import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public record PluginInitiator(@NotNull JavaPlugin plugin) implements ActionInitiator<JavaPlugin> {

    @Override
    public @NotNull JavaPlugin getInitiator() {
        return plugin;
    }

}
