package me.squidxtv.frameui;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import me.squidxtv.frameui.listener.ClickListener;
import me.squidxtv.frameui.listener.ScrollListener;
import me.squidxtv.frameui.packets.PacketManager;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class FrameUI extends JavaPlugin {

    private ProtocolManager protocolManager;
    private PacketManager packetManager;

    @Override
    public void onLoad() {
        protocolManager = ProtocolLibrary.getProtocolManager();
        packetManager = new PacketManager(this);
        saveDefaultConfig();
    }

    @Override
    public void onEnable() {
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new ClickListener(), this);
        pluginManager.registerEvents(new ScrollListener(), this);
    }

    public @NotNull ProtocolManager getProtocolManager() {
        return protocolManager;
    }

    public @NotNull PacketManager getPacketManager() {
        return packetManager;
    }

}
