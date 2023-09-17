package me.squidxtv.frameui.core.content;

import me.squidxtv.frameui.core.actions.click.Clickable;
import me.squidxtv.frameui.core.actions.scroll.Scrollable;
import me.squidxtv.frameui.core.graphics.Graphics;
import me.squidxtv.frameui.core.math.BoundingBox;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public interface Content extends Clickable, Scrollable {

    void draw(@NotNull Graphics<?> graphics, BoundingBox parent);

    @NotNull String getId();

    int getX();
    int getY();
    int getWidth();
    int getHeight();

    static List<Content> getChildren(@NotNull Element root) {
        NodeList children = root.getChildNodes();
        if (children.getLength() == 0) {
            return new ArrayList<>();
        }

        List<Content> sub = new ArrayList<>(children.getLength());
        for (int i = 0; i < children.getLength(); i++) {
            Node current = children.item(i);

            if (current instanceof Element element) {
                Content instance = switch (current.getNodeName()) {
                    case "div" -> new Div(element);
                    case "text" -> new Text(element);
                    case "image" -> {
                        try {
                            yield new Image(element);
                        } catch (IOException e) {
                            yield null;
                        }
                    }
                    default -> null;
                };
                if (instance == null) {
                    continue;
                }
                sub.add(instance);
            }
        }
        return sub;
    }

}
