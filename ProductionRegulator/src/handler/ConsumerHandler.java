package handler;

import lombok.NoArgsConstructor;
import model.Consumer;


@NoArgsConstructor
public class ConsumerHandler extends EntityHandler<Consumer> {
    private static ConsumerHandler instance;

    public static ConsumerHandler getHandler() {
        return instance = instance == null ? new ConsumerHandler() : instance;
    }

    public boolean add(Consumer c) {
        return super.add(c);
    }
}
