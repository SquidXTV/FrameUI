package me.squidxtv.frameui.core;

import me.squidxtv.frameui.core.content.*;
import me.squidxtv.util.ScreenUtil;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

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
        assertInstanceOf(Div.class, children.getFirst());
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
            return ScreenUtil.getByPath(path);
        } catch (Exception e) {
            fail(e);
        }

        fail();
        return null;
    }

}
