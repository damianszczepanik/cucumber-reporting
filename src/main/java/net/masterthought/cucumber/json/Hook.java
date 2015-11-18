package net.masterthought.cucumber.json;

import net.masterthought.cucumber.json.support.ResultsWithMatch;

public class Hook implements ResultsWithMatch {

    // Start: attributes from JSON file report
    private final Result result = null;
    private final Match match = null;
    // End: attributes from JSON file report

    @Override
    public Result getResult() {
        return result;
    }

    @Override
    public Match getMatch() {
        return match;
    }
}
