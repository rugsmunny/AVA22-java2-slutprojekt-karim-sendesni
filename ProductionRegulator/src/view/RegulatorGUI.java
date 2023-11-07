package view;


import handler.*;

import model.Producer;
import util.EventLogger;
import util.LogEvent;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.beans.*;
import java.io.IOException;

public class RegulatorGUI extends JPanel implements ActionListener, PropertyChangeListener {
    private final JProgressBar progressBar;
    private final JButton removeProducerButton;
    private final JButton saveBtn;
    private final JButton loadBtn;
    public static JTextArea logOutput;
    private final JLabel valueLabel;
    private static ProgressListener progressBarHandler;
    private static final ProducerHandler pH = ProducerHandler.getHandler();
    private static final ConsumerHandler cH = ConsumerHandler.getHandler();
    private static final ItemHandler iH = ItemHandler.getHandler();
    private static final EventLogger evtLogger = EventLogger.getInstance();

    public RegulatorGUI() {

        super(new BorderLayout());

        removeProducerButton = new JButton("REMOVE");
        removeProducerButton.setActionCommand("REMOVE");
        removeProducerButton.addActionListener(this);

        JButton addProducerButton = new JButton("ADD");
        addProducerButton.setActionCommand("ADD");
        addProducerButton.addActionListener(this);

        saveBtn = new JButton("SAVE");
        saveBtn.setActionCommand("SAVE");
        saveBtn.addActionListener(this);

        loadBtn = new JButton("LOAD");
        loadBtn.setActionCommand("LOAD");
        loadBtn.addActionListener(this);


        progressBar = new JProgressBar(SwingConstants.VERTICAL);
        progressBar.setPreferredSize(new Dimension(40, 200));

        logOutput = new JTextArea(5, 85);
        logOutput.setMargin(new Insets(5, 5, 5, 5));
        logOutput.setEditable(false);
        logOutput.setBackground(Color.BLACK);
        logOutput.setForeground(Color.WHITE);

        JPanel progressBarPanel = new JPanel(new BorderLayout());
        valueLabel = new JLabel("0%", JLabel.CENTER);
        valueLabel.setSize(40, 20);
        progressBarPanel.add(progressBar, BorderLayout.EAST);
        progressBarPanel.add(valueLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addProducerButton);
        buttonPanel.add(removeProducerButton);
        buttonPanel.add(saveBtn);
        buttonPanel.add(loadBtn);


        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(progressBarPanel, BorderLayout.EAST);
        mainPanel.add(new JScrollPane(logOutput), BorderLayout.WEST);

        add(mainPanel);
        add(buttonPanel, BorderLayout.NORTH);

        progressBarHandler = new ProgressListener();
        progressBarHandler.addPropertyChangeListener(this);
        progressBarHandler.execute();

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        switch (e.getActionCommand()) {

            case "ADD" -> {
                if (pH.add(new Producer())) {
                    evtLogger.compileLogEntry(LogEvent.valueOf(e.getActionCommand()), "Producer");
                    printLogToTextArea();
                    evtLogger.compileLogEntry(LogEvent.INFO, "Producer");
                    printLogToTextArea();
                }
            }
            case "REMOVE" -> {
                if (pH.remove()) {
                    evtLogger.compileLogEntry(LogEvent.valueOf(e.getActionCommand()), "Producer");
                    printLogToTextArea();
                    evtLogger.compileLogEntry(LogEvent.INFO, "Producer");
                    printLogToTextArea();
                }
            }
            case "SAVE" -> {
                try {
                    pH.saveListToFile("_prod");
                    cH.saveListToFile("_cons");
                    iH.saveListToFile("_items");
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                 // avKommentera om du vill kolla i konsol att antal stämmer
                System.out.println("Sizes at SAVE | P -> " + pH.size() + " | C -> " + cH.size() + " | I -> " + iH.size());
            }


            case "LOAD" -> {
                try {
                    pH.loadListFromFile("_prod");
                    cH.loadListFromFile("_cons");
                    iH.loadListFromFile("_items");
                } catch (IOException | ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
                // avKommentera om du vill kolla i konsol att antal stämmer
                System.out.println("Sizes at LOAD | P -> " + pH.size() + " | C -> " + cH.size() + " | I -> " + iH.size());
            }

        }
    }


    public void propertyChange(PropertyChangeEvent evt) {
        if ("progress".equals(evt.getPropertyName())) {

            int progress = (int) evt.getNewValue();

            if (progress < 10 || progress > 89) {
                progressBar.setForeground(progress < 10 ? Color.RED : Color.GREEN);
                iH.setLevel(progress < 10 ? 0 : 1);
                evtLogger.compileLogEntry(LogEvent.WARNING, "Item");
                printLogToTextArea();
            } else {
                progressBar.setForeground(Color.YELLOW);
            }
            progressBar.setValue(progress);
            valueLabel.setText(progress + "%");
            revalidate();
            repaint();
        }


    }

    public static void printLogToTextArea() {
        try {
            logOutput
                    .getDocument()
                    .insertString(0, String.format(
                            "%s\n", evtLogger.getLogMessage()), null);

            logOutput
                    .setCaretPosition(0);
        } catch (BadLocationException ignored) {
        }

    }

    public static void createGUI() {

        JFrame frame = new JFrame("Production Regulator v2.1");
        frame.setPreferredSize(new Dimension(1000, 800));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JComponent newContentPane = new RegulatorGUI();
        newContentPane.setSize(800, 800);
        newContentPane.setOpaque(true);
        frame.setContentPane(newContentPane);

        frame.pack();
        frame.setVisible(true);

    }
}