package me.squidxtv.frameui;

import org.bukkit.plugin.PluginLoadOrder;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.annotation.plugin.*;
import org.bukkit.plugin.java.annotation.plugin.author.Author;

/**
 * Base class for the FrameUI plugin, which provides a user interface framework for Minecraft plugins.
 * This plugin allows developers to create custom graphical user interfaces (GUIs) within Minecraft using a
 * simple XML-based layout system. The FrameUI plugin handles input events, rendering, and other GUI-related
 * functionality, so developers can focus on designing their GUIs and implementing their logic.
 * @author SquidXTV
 * @see <a href="https://github.com/SquidXTV/FrameUI">GitHub</a>
 * @see <a href="https://github.com/SquidXTV/AuctionHouseUI">AuctionHouseUI</a>
 * @version 1.0h
 */
@Plugin(name = "FrameUI", version = "1.0")
@Description("FrameUI is a user interface (UI) library that allows developers to create graphical user interfaces for their plugins using XML markup.")
@ApiVersion(ApiVersion.Target.v1_20)
@LoadOrder(PluginLoadOrder.STARTUP)
@LogPrefix("FrameUI")
@Website("www.squidxtv.me")
//@Dependency("ProtocolLib")
//@Library("com.comphenix.protocol:ProtocolLib:5.0.0")
@Author("SquidXTV")
public class FrameUI extends JavaPlugin {

    @Override
    public void onEnable() {

    }
}
