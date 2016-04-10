package net.masterthought.cucumber.json;

import net.masterthought.cucumber.json.support.Status;
import net.masterthought.cucumber.util.Util;

public class Result {

    // Start: attributes from JSON file report
    private final String status = null;
    private final String error_message = null;
    private final Long duration = 0L;
    // End: attributes from JSON file report

    public String getStatus() {
        return status == null ? Status.MISSING.getRawName() : status;
    }

    public long getDuration() {
        return duration;
    }

    public String getFormatedDuration() {
        return Util.formatDuration(duration);
    }

    public String getErrorMessage() {
        return error_message;
    }

    public String getFormatedErrorMessage() {
        final String contentId = "output_" + hashCode();
        return Util.formatMessage("Error message", error_message, contentId);
    }
}
