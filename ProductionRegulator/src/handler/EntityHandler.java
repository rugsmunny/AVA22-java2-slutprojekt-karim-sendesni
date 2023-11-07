package handler;


import model.Item;
import model.Producer;
import util.EventLogger;
import util.LogEvent;
import view.RegulatorGUI;

import javax.swing.text.BadLocationException;
import java.io.*;
import java.util.*;


public abstract class EntityHandler<T> implements Serializable {

    private final EventLogger evtLogger = EventLogger.getInstance();
    protected final Queue<T> list = new LinkedList<>();
    protected String filePath = "src/data/";

    EntityHandler() {
    }

    protected boolean add(T t) {
        boolean result;
        synchronized (list) {
            result = list.add(t);
            if (result && t instanceof Producer) {
                ProducerHandler.getHandler().setProducerInterval(((ThreadHandler) t).getInterval());
                evtLogger.compileLogEntry(LogEvent.ADD, t.getClass().getSimpleName());
            }
            list.notify();
        }
        return result;
    }

    protected boolean remove() {
        boolean result = false;
        synchronized (list) {
            T t = list.poll();

            if (t != null) {
                result = true;
                if (t instanceof Producer) {
                    ProducerHandler.getHandler().setProducerInterval(((ThreadHandler) t).getInterval());
                    evtLogger.compileLogEntry(LogEvent.REMOVE, t.getClass().getSimpleName());
                    ((ThreadHandler) t).setRunning(false);
                }
            }
        }
        return result;
    }

    public int size() {
        synchronized (list) {
            return list.size();
        }
    }

    public void saveListToFile(String str) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(filePath + str + "State.ser", false))) {
            synchronized (list) {
                outputStream.writeObject(list);
            }
        } catch (IOException e) {
            printToLogOutPut(e.getMessage());
        }
    }


    public void loadListFromFile(String str) {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(filePath + str + "State.ser"))) {
            List<T> loadedList = (List<T>) inputStream.readObject();
            if (loadedList != null && !loadedList.isEmpty()) {
                synchronized (list) {
                    list.forEach(entity -> {
                        if (!(entity instanceof Item)) ((ThreadHandler) entity).setRunning(false);
                    });
                    list.clear();
                    list.addAll(loadedList);
                    list.forEach(loadedEntity -> {
                        if (!(loadedEntity instanceof Item)) ((ThreadHandler) loadedEntity).runLoadedEntity();
                    });
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            printToLogOutPut(e.getMessage());
        }
    }

    private void printToLogOutPut(String str) {
        try {
            RegulatorGUI.logOutput
                    .getDocument()
                    .insertString(0, String.format(
                            "%s\n", str), null);

            RegulatorGUI.logOutput
                    .setCaretPosition(0);
        } catch (BadLocationException ignored) {
        }
    }
}
