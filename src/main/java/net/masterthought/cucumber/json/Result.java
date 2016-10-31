package net.masterthought.cucumber.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang.StringEscapeUtils;

import net.masterthought.cucumber.json.support.Status;
import net.masterthought.cucumber.util.Util;

public class Result {

    // Start: attributes from JSON file report
    private final Status status = null;
    @JsonProperty("error_message")
    private final String errorMessage = null;
    private final Long duration = 0L;
    // End: attributes from JSON file report

    public Status getStatus() {
        return status;
    }

    public long getDuration() {
        return duration;
    }

    public String getFormatedDuration() {
        return Util.formatDuration(duration);
    }

    /** Returns error message escaped so it is properly displayed as HTML. */
    public String getErrorMessage() {
        return StringEscapeUtils.escapeHtml(errorMessage);
    }

    // method used in Velocity templating library
    public String getErrorMessageTitle() {
        return errorMessage.split("\\r\\n|\\r|\\n")[0];
    }
}
