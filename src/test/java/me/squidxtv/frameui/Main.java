package me.squidxtv.frameui;

import me.squidxtv.frameui.api.FrameAPI;
import me.squidxtv.frameui.api.FrameAPIImpl;
import me.squidxtv.frameui.api.cache.ImageCache;
import me.squidxtv.frameui.api.cache.ImageCacheImpl;
import me.squidxtv.frameui.api.parser.ScreenParser;
import me.squidxtv.frameui.api.parser.ScreenParserImpl;
import me.squidxtv.frameui.api.registry.ScreenRegistry;
import me.squidxtv.frameui.api.registry.ScreenRegistryImpl;
import me.squidxtv.frameui.core.content.Content;
import me.squidxtv.frameui.core.content.Parent;
import me.squidxtv.frameui.core.content.ScreenModel;
import me.squidxtv.frameui.screen.DebugScreen;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.ServicesManager;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class Main {

    private static final Logger LOGGER = Logger.getLogger("FrameUI-VisualTesting");

    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException, URISyntaxException {
        DebugScreen screen = getScreen();
        screen.open();
        screen.update();
        screen.getGraphics().update();

        JFrame window = new JFrame("FrameUI - Visual Testing");
        window.setLayout(new BorderLayout());

        JLabel render = new JLabel();
        render.setIcon(new ImageIcon(screen.getGraphics().getImage()));

        JPanel panel = new JPanel(new BorderLayout());
        JTree components = new JTree(toTree(screen.getModel()));
        JScrollPane scroll = new JScrollPane(components);
        scroll.setPreferredSize(new Dimension(500, screen.getGraphics().getPixelHeight()));
        panel.add(scroll, BorderLayout.NORTH);

        window.add(render, BorderLayout.WEST);
        window.add(panel, BorderLayout.EAST);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLocationRelativeTo(null);
        window.setResizable(false);
        window.pack();
        window.setVisible(true);


        Timer timer = new Timer(1000, e -> {
            screen.update();
            screen.getGraphics().update();
            render.repaint();
            components.repaint();
        });
        timer.setRepeats(true);
        timer.start();
    }

    private static DebugScreen getScreen() throws URISyntaxException, ParserConfigurationException, SAXException, IOException {
        FrameUI plugin = mock(FrameUI.class);
        when(plugin.getLogger()).thenReturn(LOGGER);

        FileConfiguration config = YamlConfiguration.loadConfiguration(Files.newBufferedReader(Path.of(Main.class.getResource("/config.yml").toURI())));

        ServicesManager manager = new ServicesManager() {

            private final Map<Class<?>, Object> registered = new HashMap<>();

            @Override
            public <T> void register(Class<T> aClass, T t, Plugin plugin, ServicePriority servicePriority) {
                registered.put(aClass, t);
            }

            @Override
            public void unregisterAll(Plugin plugin) {}

            @Override
            public void unregister(Class<?> aClass, Object o) {}

            @Override
            public void unregister(Object o) {}

            @Override
            @SuppressWarnings("unchecked")
            public <T> T load(Class<T> aClass) {
                return (T) registered.get(aClass);
            }

            @Override
            public <T> RegisteredServiceProvider<T> getRegistration(Class<T> aClass) {
                return null;
            }

            @Override
            public List<RegisteredServiceProvider<?>> getRegistrations(Plugin plugin) {
                return null;
            }

            @Override
            public <T> Collection<RegisteredServiceProvider<T>> getRegistrations(Class<T> aClass) {
                return null;
            }

            @Override
            public Collection<Class<?>> getKnownServices() {
                return null;
            }

            @Override
            public <T> boolean isProvidedFor(Class<T> aClass) {
                return false;
            }
        };
        manager.register(ScreenParser.class, new ScreenParserImpl(plugin), plugin, ServicePriority.Normal);
        manager.register(FrameAPI.class, new FrameAPIImpl(plugin), plugin, ServicePriority.Normal);
        manager.register(ImageCache.class, new ImageCacheImpl(config), plugin, ServicePriority.Normal);
        manager.register(ScreenRegistry.class, new ScreenRegistryImpl(), plugin, ServicePriority.Normal);

        try (MockedStatic<Bukkit> bukkit = Mockito.mockStatic(Bukkit.class)) {
            bukkit.when(Bukkit::getServicesManager).thenReturn(manager);
            ScreenModel model = ScreenModel.of(Path.of(Main.class.getResource("/test.xml").toURI()));
            return new DebugScreen(model);
        }
    }

    private static MutableTreeNode toTree(Content content) {
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(content);

        if (content instanceof Parent parent) {
            for (Content child : parent.getChildren()) {
                node.add(toTree(child));
            }
        }

        return node;
    }
}
