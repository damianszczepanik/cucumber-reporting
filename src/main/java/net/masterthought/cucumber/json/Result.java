package net.masterthought.cucumber.json;

import net.masterthought.cucumber.util.Util;

public class Result {

    // Start: attributes from JSON file report
    private final String status = null;
    private final String error_message = null;
    private final Long duration = 0L;
    // End: attributes from JSON file report

    public String getStatus() {
        return status;
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
}
