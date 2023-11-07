package handler;

import lombok.NoArgsConstructor;
import model.Producer;
import netscape.javascript.JSObject;

import java.io.Serializable;


@NoArgsConstructor
public class ProducerHandler extends EntityHandler<Producer> {
    private static ProducerHandler instance;
    private static int producerInterval;

    public static ProducerHandler getHandler() {
        return instance = instance == null ? new ProducerHandler() : instance;
    }

    public boolean add(Producer p) {
        return super.add(p);
    }

    public boolean remove() {
        if (size() < 1) return false;
        return super.remove();
    }
    public int getProducerInterval() {
        return producerInterval;
    }

    public void setProducerInterval(int interval) {
        producerInterval = interval;
    }



}
