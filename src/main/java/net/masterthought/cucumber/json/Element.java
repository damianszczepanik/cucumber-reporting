package net.masterthought.cucumber.json;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

import net.masterthought.cucumber.ConfigurationOptions;
import net.masterthought.cucumber.util.Status;
import net.masterthought.cucumber.util.Util;

public class Element {

    /** Refers to background step. Is defined in json file. */
    private final static String BACKGROUND_KEYWORD = "Background";

    private final String id = null;
    private final String name = null;
    private final String description = null;
    private final String keyword = null;
    private final Step[] steps = new Step[0];
    private final Hook[] before = new Hook[0];
    private final Hook[] after = new Hook[0];
    private final Tag[] tags = new Tag[0];

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
        if (containsStepWithStatus(Status.FAILED)) {
            return Status.FAILED;
        }

        ConfigurationOptions configuration = ConfigurationOptions.instance();
        if (configuration.skippedFailsBuild()) {
            if (containsStepWithStatus(Status.SKIPPED)) {
                return Status.FAILED;
            }
        }

        if (configuration.pendingFailsBuild()) {
            if (containsStepWithStatus(Status.PENDING)) {
                return Status.FAILED;
            }
        }

        if (configuration.undefinedFailsBuild()) {
            if (containsStepWithStatus(Status.UNDEFINED)) {
                return Status.FAILED;
            }
        }

        if (configuration.missingFailsBuild()) {
            if (containsStepWithStatus(Status.MISSING)) {
                return Status.FAILED;
            }
        }
        
        return Status.PASSED;
    }

    /**
     * Checks if there is any step with passed status.
     * 
     * @param status
     *            status that should be filtered out
     * @return true if there is status with passed status, false otherwise
     */
    private boolean containsStepWithStatus(Status status) {
        for (Step step : steps) {
            if (step.getStatus() == status) {
                return true;
            }
        }
        return false;
    }

    public String getRawName() {
        return name;
    }

    public String getKeyword() {
        return keyword;
    }

    public String getName() {
        List<String> contentString = new ArrayList<String>();

        if (Util.itemExists(keyword)) {
            contentString.add("<span class=\"scenario-keyword\">" + StringEscapeUtils.escapeHtml(keyword) + ": </span>");
        }

        if (Util.itemExists(name)) {
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

    public boolean isBackground() {
        return keyword.equals(BACKGROUND_KEYWORD);
    }

    public String getTagsList() {
        return Util.tagsToHtml(tags);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
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

        return id.equals(other.id);
    }

}
