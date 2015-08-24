package net.masterthought.cucumber.json.support;

import java.util.List;

import net.masterthought.cucumber.json.Step;
import net.masterthought.cucumber.util.Status;
import net.masterthought.cucumber.util.StatusCounter;
import net.masterthought.cucumber.util.Util;

public class StepResults {

    private final Step[] allSteps;
    private final StatusCounter statusCounter;
    private final long totalDuration;

    public StepResults(List<Step> allSteps, StatusCounter statusCounter, long totalDuration) {
        this.allSteps = new Step[allSteps.size()];
        allSteps.toArray(this.allSteps);
        this.statusCounter = statusCounter;
        this.totalDuration = totalDuration;
    }

    public int getNumberOfSteps() {
        return allSteps.length;
    }

    public int getNumberOfPasses() {
        return statusCounter.getValueFor(Status.PASSED);
    }

    public int getNumberOfFailures() {
        return statusCounter.getValueFor(Status.FAILED);
    }

    public int getNumberOfUndefined() {
        return statusCounter.getValueFor(Status.UNDEFINED);
    }

    public int getNumberOfPending() {
        return statusCounter.getValueFor(Status.PENDING);
    }

    public int getNumberOfSkipped() {
        return statusCounter.getValueFor(Status.SKIPPED);
    }

    public int getNumberOfMissing() {
        return statusCounter.getValueFor(Status.MISSING);
    }

    public long getTotalDuration() {
        return totalDuration;
    }

    public String getTotalDurationAsString() {
        return Util.formatDuration(totalDuration);
    }
}