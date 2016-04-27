package net.masterthought.cucumber.json.support;

import org.apache.commons.lang.StringUtils;

import net.masterthought.cucumber.util.Util;

/**
 * Keeps information about steps statistics.
 * 
 * @author Damian Szczepanik (damianszczepanik@github)
 *
 */
public class StepObject implements Comparable<StepObject> {

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
        this.statusCounter.incrementFor(Status.toStatus(status));
    }

    public long getDurations() {
        return totalDuration;
    }

    public String getTotalFormattedDuration() {
        return Util.formatDuration(totalDuration);
    }

    public long getAverageDuration() {
        return totalDuration / totalOccurrences;
    }

    public String getAverageFormattedDuration() {
        return Util.formatDuration(getAverageDuration());
    }

    public int getTotalOccurrences() {
        return totalOccurrences;
    }

    /** Returns in as percentage how many steps passed (PASSED / All) formatted to double decimal precision. */
    public String getPercentageResult() {
        int total = 0;
        for (Status status : Status.values()) {
            total += this.statusCounter.getValueFor(status);
        }
        if (total == 0) {
            return Util.PERCENT_FORMATTER.format(0);
        } else {
            // 1F is to force floating conversion instead of loosing decimal part
            return Util.PERCENT_FORMATTER.format(1F * this.statusCounter.getValueFor(Status.PASSED) / total);
        }
    }

    public Status getStatus() {
        return statusCounter.getFinalStatus();
    }

    @Override
    public int compareTo(StepObject o) {
        if (StringUtils.isEmpty(location)) {
            return -1;
        }
        if (StringUtils.isEmpty(o.getLocation())) {
            return 1;
        }
        return Integer.signum(location.compareTo(o.getLocation()));
    }
}
