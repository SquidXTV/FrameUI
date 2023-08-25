package me.squidxtv.frameui.core;

import me.squidxtv.frameui.FrameUI;
import me.squidxtv.frameui.api.FrameAPI;
import me.squidxtv.frameui.api.FrameAPIImpl;
import me.squidxtv.frameui.api.cache.ImageCache;
import me.squidxtv.frameui.api.cache.ImageCacheImpl;
import me.squidxtv.frameui.api.parser.ScreenParser;
import me.squidxtv.frameui.api.parser.ScreenParserImpl;
import me.squidxtv.frameui.api.registry.ScreenRegistry;
import me.squidxtv.frameui.api.registry.ScreenRegistryImpl;
import me.squidxtv.frameui.core.content.*;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.ServicesManager;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ParentTest {

    @Test
    void childrenByType() {
        ScreenModel model = ofFile("ChildrenByType.xml");
        assertNotNull(model);
        assertFalse(model.getChildrenByType(Div.class).isEmpty());
        List<Image> foundImages = model.getChildrenByType(Image.class);
        assertFalse(foundImages.isEmpty());
        assertEquals(1, foundImages.size());
        assertTrue(model.getChildrenByType(Text.class).isEmpty());
    }

    @Test
    void descendantsByType() {
        ScreenModel model = ofFile("DescendantsByType.xml");
        assertNotNull(model);
        assertTrue(model.getDescendantsByType(Text.class).isEmpty());
        assertEquals(3, model.getDescendantsByType(Div.class).size());
        assertFalse(model.getDescendantsByType(Image.class).isEmpty());
    }

    @Test
    void childrenById() {
        ScreenModel model = ofFile("ChildrenById.xml");
        assertNotNull(model);
        List<Content> children = model.getChildrenById("to-search-for");
        assertEquals(1, children.size());
        assertInstanceOf(Div.class, children.get(0));
        assertTrue(model.getChildrenById("not-to-search-for").isEmpty());
    }

    @Test
    void descendantsById() {
        ScreenModel model = ofFile("DescendantsById.xml");
        assertNotNull(model);
        List<Content> descendantsById = model.getDescendantsById("to-search-for");
        assertEquals(4, descendantsById.size());
        assertTrue(model.getDescendantsById("not-to-search-for").isEmpty());
    }

    @Test
    void firstChildByType() {
        ScreenModel model = ofFile("FirstChildByType.xml");
        assertNotNull(model);
        Optional<Div> firstDivByType = model.getFirstChildByType(Div.class);
        assertTrue(firstDivByType.isPresent());
        assertEquals("first-div", firstDivByType.get().getId());
        Optional<Text> firstTextByType = model.getFirstChildByType(Text.class);
        assertTrue(firstTextByType.isPresent());
        assertEquals("first-text", firstTextByType.get().getId());
        assertTrue(model.getFirstChildByType(Image.class).isEmpty());
    }

    @Test
    void firstDescendantByType() {
        ScreenModel model = ofFile("FirstDescendantByType.xml");
        assertNotNull(model);
        Optional<Text> firstTextByType = model.getFirstDescendantByType(Text.class);
        assertTrue(firstTextByType.isPresent());
        assertEquals("first-text", firstTextByType.get().getId());
        assertTrue(model.getFirstDescendantByType(Image.class).isPresent());
    }

    @Test
    void firstChildById() {
        ScreenModel model = ofFile("FirstChildById.xml");
        assertNotNull(model);
        assertTrue(model.getFirstChildById("first-text").isPresent());
        assertFalse(model.getFirstChildById("second-text").isPresent());
    }

    @Test
    void firstDescendantById() {
        ScreenModel model = ofFile("FirstDescendantById.xml");
        assertNotNull(model);
        Optional<Content> firstText = model.getFirstDescendantById("text");
        assertTrue(firstText.isPresent());
        Content text = firstText.get();
        assertInstanceOf(Text.class, text);
        assertEquals("FirstText", ((Text) text).getContent());
    }

    private static ScreenModel ofFile(String filename) {
        try {
            Path path = Path.of(ParentTest.class.getResource("/me/squidxtv/frameui/core/parent/" + filename).toURI());
            try (MockedStatic<Bukkit> bukkit = Mockito.mockStatic(Bukkit.class)) {
                ServicesManager manager = getManager();
                bukkit.when(Bukkit::getServicesManager).thenReturn(manager);
                return ScreenModel.of(path);
            }
        } catch (Exception e) {
            fail(e);
        }

        fail();
        return null;
    }

    private static ServicesManager getManager() throws ParserConfigurationException, SAXException, URISyntaxException, IOException {
        FrameUI plugin = mock(FrameUI.class);
        when(plugin.getLogger()).thenReturn(Logger.getLogger(ParentTest.class.getName()));
        FileConfiguration config = YamlConfiguration.loadConfiguration(Files.newBufferedReader(Path.of(ParentTest.class.getResource("/me/squidxtv/plugin-config.yml").toURI())));

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
        return manager;
    }

}
