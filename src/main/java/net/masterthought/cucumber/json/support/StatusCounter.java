package net.masterthought.cucumber.json.support;

import java.util.HashMap;
import java.util.Map;

/**
 * Keeps information about statuses occurrence.
 * 
 * @author Damian Szczepanik (damianszczepanik@github)
 *
 */
public class StatusCounter {

    private final Map<Status, Integer> counter = new HashMap<>();

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
        this.incrementFor(status, 1);
    }

    /**
     * Increments status counter.
     * 
     * @param status
     *            status for which the counter should be incremented.
     * @param incrementBy
     *            by this value counter will be incremented
     */
    public void incrementFor(Status status, int incrementBy) {
        int counter = this.counter.get(status);
        counter += incrementBy;
        this.counter.put(status, counter);
    }

    /**
     * Gets the counter for given status.
     */
    public int getValueFor(Status status) {
        return this.counter.get(status);
    }
}
