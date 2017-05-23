package game.input;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Created by vivace on 23.05.17.
 */
public class KeyInputHandler extends KeyAdapter {
    /** The number of key presses we've had while waiting for an "any key" press */
    private int pressCount = 1;

    private boolean waitingForKeyPress = true;
    /** True if the left cursor key is currently pressed */
    private boolean leftPressed = false;
    /** True if the right cursor key is currently pressed */
    private boolean rightPressed = false;
    /** True if we are firing */
    private boolean firePressed = false;

    /**
     * Notification from AWT that a key has been pressed. Note that
     * a key being pressed is equal to being pushed down but *NOT*
     * released. Thats where keyTyped() comes in.
     *
     * @param e The details of the key that was pressed
     */
    public void keyPressed(KeyEvent e) {
        // if we're waiting for an "any key" typed then we don't
        // want to do anything with just a "press"
        if (waitingForKeyPress) {
            return;
        }


        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            leftPressed = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            rightPressed = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            firePressed = true;
        }
    }

    /**
     * Notification from AWT that a key has been released.
     *
     * @param e The details of the key that was released
     */
    public void keyReleased(KeyEvent e) {
        // if we're waiting for an "any key" typed then we don't
        // want to do anything with just a "released"
        if (waitingForKeyPress) {
            return;
        }

        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            leftPressed = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            rightPressed = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            firePressed = false;
        }
    }

    /**
     * Notification from AWT that a key has been typed. Note that
     * typing a key means to both press and then release it.
     *
     * @param e The details of the key that was typed.
     */
    public void keyTyped(KeyEvent e) {
        // if we're waiting for a "any key" type then
        // check if we've recieved any recently. We may
        // have had a keyType() event from the user releasing
        // the shoot or move keys, hence the use of the "pressCount"
        // counter.

        if (waitingForKeyPress) {
            if (pressCount == 1) {
                // since we've now recieved our key typed

                // event we can mark it as such and start

                // our new game

                waitingForKeyPress = false;
                pressCount = 0;
            } else {
                pressCount++;
            }
        }

        // if we hit escape, then quit the game

        if (e.getKeyChar() == 27) {
            System.exit(0);
        }
    }
}