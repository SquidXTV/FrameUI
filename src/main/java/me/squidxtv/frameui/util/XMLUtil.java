package me.squidxtv.frameui.util;

import me.squidxtv.frameui.core.content.Div;
import me.squidxtv.frameui.core.content.ElementNode;
import me.squidxtv.frameui.core.content.Img;
import me.squidxtv.frameui.core.content.Text;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.Arrays;
import java.util.Objects;

/**
 * Utility class for parsing XML files.
 */
public final class XMLUtil {

    private XMLUtil() {}

    /**
     * Gets all child nodes of given Node.
     * @param node Node
     * @return array of child nodes
     */
    public static ElementNode[] getChildNodes(Node node) {
        NodeList nodes = node.getChildNodes();
        if (nodes.getLength() == 0) {
            return new ElementNode[0];
        }

        ElementNode[] sub = new ElementNode[nodes.getLength()];
        for (int i = 0; i < nodes.getLength(); i++) {
            Node current = nodes.item(i);

            if (current.getNodeName().equals("#text")) {
                continue;
            }

            switch (current.getNodeName()) {
                case "div" -> sub[i] = Div.of((Element) current);
                case "text" -> sub[i] = Text.of((Element) current);
                case "img" -> sub[i] = Img.of((Element) current);
            }
        }
        return Arrays.stream(sub).filter(Objects::nonNull).toArray(ElementNode[]::new);
    }
}
