package net.masterthought.cucumber.json;

import java.util.ArrayList;
import java.util.List;

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

    /** Refers to scenario (not background) step. Is defined in json file. */
    private final static String SCENARIO_TYPE = "scenario";

    private StatusCounter statusCounter;

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
        if (statusCounter == null) {
            calculateStatus();
        }

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

    private void calculateStatus() {
        statusCounter = new StatusCounter();
        for (Step step : steps) {
            statusCounter.incrementFor(step.getStatus());
        }
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
        List<String> contentString = new ArrayList<String>();

        if (StringUtils.isNotEmpty(keyword)) {
            contentString.add("<span class=\"scenario-keyword\">" + StringEscapeUtils.escapeHtml(keyword) + ": </span>");
        }

        if (StringUtils.isNotEmpty(name)) {
            contentString.add("<span class=\"scenario-name\">" + StringEscapeUtils.escapeHtml(name) + "</span>");
        }

        return !contentString.isEmpty() ? getStatus().toHtmlClass()
                + StringUtils.join(contentString.toArray(), " ") + "</div>" : "";
    }

    public boolean hasTags() {
        return tags.length > 0;
    }

    public boolean hasSteps() {
        return steps.length > 0;
    }

    public boolean isScenario() {
        return SCENARIO_TYPE.equals(type);
    }

    public String getTagsList() {
        return Util.tagsToHtml(tags);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
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

        return id != null ? id.equals(other.id) : (other.id == null);
    }

}
