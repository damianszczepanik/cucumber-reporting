package net.masterthought.cucumber.util;

/**
 * Defines all possible status provided by Cucumber.
 * 
 * @author Damian Szczepanik (damianszczepanik@github)
 *
 */
public enum Status {

    PASSED("#00CE00"),
    FAILED("#FF0000"),
    SKIPPED("#88AAFF"),
    PENDING("#FBB907"),
    UNDEFINED("#FBB957"),
    MISSING("#FBB9A7"),
    HIDDEN("");

    /** Color representation for given status. */
    public final String color;
    
    Status(String color) {
        this.color = color;
    }
    
    /** Returns statuses in order they are displayed. */
    public static Status[] getOrderedStatuses() {
        return new Status[] {PASSED, FAILED, SKIPPED, PENDING, UNDEFINED, MISSING };
    }

    /** Returns name of the status as lower case characters. */
    public String getName() {
        return name().toLowerCase();
    }

    /** Returns name of the status starting from upper case character. */
    public String getLabel() {
        return String.valueOf(name().charAt(0)).toUpperCase() + name().substring(1).toLowerCase();
    }

    public String toHtmlClass() {
        // TODO: should be moved out of this class
        return "<div class=\"" + this.getName().toLowerCase() + "\">";
    }
}