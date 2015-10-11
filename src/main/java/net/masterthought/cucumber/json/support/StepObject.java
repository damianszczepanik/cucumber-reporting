package net.masterthought.cucumber.json.support;

import net.masterthought.cucumber.util.Util;

/**
 * Keeps information about steps statistics.
 * 
 * @author Damian Szczepanik (damianszczepanik@github)
 *
 */
public class StepObject {

    /** Name of the method / step implementation. This value is unique, there are no two steps with the same locations. */
    public final String location;

    /** Time that was spend to execute all occurrence of this step. */
    private long totalDuration;

    /** How many times this step was executed. */
    private int totalOccurrences;

    private final StatusCounter statusCounter = new StatusCounter();

    public StepObject(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public void addDuration(long duration, String status) {
        this.totalDuration += duration;
        this.totalOccurrences++;
        this.statusCounter.incrementFor(Status.valueOf(status.toUpperCase()));
    }

    public long getTotalDuration() {
        return totalDuration;
    }

    public String getTotalFormattedDuration() {
        return Util.formatDuration(totalDuration);
    }

    public int getTotalOccurrences() {
        return totalOccurrences;
    }

    /** Returns in as percentage how many steps passed (PASSED / All ). */
    public float getPercentageResult() {
        int total = 0;
        for (Status status : Status.values()) {
            total += this.statusCounter.getValueFor(status);
        }
        if (total == 0) {
            return 0;
        } else {
            return 100 * this.statusCounter.getValueFor(Status.PASSED) / total;
        }
    }
}
