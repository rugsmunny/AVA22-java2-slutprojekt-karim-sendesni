package handler;

import util.Interval;
import lombok.Data;
import java.io.Serializable;
@Data
public abstract class ThreadHandler implements Runnable, Serializable {

    private final int interval;
    protected boolean isRunning;

    protected ThreadHandler() {
        interval = Interval.randomInterval(1, 10);
        isRunning = true;
        new Thread(this).start();
    }

    public void runLoadedEntity() {
        new Thread(this).start();
    }

    @Override
    public void run() {

        while (isRunning) {
            try {
                Thread.sleep(interval);
                execute();
            } catch (InterruptedException ignored) {}
        }
        Thread.currentThread().interrupt();
    }
    protected abstract void execute();
}
