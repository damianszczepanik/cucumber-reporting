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

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        return name.equals(((Tag) obj).name);
    }
}
