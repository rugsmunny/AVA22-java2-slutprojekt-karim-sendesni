package model;

import handler.ItemHandler;
import handler.ThreadHandler;
import util.counters.Counter;

import java.io.Serializable;

public class Consumer extends ThreadHandler implements Serializable  {

	@Override
	public void execute() {
		if(ItemHandler.getHandler().remove()) {
			Counter.ConsumptionCounter.add();

		}
	}
}
