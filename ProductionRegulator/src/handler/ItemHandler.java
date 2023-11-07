package handler;

import lombok.NoArgsConstructor;
import model.Item;

@NoArgsConstructor
public class ItemHandler extends EntityHandler<Item> {
    private static ItemHandler instance;
    private static int level;
    private static final String[] availabilityLevels = {"HIGH", "LOW"};

    public static synchronized ItemHandler getHandler() {
        return instance = instance == null ? new ItemHandler() : instance;
    }

    public boolean add(Item i) { return super.add(i); }

    public boolean remove() { return super.remove(); }

    public String getAvailabilityLevel() {
        return availabilityLevels[getLevel()];
    }

    private int getLevel() {
        return level;
    }

    public void setLevel(int i) {
        level = i;
    }
}
