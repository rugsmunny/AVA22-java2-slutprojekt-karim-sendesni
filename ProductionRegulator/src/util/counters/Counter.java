package util.counters;

import lombok.Getter;
import util.EventLogger;
import util.LogEvent;
import view.RegulatorGUI;

import static java.lang.Thread.sleep;

@Getter
public class Counter implements Runnable {

    private static Counter c;
    private static final EventLogger evtLog = EventLogger.getInstance();
    public static int average;
    private static boolean isRunning = true;

    private static final int interval = 10000;

    private Counter() {}

    public static void runCounter() {
        c = c != null ? c : new Counter();
        c.run();
    }

    public static int getCurrentAverage() {
        return (int) Math.round((ConsumptionCounter.consumed / ProductionCounter.produced) * 100);
    }

    @Override
    public void run() {

        while (isRunning) {
            try {
                sleep(interval);
                average = getCurrentAverage();
                evtLog.compileLogEntry(LogEvent.INFO, "Item");
                RegulatorGUI.printLogToTextArea();
            } catch (InterruptedException ignored) {
            }
        }
        Thread.currentThread().interrupt();
    }


    @Getter
    public static class ConsumptionCounter {

        protected static double consumed = 0;

        public static void add() { consumed++; }
    }
    @Getter
    public static class ProductionCounter {

        protected static double produced = 0;

        public static void add() { produced++; }
    }
}

