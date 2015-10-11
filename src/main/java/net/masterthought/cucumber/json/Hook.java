package net.masterthought.cucumber.json;

import net.masterthought.cucumber.json.support.ResultsWithMatch;

public class Hook implements ResultsWithMatch {

    private final Result result = null;
    private final Match match = null;

    @Override
    public Result getResult() {
        return result;
    }

    @Override
    public Match getMatch() {
        return match;
    }
}
