package net.masterthought.cucumber.util;

import org.apache.commons.lang3.mutable.MutableInt;

/**
 * Simple counter to give elements on a page a unique ID. Using object hashes
 * doesn't guarantee uniqueness.
 */
public class Counter extends MutableInt {
    /**
     * @return The next integer
     */
    public int next() {
        increment();
        return intValue();
    }
}
