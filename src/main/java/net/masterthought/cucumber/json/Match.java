package net.masterthought.cucumber.json;

import net.masterthought.cucumber.json.support.Argument;

public class Match {

    // Start: attributes from JSON file report
    private final String location = null;
    private final Argument[] arguments = new Argument[0];
    // End: attributes from JSON file report

    public String getLocation() {
        return location;
    }

    public Argument[] getArguments() {
        return arguments;
    }
}
