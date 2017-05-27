package game.input;

import game.Game;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Created by vivace on 23.05.17.
 */
public class KeyInputHandler extends KeyAdapter {

    // Game instance, which is target for handled key actions.
    private Game game;


    public KeyInputHandler(Game game) {
        this.game = game;
    }

    /**
     * Notification from AWT that a key has been pressed. Note that
     * a key being pressed is equal to being pushed down but *NOT*
     * released. That's where keyTyped() comes in.
     *
     * @param event The details of the key that was pressed
     */
    public void keyPressed(KeyEvent event) {
        // Change game state when left, right or space key is pressed.
        switch (event.getKeyCode())  {
            case KeyEvent.VK_LEFT:
                game.startRotating(Game.RotationDirections.LEFT);
                break;
            case KeyEvent.VK_RIGHT:
                game.startRotating(Game.RotationDirections.RIGHT);
                break;
            case KeyEvent.VK_SPACE:
                game.startFiring();
                break;
        }
    }

    /**
     * Notification from AWT that a key has been released.
     *
     * @param event The details of the key that was released
     */
    public void keyReleased(KeyEvent event) {
        // Also change the game state when left, right or space key is
        // released.
        switch (event.getKeyCode())  {
            case KeyEvent.VK_LEFT:
                game.stopRotating(Game.RotationDirections.LEFT);
                break;
            case KeyEvent.VK_RIGHT:
                game.stopRotating(Game.RotationDirections.RIGHT);
                break;
            case KeyEvent.VK_SPACE:
                game.stopFiring();
                break;
        }
    }

    /**
     * Notification from AWT that a key has been typed. Note that
     * typing a key means to both press and then release it.
     *
     * @param event The details of the key that was typed.
     */
    // TODO not working properly. Was patched around with press event.
    public void keyTyped(KeyEvent event) {
        // Exit when "escape" was type and fire single missile with space type.
        switch (event.getKeyCode()) {
            case KeyEvent.VK_SPACE:
                game.fireOnce();
                break;
            case KeyEvent.VK_ESCAPE:
                System.exit(0);
                break;
        }
    }

}