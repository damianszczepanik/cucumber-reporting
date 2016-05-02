package net.masterthought.cucumber.json;

public class Tag {

    // Start: attributes from JSON file report
    private final String name = null;
    // End: attributes from JSON file report

    public String getName() {
        return name;
    }

    public String getFileName() {
        return name.replace("@", "").trim() + ".html";
    }
}
