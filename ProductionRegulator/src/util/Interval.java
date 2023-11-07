package util;

import java.util.Random;

/*
 * Interval class includes a setInterval() method
 * returning a random int value ranging between:
 * boundLow < boundHigh representing time in milliseconds
 */
public class Interval {
    private static final Random random = new Random();
    public static int randomAmount(int boundLow, int boundHigh) {

        return random.nextInt(boundHigh + 1 - boundLow) + boundLow;
    }

    public static int randomInterval(int boundLow, int boundHigh) {

        return 1000 * ( random.nextInt(boundHigh + 1 - boundLow) + boundLow );
    }
}
