package net.masterthought.cucumber.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.masterthought.cucumber.json.support.Durationable;
import net.masterthought.cucumber.json.support.Status;
import net.masterthought.cucumber.util.Util;
import org.apache.commons.lang3.StringUtils;

public class Result implements Durationable {

    // Start: attributes from JSON file report

    // by default set UNDEFINED status
    // for all cases where Result is not present or completed
    private final Status status = Status.UNDEFINED;
    @JsonProperty("error_message")
    private final String errorMessage = null;
    private final Long duration = 0L;
    // End: attributes from JSON file report

    public Status getStatus() {
        return status;
    }

    @Override
    public long getDuration() {
        return duration;
    }

    @Override
    public String getFormattedDuration() {
        return Util.formatDuration(duration);
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public final String getErrorMessageTitle() {
        if (errorMessage != null) {
            String[] title = errorMessage.split("[\\p{Space}]+");
            if (title.length > 0) {
                return title[0];
            }
        }
        return StringUtils.EMPTY;
    }
}
