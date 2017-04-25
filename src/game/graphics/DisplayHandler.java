package game.graphics;

import game.Game;
import game.graphics.drawElements.DrawElement;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.*;

import javax.swing.JPanel;
import javax.swing.Timer;
/**
 * Created by protoCJ on 23.04.2017.
 */
public class DisplayHandler extends JPanel implements ActionListener {

    Queue<DrawElement> drawQueue;
    Game game;
    private Timer timer;
    private int delay = 10;

    public DisplayHandler(Game game) {
        this.game = game;
        drawQueue = new ArrayDeque<>();

        init();
    }

    public void init() {
        addKeyListener(new TAdapter());
        setFocusable(true);
        setBackground(Color.BLACK);


        timer = new Timer(delay, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        drawElements(g);
    }

    public void addElement(DrawElement drawElement) {
        drawQueue.add(drawElement);
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

    @Override
    public void actionPerformed(ActionEvent e) {
        addElement(game.getPlayer());
        repaint();
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyReleased(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {
            System.out.println("Keyboard event");
            if (e.getKeyChar() == 'a') {
                game.rotateLeft();
            }
            if (e.getKeyChar() == 'd') {
                game.rotateRight();
            }
        }
    }
}
