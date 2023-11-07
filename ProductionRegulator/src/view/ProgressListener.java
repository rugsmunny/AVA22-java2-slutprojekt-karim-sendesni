package view;

import util.counters.Counter;

import javax.swing.SwingWorker;

public class ProgressListener extends SwingWorker<Void, Integer> {

    private final boolean isRunning = true;

    ProgressListener() {}

    @Override
    protected Void doInBackground() {

        while (isRunning) {

            try {

                Thread.sleep(1000);
                setProgress(Counter.getCurrentAverage());
                publish(Counter.getCurrentAverage());

            } catch (InterruptedException ignore) {}
        }
    }
}
