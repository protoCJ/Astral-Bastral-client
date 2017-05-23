package game.input;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Created by vivace on 23.05.17.
 */



class SchedulerThread implements Runnable {

    Runnable toSchedule;
    ScheduledExecutorService scheduler;

    public SchedulerThread(Runnable toSchedule, ScheduledExecutorService scheduler) {
        this.toSchedule = toSchedule;
        this.scheduler = scheduler;
    }

    public void run() {
        final ScheduledFuture<?> scheduledFuture =
                scheduler.scheduleAtFixedRate(toSchedule, 0, 10, MILLISECONDS);
        scheduler.schedule(new Runnable() {
            public void run() { scheduledFuture.cancel(true); }
        }, 60 * 60, SECONDS);
    }
}