package net.masterthought.cucumber.json;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.json.support.Status;
import net.masterthought.cucumber.json.support.StatusCounter;

public class Element {

    // Start: attributes from JSON file report
    private final String id = null;
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

    public Status getElementStatus() {
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

    public String getId() {
        return id;
    }

    public String getRawName() {
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
        return SCENARIO_TYPE.equals(type);
    }

    public String getName() {
        return StringUtils.defaultString(StringEscapeUtils.escapeHtml(name));
    }

    public boolean hasSteps() {
        return steps.length > 0;
    }

    public Feature getFeature() {
        return feature;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }

        Element other = (Element) obj;
        // in case of parallel tests, elements are the same when belong to the same feature
        if (feature == other.feature) {
            return id != null ? id.equals(other.id) : super.equals(other);
        } else {
            return false;
        }
    }

    public void setMedaData(Feature feature, Configuration configuration) {
        this.feature = feature;
        for (Step step : steps) {
            step.setMetaData();
        }
        calculateHooks(before);
        calculateHooks(after);
        elementStatus = calculateStatus(configuration);
        beforeStatus = calculateHookStatus(before);
        afterStatus = calculateHookStatus(after);
        stepsStatus = calculateStepsStatus();
    }

    private void calculateHooks(Hook[] hooks) {
        for (Hook hook : hooks) {
            hook.setMedaData();
        }
    }

    private Status calculateHookStatus(Hook[] hooks) {
        StatusCounter statusCounter = new StatusCounter();
        for (Hook hook : hooks) {
            statusCounter.incrementFor(hook.getStatus());
        }

        return statusCounter.getFinalStatus();
    }

    private Status calculateStatus(Configuration configuration) {
        StatusCounter statusCounter = new StatusCounter();
        for (Step step : steps) {
            statusCounter.incrementFor(step.getStatus());
        }
        calculateStatusForHook(statusCounter, before);
        calculateStatusForHook(statusCounter, after);

        return getStatusForConfiguration(statusCounter, configuration);
    }

    private Status calculateStepsStatus() {
        StatusCounter statusCounter = new StatusCounter();
        for (Step step : steps) {
            statusCounter.incrementFor(step.getStatus());
        }
        return statusCounter.getFinalStatus();
    }

    /**
     * Evaluates the elementStatus according to the provided configuration.
     *
     * @param configuration
     *            configuration that keeps the information whether the not-passed elementStatus should fail the build
     * @return evaluated elementStatus
     */
    private Status getStatusForConfiguration(StatusCounter statusCounter, Configuration configuration) {
        if (statusCounter.getValueFor(Status.FAILED) > 0) {
            return Status.FAILED;
        }

        if (configuration.failsIfSkipped() && statusCounter.getValueFor(Status.SKIPPED) > 0) {
            return Status.FAILED;
        }

        if (configuration.failsIFPending() && statusCounter.getValueFor(Status.PENDING) > 0) {
            return Status.FAILED;
        }

        if (configuration.failsIfUndefined() && statusCounter.getValueFor(Status.UNDEFINED) > 0) {
            return Status.FAILED;
        }

        if (configuration.failsIfMissing() && statusCounter.getValueFor(Status.MISSING) > 0) {
            return Status.FAILED;
        }

        return Status.PASSED;
    }

    private void calculateStatusForHook(StatusCounter statusCounter, Hook[] hooks) {
        for (Hook hook : hooks) {
            Result result = hook.getResult();
            if (result != null) {
                statusCounter.incrementFor(Status.toStatus(result.getStatus()));
            }
        }
    }
}
