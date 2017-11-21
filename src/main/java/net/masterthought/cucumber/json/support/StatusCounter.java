package net.masterthought.cucumber.json.support;

import java.util.EnumMap;

/**
 * Keeps information about statuses occurrence.
 * 
 * @author Damian Szczepanik (damianszczepanik@github)
 *
 */
public class StatusCounter {

    private EnumMap<Status, Integer> counter = new EnumMap<>(Status.class);

    private int size = 0;

    public StatusCounter() {
        for (Status status : Status.values()) {
            counter.put(status, 0);
        }
    }

    /**
     * Increments finalStatus counter by single value.
     *
     * @param status
     *            finalStatus for which the counter should be incremented.
     */
    public void incrementFor(Status status) {
        final int statusCounter = getValueFor(status) + 1;
        this.counter.put(status, statusCounter);
        size++;
    }

    /**
     * Gets the number of occurrences for given status.
     *
     * @param status the status
     * @return number of occurrences for given status
     */
    public int getValueFor(Status status) {
        return this.counter.get(status);
    }

    /**
     * Gets the sum of all occurrences for all statuses.
     *
     * @return sum of all occurrences for all statuses
     */
    public int size() {
        return size;
    }

    /**
     * If statuses for all items are the same then this finalStatus is returned, otherwise {@link Status#FAILED}.
     *
     * @return final status for this counter
     */
    public Status getFinalStatus(boolean strict) {
        if (strict) {
            return counter.get(Status.PASSED) == size() ? Status.PASSED : Status.FAILED;
        }
        return Status.PASSED;
    }
}
