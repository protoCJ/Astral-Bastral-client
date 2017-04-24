package game.graphics;

import game.graphics.drawElements.DrawElement;

import java.awt.*;
import java.util.*;

import javax.swing.JPanel;
/**
 * Created by protoCJ on 23.04.2017.
 */
public class DisplayHandler extends JPanel {

    Queue<DrawElement> drawQueue;

    public DisplayHandler() {
        drawQueue = new ArrayDeque<>();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        drawElements(g);
    }

    public void addElement(DrawElement drawElement) {

    }

    private void drawElements(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;

        RenderingHints rh
                = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        rh.put(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);

        g2d.setRenderingHints(rh);

        Dimension size = getSize();
        double w = size.getWidth();
        double h = size.getHeight();

        while (!drawQueue.isEmpty()) {
            drawQueue.remove().draw(g2d);
        }
    }
}
