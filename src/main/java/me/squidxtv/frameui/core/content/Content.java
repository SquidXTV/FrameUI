package me.squidxtv.frameui.core.content;

import me.squidxtv.frameui.core.actions.click.Clickable;
import me.squidxtv.frameui.core.actions.scroll.Scrollable;
import me.squidxtv.frameui.core.graphics.Graphics;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public interface Content extends Clickable, Scrollable {

    void draw(@NotNull Graphics<?> graphics, int parentX, int parentY, int parentWidth, int parentHeight);

    @NotNull String getId();

    static @NotNull Content[] getChildren(@NotNull Element root) {
        NodeList children = root.getChildNodes();
        if (children.getLength() == 0) {
            return new Content[0];
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
                sub.add(instance);
            }
        }
        return sub.stream().filter(Objects::nonNull).toArray(Content[]::new);
    }

}
