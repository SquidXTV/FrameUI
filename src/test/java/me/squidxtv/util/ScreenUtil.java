package me.squidxtv.util;

import me.squidxtv.frameui.FrameUI;
import me.squidxtv.frameui.api.FrameAPI;
import me.squidxtv.frameui.api.FrameAPIImpl;
import me.squidxtv.frameui.api.cache.ImageCache;
import me.squidxtv.frameui.api.cache.ImageCacheImpl;
import me.squidxtv.frameui.api.parser.ScreenParser;
import me.squidxtv.frameui.api.parser.ScreenParserImpl;
import me.squidxtv.frameui.api.registry.ScreenRegistry;
import me.squidxtv.frameui.api.registry.ScreenRegistryImpl;
import me.squidxtv.frameui.core.content.ScreenModel;
import me.squidxtv.visual.Main;
import me.squidxtv.visual.screen.DebugScreen;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.ServicesManager;
import org.jetbrains.annotations.NotNull;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
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

public final class ScreenUtil {

    private static final Logger LOGGER = Logger.getLogger("FrameUI-VisualTesting");

    private ScreenUtil() {
        throw new UnsupportedOperationException("Helper class, instantiation unsupported.");
    }

    public static ScreenModel getByPath(@NotNull Path path) throws ParserConfigurationException, SAXException, URISyntaxException, IOException {
        FrameUI plugin = mock(FrameUI.class);
        when(plugin.getLogger()).thenReturn(LOGGER);

        ServicesManager manager = getCustomServicesManager(plugin);
        try (MockedStatic<Bukkit> bukkit = Mockito.mockStatic(Bukkit.class)) {
            bukkit.when(Bukkit::getServicesManager).thenReturn(manager);
            return ScreenModel.of(path);
        }
    }

    public static DebugScreen getDebugScreen(@NotNull Path path) throws ParserConfigurationException, SAXException, URISyntaxException, IOException {
        FrameUI plugin = mock(FrameUI.class);
        when(plugin.getLogger()).thenReturn(LOGGER);

        ServicesManager manager = getCustomServicesManager(plugin);
        try (MockedStatic<Bukkit> bukkit = Mockito.mockStatic(Bukkit.class)) {
            bukkit.when(Bukkit::getServicesManager).thenReturn(manager);
            return new DebugScreen(ScreenModel.of(path));
        }
    }

    private static @NotNull ServicesManager getCustomServicesManager(FrameUI plugin) throws URISyntaxException, ParserConfigurationException, SAXException, IOException {
        ServicesManager manager = new ServicesManager() {

            private final Map<Class<?>, Object> registered = new HashMap<>();

            @Override
            public <T> void register(Class<T> aClass, T t, Plugin plugin, ServicePriority servicePriority) {
                registered.put(aClass, t);
            }

            @Override
            public void unregisterAll(Plugin plugin) {
            }

            @Override
            public void unregister(Class<?> aClass, Object o) {
            }

            @Override
            public void unregister(Object o) {
            }

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

        FileConfiguration config = YamlConfiguration.loadConfiguration(Files.newBufferedReader(Path.of(Main.class.getResource("/me/squidxtv/plugin-config.yml").toURI())));

        manager.register(ScreenParser.class, new ScreenParserImpl(plugin), plugin, ServicePriority.Normal);
        manager.register(FrameAPI.class, new FrameAPIImpl(plugin), plugin, ServicePriority.Normal);
        manager.register(ImageCache.class, new ImageCacheImpl(config), plugin, ServicePriority.Normal);
        manager.register(ScreenRegistry.class, new ScreenRegistryImpl(), plugin, ServicePriority.Normal);

        return manager;
    }

}
