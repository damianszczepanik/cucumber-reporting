package net.masterthought.cucumber.json;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.json.support.Status;
import net.masterthought.cucumber.json.support.StatusCounter;
import net.masterthought.cucumber.util.Util;

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

    private String beforeAttachments;
    private String afterAttachments;
    private Status status;

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

    public String getBeforeAttachments() {
        return beforeAttachments;
    }

    public String getAfterAttachments() {
        return afterAttachments;
    }

    public Status getStatus() {
        return status;
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

    public boolean hasTags() {
        return tags.length > 0;
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

        return id != null ? id.equals(other.id) : super.equals(obj);
    }

    public void setMedaData(Feature feature, Configuration configuration) {
        this.feature = feature;
        for (Step step : steps) {
            step.setMedaData(this);
        }
        calculateHooks(before);
        calculateHooks(after);
        beforeAttachments = calculateAttachments("Before", before);
        afterAttachments = calculateAttachments("After", after);
        status = calculateStatus(configuration);
    }

    private void calculateHooks(Hook[] hooks) {
        for (int i = 0; i < hooks.length; i++) {
            hooks[i].setMedaData();
        }
    }

    private String calculateAttachments(String keyword, Hook[] hooks) {
        StringBuilder sb = new StringBuilder();
        for (Hook hook : hooks) {
            String attachmentStatus = hook.getResult().getStatus();

            sb.append("<div class=\"").append(attachmentStatus).append("\">");
            sb.append("<span class=\"step-keyword\">").append(keyword).append(" </span>");
            sb.append("<i>").append(hook.getMatch().getLocation()).append("</i>");

            sb.append("<span class=\"step-duration\">");
            if (Status.MISSING.getRawName().equals(attachmentStatus)) {
                sb.append(Util.formatDuration(hook.getResult().getDuration()));
            }
            sb.append("</span>");
            sb.append(Util.formatMessage(hook.getResult().getErrorMessage(), hook.getResult().hashCode()));
            sb.append("</div>");

            sb.append(hook.getAttachments());
        }
        return sb.toString();
    }

    private Status calculateStatus(Configuration configuration) {
        StatusCounter statusCounter = new StatusCounter();
        for (Step step : steps) {
            statusCounter.incrementFor(step.getStatus());
        }
        calculateStatusForHook(statusCounter, before);
        calculateStatusForHook(statusCounter, after);

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
