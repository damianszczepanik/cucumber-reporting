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
    private final String val = null;
    private final Integer offset = null;
    // End: attributes from JSON file report

    public Row[] getRows() {
        return rows;
    }

    public String getVal() {
        return val;
    }

    public Integer getOffset() {
        return offset;
    }
}
