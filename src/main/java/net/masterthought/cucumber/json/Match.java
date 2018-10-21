package net.masterthought.cucumber.json;

public class Match {

    // Start: attributes from JSON file report
    private final String location = null;
    private final MatchArgument[] arguments = new MatchArgument[0];
    // End: attributes from JSON file report

    public String getLocation() {
        return location;
    }

    public MatchArgument[] getArguments() {
        return arguments;
    }
}
