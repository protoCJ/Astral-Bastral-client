package game.input;

import game.Game;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Created by vivace on 23.05.17.
 */
public class KeyInputHandler extends KeyAdapter {
    /** The number of key presses we've had while waiting for an "any key" press */
    private Game game;

    public KeyInputHandler(Game game) {
        this.game = game;
    }

    /**
     * Notification from AWT that a key has been pressed. Note that
     * a key being pressed is equal to being pushed down but *NOT*
     * released. Thats where keyTyped() comes in.
     *
     * @param e The details of the key that was pressed
     */
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode())  {
            case KeyEvent.VK_LEFT:
                game.rotatePlayer(Game.RotationDirections.LEFT);
                break;
            case KeyEvent.VK_RIGHT:
                game.rotatePlayer(Game.RotationDirections.RIGHT);
                break;
            case KeyEvent.VK_SPACE:
                //TODO
                break;
        }
    }

    /**
     * Notification from AWT that a key has been released.
     *
     * @param e The details of the key that was released
     */
    public void keyReleased(KeyEvent e) {
    }

    /**
     * Notification from AWT that a key has been typed. Note that
     * typing a key means to both press and then release it.
     *
     * @param e The details of the key that was typed.
     */
    public void keyTyped(KeyEvent e) {
        if (e.getKeyChar() == 27) {
            System.exit(0);
        }
    }
}