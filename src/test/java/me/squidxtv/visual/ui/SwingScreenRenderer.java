package me.squidxtv.visual.ui;

import me.squidxtv.frameui.core.actions.initiator.ActionInitiator;
import me.squidxtv.frameui.core.actions.scroll.ScrollDirection;
import me.squidxtv.visual.screen.DebugScreen;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class SwingScreenRenderer extends JLabel {

    public SwingScreenRenderer(DebugScreen screen) {
        setIcon(new ImageIcon(screen.getGraphics().getImage()));
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() != MouseEvent.BUTTON1) {
                    return;
                }
                screen.click(ActionInitiator.ofClass(SwingScreenRenderer.class), e.getX(), e.getY());
            }

            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}

        });

        addMouseWheelListener(e -> {
            ScrollDirection direction;
            if (e.getWheelRotation() > 0) {
                direction = ScrollDirection.UP;
            } else if (e.getWheelRotation() < 0) {
                direction = ScrollDirection.DOWN;
            } else {
                throw new IllegalStateException("Illegal wheel rotation direction.");
            }
            screen.scroll(ActionInitiator.ofClass(SwingScreenRenderer.class), direction, e.getX(), e.getY());
        });
    }


}
