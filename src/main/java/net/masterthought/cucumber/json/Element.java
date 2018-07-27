package net.masterthought.cucumber.json;

import org.apache.commons.lang.StringUtils;

import net.masterthought.cucumber.json.support.Durationable;
import net.masterthought.cucumber.json.support.Status;
import net.masterthought.cucumber.json.support.StatusCounter;
import net.masterthought.cucumber.util.Util;

public class Element implements Durationable {

    // Start: attributes from JSON file report
    private final String name = null;
    private final String type = null;
    private final String description = null;
    private final String keyword = null;
    private final Step[] steps = new Step[0];
    private final Hook[] before = new Hook[0];
    private final Hook[] after = new Hook[0];
    private final Tag[] tags = new Tag[0];
    // End: attributes from JSON file report

    private static final String SCENARIO_TYPE = "scenario";

    private Status elementStatus;
    private Status beforeStatus;
    private Status afterStatus;
    private Status stepsStatus;

    private Feature feature;
    private long duration;

    public Step[] getSteps() {
        return steps;
    }

    public Hook[] getBefore() {
        return before;
    }

    public Hook[] getAfter() {
        return after;
    }

    public Tag[] getTags() {
        return tags;
    }

    public Status getStatus() {
        return elementStatus;
    }

    public Status getBeforeStatus() {
        return beforeStatus;
    }

    public Status getAfterStatus() {
        return afterStatus;
    }

    public Status getStepsStatus() {
        return stepsStatus;
    }

    public String getName() {
        return name;
    }

    public String getKeyword() {
        return keyword;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return StringUtils.defaultString(description);
    }

    public boolean isScenario() {
        return SCENARIO_TYPE.equalsIgnoreCase(type);
    }

    public Feature getFeature() {
        return feature;
    }

    @Override
    public long getDuration() {
        return duration;
    }

    @Override
    public String getFormattedDuration() {
        return Util.formatDuration(duration);
    }

    public void setMetaData(Feature feature) {
        this.feature = feature;

        for (Step step : steps) {
            step.setMetaData();
        }

        beforeStatus = new StatusCounter(before).getFinalStatus();
        afterStatus = new StatusCounter(after).getFinalStatus();
        stepsStatus = new StatusCounter(steps).getFinalStatus();
        elementStatus = calculateElementStatus();

        calculateDuration();
    }

    private Status calculateElementStatus() {
        StatusCounter statusCounter = new StatusCounter();
        statusCounter.incrementFor(stepsStatus);
        statusCounter.incrementFor(beforeStatus);
        statusCounter.incrementFor(afterStatus);
        return statusCounter.getFinalStatus();
    }

    private void calculateDuration() {
        for (Step step : steps) {
            duration += step.getResult().getDuration();
        }
    }
}
