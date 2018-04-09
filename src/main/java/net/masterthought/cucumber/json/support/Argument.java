package net.masterthought.cucumber.json.support;

import net.masterthought.cucumber.json.Row;

/**
 * Protractor implementation uses this for the step parameter.
 *
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class Argument {

    // Start: attributes from JSON file report
    private final Row[] rows = new Row[0];
    // End: attributes from JSON file report

    public Row[] getRows() {
        return rows;
    }
}
