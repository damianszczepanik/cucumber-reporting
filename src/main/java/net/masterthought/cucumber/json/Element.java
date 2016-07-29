package net.masterthought.cucumber.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.json.support.Status;
import net.masterthought.cucumber.json.support.StatusCounter;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

import java.util.Objects;

public class Element {

    // Start: attributes from JSON file report
    // as long as this is private attribute without getter deserialization must be forced by annotation
    @JsonProperty("id")
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

    public String getName() {
        return name;
    }

    public String getEscapedName() {
        return StringUtils.defaultString(StringEscapeUtils.escapeHtml(name));
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

    public Feature getFeature() {
        return feature;
    }

    @Override
    public int hashCode() {
        // background type does not define id
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
        // in case of parallel tests, elements are the same when belong to different features
        if (feature == other.feature) {
            return id != null ? Objects.equals(id, other.id) : super.equals(other);
        } else {
            return false;
        }
    }

    public void setMetaData(Feature feature, Configuration configuration) {
        this.feature = feature;

        elementStatus = calculateStatus(configuration);
        beforeStatus = calculateHookStatus(before);
        afterStatus = calculateHookStatus(after);
        stepsStatus = calculateStepsStatus();
    }

    private Status calculateHookStatus(Hook[] hooks) {
        StatusCounter statusCounter = new StatusCounter();
        for (Hook hook : hooks) {
            statusCounter.incrementFor(hook.getResult().getStatus());
        }

        return statusCounter.getFinalStatus();
    }

    private Status calculateStatus(Configuration configuration) {
        StatusCounter statusCounter = new StatusCounter();
        for (Step step : steps) {
            statusCounter.incrementFor(step.getResult().getStatus());
        }
        calculateStatusForHook(statusCounter, before);
        calculateStatusForHook(statusCounter, after);

        return getStatusForConfiguration(statusCounter, configuration);
    }

    private Status calculateStepsStatus() {
        StatusCounter statusCounter = new StatusCounter();
        for (Step step : steps) {
            statusCounter.incrementFor(step.getResult().getStatus());
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

        if (configuration.failsIfPending() && statusCounter.getValueFor(Status.PENDING) > 0) {
            return Status.FAILED;
        }

        if (configuration.failsIfUndefined() && statusCounter.getValueFor(Status.UNDEFINED) > 0) {
            return Status.FAILED;
        }

        return Status.PASSED;
    }

    private void calculateStatusForHook(StatusCounter statusCounter, Hook[] hooks) {
        for (Hook hook : hooks) {
            statusCounter.incrementFor(hook.getResult().getStatus());
        }
    }
}
