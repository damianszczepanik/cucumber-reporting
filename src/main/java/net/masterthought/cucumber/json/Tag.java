package net.masterthought.cucumber.json;

import net.masterthought.cucumber.util.Util;

public class Tag {

    // Start: attributes from JSON file report
    private final String name = null;
    // End: attributes from JSON file report

    public String getName() {
        return name;
    }

    public String getFileName() {
        return generateFileName(name);
    }

    public static String generateFileName(String tagName) {
        // TODO: the file name should be unique
        return String.format("report-tag_%s.html", Util.toValidFileName(tagName.replace("@", "")).trim());
    }
}
