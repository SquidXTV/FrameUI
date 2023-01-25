package me.squidxtv.frameui;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.utility.ByteBuddyFactory;
import com.comphenix.protocol.utility.HexDumper;
import com.comphenix.protocol.utility.MinecraftReflection;
import com.comphenix.protocol.utility.Util;
import me.squidxtv.frameui.core.ScreenModel;
import me.squidxtv.frameui.listener.ClickListener;
import me.squidxtv.frameui.listener.ScrollListener;
import me.squidxtv.frameui.packets.PacketManager;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

public final class FrameUI extends JavaPlugin {

    private DocumentBuilder documentBuilder;
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

        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = schemaFactory.newSchema(FrameUI.class.getResource("/frameui.xsd"));

            documentBuilderFactory.setSchema(schema);
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException | SAXException e) {
            getLogger().severe("Unable to create DocumentBuilder: \n" + e.getMessage() + "\n Disabling plugin.");
            pluginManager.disablePlugin(this);
        }

        pluginManager.registerEvents(new ClickListener(), this);
        pluginManager.registerEvents(new ScrollListener(), this);
    }

    public ProtocolManager getProtocolManager() {
        return protocolManager;
    }

    public PacketManager getPacketManager() {
        return packetManager;
    }

    public DocumentBuilder getDocumentBuilder() {
        return this.documentBuilder;
    }

}
