package net.masterthought.cucumber.json;

public class Hook implements ResultsWithMatch {

    private Result result;
    private Match match;

    @Override
    public Result getResult() {
        return result;
    }

    @Override
    public Match getMatch() {
        return match;
    }
}
