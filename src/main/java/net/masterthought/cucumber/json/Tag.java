package net.masterthought.cucumber.json;

import net.masterthought.cucumber.util.Util;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

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

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(name).toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Tag)) { return false; }
        if(obj == this) { return true; }
        Tag tag = (Tag) obj;
        return new EqualsBuilder().append(this.name, tag.name).isEquals();
    }
}
