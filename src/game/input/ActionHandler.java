package game.input;

import game.Game;

import java.nio.ByteBuffer;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import static java.util.concurrent.TimeUnit.*;

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
        (new SchedulerThread(this, scheduler)).start();
    }

    @Override
    public void run() {
        System.out.println("Handling actions.");
        byte[] toSend = prepareData();
        game.sendData(toSend);
        System.out.println("Handled actions.");

    }

    byte[] prepareData() {
        return ByteBuffer.allocate(4).putFloat(7.0f/*game.getPlayerRotation()*/).array();
    }
}


class SchedulerThread extends Thread {

    Runnable toSchedule;
    ScheduledExecutorService scheduler;

    public void run() {
        final ScheduledFuture<?> scheduledFuture =
                scheduler.scheduleAtFixedRate(toSchedule, 0, 300, MILLISECONDS);
        scheduler.schedule(new Runnable() {
            public void run() { scheduledFuture.cancel(true); }
        }, 60 * 60, SECONDS);
    }

    public SchedulerThread(Runnable toSchedule, ScheduledExecutorService scheduler) {
        this.toSchedule = toSchedule;
        this.scheduler = scheduler;
    }
}