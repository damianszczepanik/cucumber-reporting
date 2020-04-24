package net.masterthought.cucumber.json;

import net.masterthought.cucumber.util.Util;

public class Tag {

    // Start: attributes from JSON file report
    private final String name;
    // End: attributes from JSON file report

    public Tag(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getFileName() {
        return generateFileName(name);
    }

    public static String generateFileName(String tagName) {
        return String.format("report-tag_%s.html", Util.toValidFileName(tagName));
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object tag) {
        // not fully implemented but I don't expect to have different objects here
        return ((Tag) tag).name.equals(name);
    }
}
