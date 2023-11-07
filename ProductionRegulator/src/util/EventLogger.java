package util;

import handler.ItemHandler;
import handler.ProducerHandler;
import lombok.Getter;
import util.counters.Counter;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Getter
public class EventLogger {

    private static EventLogger eventLogger;
    private static final StringBuilder STR_EVENT_BUILDER = new StringBuilder();
    private final String LOG_FILE_PATH = "src/data/" + getLogDateTime() + ".txt";
    private String logMessage;

    private EventLogger() {}

    public static EventLogger getInstance() {
        return eventLogger = eventLogger != null ? eventLogger : new EventLogger();
    }

    public void compileLogEntry(LogEvent evtType, String target) {
        STR_EVENT_BUILDER.append(
                String.format("%1$s | %2$s %3$7s %4$s %5$6s %6$1s %7$10s %8$1s%n",
                        getDateTime(),
                        "[", evtType, "]",
                        target.toUpperCase(),
                        "[", setMessage(evtType, target), "]"));

        finalizeLogMsg();

    }

    private String getDateTime() {
        return LocalDateTime.now()
                .format(DateTimeFormatter
                        .ofPattern("yyyy:MM:dd - HH:mm:ss"));
    }

    private String setMessage(LogEvent event, String target) {
        return switch (event) {
            case INFO -> switch (target) {
                case "Producer" -> "AMOUNT - " + ProducerHandler.getHandler().size();
                case "Item" -> "CONSUMPTION_RATE - " + Counter.average + "%";
                default -> "Message could not be found!";
            };
            case WARNING -> "AVAILABILITY - " + ItemHandler.getHandler().getAvailabilityLevel();
            case ADD, REMOVE -> "INTERVAL - " + ProducerHandler.getHandler().getProducerInterval();
        };
    }

    private void finalizeLogMsg() {
        logMessage = STR_EVENT_BUILDER.toString();
        STR_EVENT_BUILDER.setLength(0);
        writeToLogFile();
    }

    private String getLogDateTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy_MM_dd"));
    }

    private void writeToLogFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE_PATH, true))) {
            writer.write(getLogMessage());
        } catch (IOException ioE) {
            System.out.printf("\n[ERROR] | %s | [%s]\n", getDateTime(), ioE.getMessage());
        }
    }
}




