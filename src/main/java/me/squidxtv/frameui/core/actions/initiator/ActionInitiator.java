package me.squidxtv.frameui.core.actions.initiator;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public sealed interface ActionInitiator<T> permits CodeInitiator, PlayerInitiator, PluginInitiator {

    // ToDo: remove method, makes no sense as it is going to be capture of ?, instanceof check needed anyways
    @NotNull T getInitiator();

    static @NotNull PlayerInitiator ofPlayer(Player player) {
        return new PlayerInitiator(player);
    }

    static @NotNull PluginInitiator ofPlugin(JavaPlugin plugin) {
        return new PluginInitiator(plugin);
    }

    static @NotNull CodeInitiator ofClass(Class<?> clazz) {
        return new CodeInitiator(clazz);
    }

}

