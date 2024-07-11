package me.squidxtv.frameui;

import me.squidxtv.frameui.api.FramesApi;
import me.squidxtv.frameui.api.ScreenRegistry;
import me.squidxtv.frameui.api.ScreenRegistryImpl;
import me.squidxtv.frameui.listener.ClickListener;
import me.squidxtv.frameui.listener.LeaveListener;
import me.squidxtv.frameui.listener.ScrollListener;
import org.bukkit.Server;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;

public class FrameUI extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        Server server = getServer();

        ScreenRegistry registry = new ScreenRegistryImpl(this);
        ServicesManager services = server.getServicesManager();
        services.register(ScreenRegistry.class, registry, this, ServicePriority.Normal);

        FileConfiguration config = getConfig();
        PluginManager pluginManager = server.getPluginManager();

        pluginManager.registerEvents(new LeaveListener(registry), this);

        if (config.getBoolean("features.click", true)) {
            pluginManager.registerEvents(new ClickListener(registry), this);
        }

        if (config.getBoolean("features.scroll", true)) {
            pluginManager.registerEvents(new ScrollListener(registry), this);
        }
    }


}
