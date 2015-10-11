package net.masterthought.cucumber;

import java.util.HashMap;
import java.util.Map;

import net.masterthought.cucumber.json.support.Status;
import net.masterthought.cucumber.util.Util;

/**
 * Created by JALASOFT\paolo^lizarazu on 09-01-14.
 */
public class Background {
    private final Map<Status, Integer> stepsCounter = new HashMap<>();

    private int totalScenarios;
    private int totalScenariosPassed;
    private int totalScenariosFailed;
    private int totalSteps;
    private long totalDuration;

    public Background() {
        for (Status status : Status.values()) {
            stepsCounter.put(status, 0);
        }
    }

    public int getTotalScenarios() {
        return totalScenarios;
    }
    public int addTotalScenarios(int add) {
        return totalScenarios+=add;
    }

    public int getTotalScenariosPassed() {
        return totalScenariosPassed;
    }

    public void incPassedScenarios() {
        this.totalScenariosPassed++;
    }

    public int getTotalScenariosFailed() {
        return totalScenariosFailed;
    }

    public void incFailedScenarios() {
        this.totalScenariosFailed++;
    }

    public int getTotalSteps() {
        return totalSteps;
    }

    public void addTotalSteps(int totalSteps) {
        this.totalSteps += totalSteps;
    }

    public int getTotalStepsPassed() {
        return getTotalStepsForStatus(Status.PASSED);
    }

    public int getTotalStepsFailed() {
        return getTotalStepsForStatus(Status.FAILED);
    }

    public int getTotalStepsSkipped() {
        return getTotalStepsForStatus(Status.SKIPPED);
    }

    public int getTotalStepsUndefined() {
        return getTotalStepsForStatus(Status.UNDEFINED);
    }

    public int getTotalStepsMissing() {
        return getTotalStepsForStatus(Status.MISSING);
    }

    public int getTotalStepsPending() {
        return getTotalStepsForStatus(Status.PENDING);
    }

    public void incrStepCounterForStatus(Status status) {
        Integer counter = this.stepsCounter.get(status);
        counter++;
        this.stepsCounter.put(status, counter);
    }

    public int getTotalStepsForStatus(Status status) {
        return stepsCounter.get(status);
    }

    public String getTotalFormattedDuration() {
        return Util.formatDuration(totalDuration);
    }

    public long getTotalDuration() {
        return totalDuration;
    }

    public void incrTotalDurationBy(Long totalDuration) {
        this.totalDuration += totalDuration;
    }
}
