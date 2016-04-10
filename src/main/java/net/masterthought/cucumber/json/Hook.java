package net.masterthought.cucumber.json;

import net.masterthought.cucumber.json.support.ResultsWithMatch;
import net.masterthought.cucumber.json.support.Status;

public class Hook implements ResultsWithMatch {

    // Start: attributes from JSON file report
    private final Result result = null;
    private final Match match = null;
    // End: attributes from JSON file report

    private Status status;

    @Override
    public Result getResult() {
        return result;
    }

    @Override
    public Status getStatus() {
        return status;
    }

    @Override
    public Match getMatch() {
        return match;
    }

    public void setMedaData() {
        calculateStatus();
    }

    private void calculateStatus() {
        if (result == null) {
            status = Status.MISSING;
        } else {
            status = Status.toStatus(result.getStatus());
        }
    }
}
