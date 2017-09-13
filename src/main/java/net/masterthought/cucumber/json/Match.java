package net.masterthought.cucumber.json;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class Match {

    // Start: attributes from JSON file report
    private final String location = null;
    // End: attributes from JSON file report

    public String getLocation() {
        return location;
    }

    @Override
    public int hashCode() { return new HashCodeBuilder().append(location).toHashCode(); }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Match)) { return false; }
        if(obj == this) { return true; }
        Match match = (Match) obj;
        return new EqualsBuilder().append(this.location, match.location).isEquals();
    }
}
