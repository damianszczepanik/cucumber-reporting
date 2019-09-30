package net.masterthought.cucumber.json.support;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Set;

/**
 * Keeps information about statuses occurrence.
 * 
 * @author Damian Szczepanik (damianszczepanik@github)
 *
 */
public class StatusCounter {

    private EnumMap<Status, Integer> counter = new EnumMap<>(Status.class);

    /**
     * Is equal to {@link Status#FAILED} when at least counted status is not {@link Status#PASSED},
     * otherwise set to {@link Status#PASSED}.
     */
    private Status finalStatus = Status.PASSED;

    private int size = 0;

    public StatusCounter(Resultsable[] resultsables) {
        this(resultsables, Collections.emptySet());
    }

    public StatusCounter(Resultsable[] resultsables, Set<Status> notFailingStatuses) {
        this();
        for (Resultsable resultsable : resultsables) {
            Status status = resultsable.getResult().getStatus();
            if (notFailingStatuses != null && notFailingStatuses.contains(status)) {
                incrementFor(Status.PASSED);
            } else {
                incrementFor(status);
            }
        }
    }

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

        if (finalStatus == Status.PASSED && status != Status.PASSED) {
            finalStatus = Status.FAILED;
        }
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
    public Status getFinalStatus() {
        return finalStatus;
    }
}
