package game.input;

import game.Game;

import java.nio.ByteBuffer;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Created by protoCJ on 23.04.2017.
 */
public class ActionHandler implements Runnable {

    Game game;
    Queue<UserAction> userActionQueue;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public ActionHandler(Game game) {
        this.game = game;
        userActionQueue = new ArrayDeque<>();
    }

    public void addToInputQueue(UserAction userInput) {
        userActionQueue.add(userInput);
    }

    public void start() {
        new Thread(new SchedulerThread(this, scheduler)).start();
    }

    @Override
    public void run() {
        System.out.println("Handling actions.");
        game.sendData(game.getUpdateData());
        System.out.println("Handled actions.");

    }
}