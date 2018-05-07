package net.masterthought.cucumber.json.support;

import org.apache.commons.lang.StringUtils;

import net.masterthought.cucumber.ValidationException;
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

    /**
     * Max occured duration for the step.
     */
    private long maxDuration;

    private final StatusCounter statusCounter = new StatusCounter();

    public StepObject(String location) {
        if (StringUtils.isEmpty(location)) {
            throw new ValidationException("Location cannnot be null!");
        }
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public void addDuration(long duration, Status status) {
        this.totalDuration += duration;
        this.totalOccurrences++;
        this.statusCounter.incrementFor(status);
        if (duration > maxDuration) {
            this.maxDuration = duration;
        }
    }

    public long getDuration() {
        return totalDuration;
    }

    public String getFormattedTotalDuration() {
        return Util.formatDuration(totalDuration);
    }

    public long getAverageDuration() {
        return totalDuration / totalOccurrences;
    }

    public String getFormattedAverageDuration() {
        return Util.formatDuration(getAverageDuration());
    }

    public int getTotalOccurrences() {
        return totalOccurrences;
    }

    public long getMaxDuration() {
        return maxDuration;
    }

    public String getFormattedMaxDuration() {
        return Util.formatDuration(maxDuration);
    }

    /**
     * Gets percentage how many steps passed (PASSED / All) formatted to double decimal precision.
     *
     * @return percentage of passed statuses
     */
    public String getPercentageResult() {
        int total = 0;
        for (Status status : Status.values()) {
            total += this.statusCounter.getValueFor(status);
        }
        return Util.formatAsPercentage(this.statusCounter.getValueFor(Status.PASSED), total);
    }

    public Status getStatus() {
        return statusCounter.getFinalStatus();
    }
}
