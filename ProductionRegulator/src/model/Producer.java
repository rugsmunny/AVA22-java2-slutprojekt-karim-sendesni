package model;

import handler.ItemHandler;
import handler.ThreadHandler;
import util.counters.Counter;

import java.io.Serializable;


public class Producer extends ThreadHandler implements Serializable {

    @Override
    public void execute() {
        if (ItemHandler.getHandler().add(new Item())) {
            Counter.ProductionCounter.add();

        }
    }
}










