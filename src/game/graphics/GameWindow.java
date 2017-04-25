package game.graphics;

import game.Game;
import main.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created by protoCJ on 23.04.2017.
 */
public class GameWindow extends JFrame {
    Game game;


    public GameWindow(Game game) {
        this.game = game;
        initUI();
    }

    private void initUI() {

        add(new DisplayHandler(game));

        setSize(640, 480);

        setTitle("Astral Bastral");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public void start() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                GameWindow ex = new GameWindow(game);
                ex.setVisible(true);
            }
        });
    }
}
