package me.squidxtv.frameui;

import me.squidxtv.frameui.api.FrameAPI;
import me.squidxtv.frameui.api.FrameAPIImpl;
import me.squidxtv.frameui.api.cache.ImageCache;
import me.squidxtv.frameui.api.cache.ImageCacheImpl;
import me.squidxtv.frameui.api.parser.ScreenParser;
import me.squidxtv.frameui.api.parser.ScreenParserImpl;
import me.squidxtv.frameui.api.registry.ScreenRegistry;
import me.squidxtv.frameui.api.registry.ScreenRegistryImpl;
import org.bukkit.plugin.PluginLoadOrder;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.annotation.dependency.Dependency;
import org.bukkit.plugin.java.annotation.dependency.Library;
import org.bukkit.plugin.java.annotation.plugin.*;
import org.bukkit.plugin.java.annotation.plugin.author.Author;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.util.logging.Level;

/**
 * Base class for the FrameUI plugin, which provides a user interface framework for Minecraft plugins.
 * This plugin allows developers to create custom graphical user interfaces (GUIs) within Minecraft using a
 * simple XML-based layout system. The FrameUI plugin handles input events, rendering, and other GUI-related
 * functionality, so developers can focus on designing their GUIs and implementing their logic.
 *
 * @author SquidXTV
 * @version 1.0
 * @see <a href="https://github.com/SquidXTV/FrameUI">GitHub</a>
 * @see <a href="https://github.com/SquidXTV/AuctionHouseUI">AuctionHouseUI</a>
 */
@Plugin(name = "FrameUI", version = "1.0")
@Description("FrameUI is a user interface (UI) library that allows developers to create graphical user interfaces for their plugins using XML markup.")
@ApiVersion(ApiVersion.Target.v1_20)
@LoadOrder(PluginLoadOrder.STARTUP)
@LogPrefix("FrameUI")
@Website("www.squidxtv.me")
@Dependency("ProtocolLib")
@Library("com.comphenix.protocol:ProtocolLib:5.0.0")
@Author("SquidXTV")
public class FrameUI extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();

        ServicesManager servicesManager = getServer().getServicesManager();
        servicesManager.register(FrameAPI.class, new FrameAPIImpl(this), this, ServicePriority.Normal);
        servicesManager.register(ImageCache.class, new ImageCacheImpl(this.getConfig()), this, ServicePriority.Normal);
        servicesManager.register(ScreenRegistry.class, new ScreenRegistryImpl(), this, ServicePriority.Normal);
        try {
            servicesManager.register(ScreenParser.class, new ScreenParserImpl(this), this, ServicePriority.Normal);
        } catch (SAXException | ParserConfigurationException e) {
            getLogger().log(Level.SEVERE, "Exception thrown when creating ScreenParser. Disabling FrameUI plugin.", e);
            getServer().getPluginManager().disablePlugin(this);
        }
    }
}
