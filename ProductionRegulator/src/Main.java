import handler.ConsumerHandler;
import model.Consumer;
import util.Interval;
import util.counters.Counter;
import view.RegulatorGUI;

public class Main {

    public static void main(String[] args) {

        javax
                .swing
                .SwingUtilities
                .invokeLater(RegulatorGUI::createGUI);

        for (int i = 0; i < Interval.randomAmount(3, 15); i++) {
            ConsumerHandler
                    .getHandler()
                    .add(new Consumer());
        }

        Counter.runCounter();
    }
}