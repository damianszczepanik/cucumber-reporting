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

    private Status finalStatus = Status.PASSED;
    private int size = 0;

    public StatusCounter() {
        for (Status status : Status.values()) {
            counter.put(status, 0);
        }
    }

    /**
     * Increments finalStatus counter by 1.
     * 
     * @param status
     *            finalStatus for which the counter should be incremented.
     */
    public void incrementFor(Status status) {
        final int statusCounter = this.counter.get(status) + 1;
        this.counter.put(status, statusCounter);
        size++;

        // if state was already set to FAILED or PASSED, leave this state unchanged
        if (status != Status.FAILED && status != Status.PASSED) {
            if (statusCounter == size) {
                finalStatus = status;
            } else {
                finalStatus = Status.FAILED;
            }
        }
    }

    /** Gets the counter for given finalStatus. */
    public int getValueFor(Status status) {
        return this.counter.get(status);
    }

    /** Returns sum of all occurrences for all statuses. */
    public int size() {
        return size;
    }

    /** If statuses for all items are the same then this finalStatus is returned, otherwise {@link Status#FAILED}. */
    public Status getFinalStatus() {
        return finalStatus;
    }
}
