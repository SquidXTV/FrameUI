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
            public <T> void register(@NotNull Class<T> aClass, @NotNull T t, @NotNull Plugin plugin, @NotNull ServicePriority servicePriority) {
                registered.put(aClass, t);
            }

            @Override
            public void unregisterAll(@NotNull Plugin plugin) {
                throw new UnsupportedOperationException("Functionality not used in debug environment.");
            }

            @Override
            public void unregister(@NotNull Class<?> aClass, @NotNull Object o) {
                throw new UnsupportedOperationException("Functionality not used in debug environment.");
            }

            @Override
            public void unregister(@NotNull Object o) {
                throw new UnsupportedOperationException("Functionality not used in debug environment.");
            }

            @Override
            @SuppressWarnings("unchecked")
            public <T> T load(@NotNull Class<T> aClass) {
                return (T) registered.get(aClass);
            }

            @Override
            public <T> @NotNull RegisteredServiceProvider<T> getRegistration(@NotNull Class<T> aClass) {
                throw new UnsupportedOperationException("Functionality not used in debug environment.");
            }

            @Override
            public @NotNull List<RegisteredServiceProvider<?>> getRegistrations(@NotNull Plugin plugin) {
                throw new UnsupportedOperationException("Functionality not used in debug environment.");
            }

            @Override
            public <T> @NotNull Collection<RegisteredServiceProvider<T>> getRegistrations(@NotNull Class<T> aClass) {
                throw new UnsupportedOperationException("Functionality not used in debug environment.");
            }

            @Override
            public @NotNull Collection<Class<?>> getKnownServices() {
                throw new UnsupportedOperationException("Functionality not used in debug environment.");
            }

            @Override
            public <T> boolean isProvidedFor(@NotNull Class<T> aClass) {
                throw new UnsupportedOperationException("Functionality not used in debug environment.");
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
