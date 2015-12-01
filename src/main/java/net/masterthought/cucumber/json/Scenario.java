package net.masterthought.cucumber.json;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

import net.masterthought.cucumber.ConfigurationOptions;
import net.masterthought.cucumber.json.support.Status;
import net.masterthought.cucumber.json.support.StatusCounter;
import net.masterthought.cucumber.util.Util;

public class Scenario {

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

    private String beforeAttachments;
    private String afterAttachments;

    private StatusCounter statusCounter;
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
        if (statusCounter.getValueFor(Status.FAILED) > 0) {
            return Status.FAILED;
        }

        ConfigurationOptions configuration = ConfigurationOptions.instance();
        if (configuration.skippedFailsBuild() && statusCounter.getValueFor(Status.SKIPPED) > 0) {
            return Status.FAILED;
        }

        if (configuration.pendingFailsBuild() && statusCounter.getValueFor(Status.PENDING) > 0) {
            return Status.FAILED;
        }

        if (configuration.undefinedFailsBuild() && statusCounter.getValueFor(Status.UNDEFINED) > 0) {
            return Status.FAILED;
        }

        if (configuration.missingFailsBuild() && statusCounter.getValueFor(Status.MISSING) > 0) {
            return Status.FAILED;
        }

        return Status.PASSED;
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

    public String getName() {
        StringBuilder sb = new StringBuilder();
        sb.append("<div class=\"").append(getStatus().getName().toLowerCase()).append("\">");

        if (StringUtils.isNotEmpty(keyword)) {
            sb.append("<span class=\"scenario-keyword\">").append(StringEscapeUtils.escapeHtml(keyword))
                    .append(": </span>");
        }

        if (StringUtils.isNotEmpty(name)) {
            sb.append("<span class=\"scenario-name\">").append(StringEscapeUtils.escapeHtml(name)).append("</span>");
        }
        sb.append("</div>");

        return sb.toString();
    }

    public boolean hasTags() {
        return tags.length > 0;
    }

    public boolean hasSteps() {
        return steps.length > 0;
    }

    public String getTagsList() {
        return Util.tagsToHtml(tags);
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
        Scenario other = (Scenario) obj;

        return id != null ? id.equals(other.id) : super.equals(obj);
    }

    public void setMedaData(Feature feature) {
        this.feature = feature;
        for (Step step : steps) {
            step.setMedaData(this);
        }
        calculateStatus();
        calculateHooks(before);
        calculateHooks(after);
        beforeAttachments = calculateAttachments("Before", before);
        afterAttachments = calculateAttachments("After", after);
    }

    private void calculateStatus() {
        statusCounter = new StatusCounter();
        for (Step step : steps) {
            statusCounter.incrementFor(step.getStatus());
        }
    }

    private void calculateHooks(Hook[] hooks) {
        for (int i = 0; i < hooks.length; i++) {
            hooks[i].setMedaData();
        }
    }

    private String calculateAttachments(String keyword, Hook[] hooks) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < hooks.length; i++) {
            String status = hooks[i].getResult().getStatus();

            sb.append("<div class=\"").append(status).append("\">");
            sb.append("<span class=\"step-keyword\">").append(keyword).append(" </span>");
            sb.append("<i>").append(hooks[i].getMatch().getLocation()).append("</i>");

            sb.append("<span class=\"step-duration\">");
            if (status != Status.MISSING.getName()) {
                sb.append(Util.formatDuration(hooks[i].getResult().getDuration()));
            }
            sb.append("</span>");
            sb.append(Util.formatErrorMessage(hooks[i].getResult().getErrorMessage(), hooks[i].getResult().hashCode()));
            sb.append("</div>");

            sb.append(hooks[i].getAttachments());
        }
        return sb.toString();
    }
}
