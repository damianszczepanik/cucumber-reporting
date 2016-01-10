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

    public StatusCounter() {
        for (Status status : Status.values()) {
            counter.put(status, 0);
        }
    }

    /**
     * Increments status counter by 1.
     * 
     * @param status
     *            status for which the counter should be incremented.
     */
    public void incrementFor(Status status) {
        int counter = this.counter.get(status);
        counter++;
        this.counter.put(status, counter);
    }

    /**
     * Gets the counter for given status.
     */
    public int getValueFor(Status status) {
        return this.counter.get(status);
    }

    /** Sums all occurrences for all statuses. */
    public int size() {
        int size = 0;
        for (Status status : Status.values()) {
            size += counter.get(status).intValue();
        }
        return size;
    }
}
