package me.squidxtv.visual;

import me.squidxtv.frameui.core.content.Content;
import me.squidxtv.frameui.core.content.Parent;
import me.squidxtv.util.ScreenUtil;
import me.squidxtv.visual.screen.DebugScreen;
import org.jetbrains.annotations.NotNull;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;

public class Main {

    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException, URISyntaxException {
        DebugScreen screen = ScreenUtil.getDebugScreen(Path.of(Main.class.getResource("test.xml").toURI()));
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
            components.setModel(new DefaultTreeModel(toTree(screen.getModel())));
            components.revalidate();
            components.repaint();
        });
        timer.setRepeats(true);
        timer.start();
    }

    private static @NotNull MutableTreeNode toTree(Content content) {
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(content);

        if (content instanceof Parent parent) {
            for (Content child : parent.getChildren()) {
                node.add(toTree(child));
            }
        }

        return node;
    }
}
