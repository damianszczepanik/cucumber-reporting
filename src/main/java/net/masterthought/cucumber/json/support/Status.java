package net.masterthought.cucumber.json.support;

/**
 * Defines all possible status provided by Cucumber.
 * 
 * @author Damian Szczepanik (damianszczepanik@github)
 *
 */
public enum Status {

    // use RGB color instead of predefined such as blue, yellow!
    PASSED("#00CE00"),
    FAILED("#FF0000"),
    SKIPPED("#88AAFF"),
    PENDING("#FBB907"),
    UNDEFINED("#FBB957"),
    MISSING("#FBB9A7");

    /** Color representation for given status. */
    public final String color;
    
    private Status(String color) {
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

    /** Returns name of the status changing first letter to upper case character. */
    public String getLabel() {
        return String.valueOf(name().charAt(0)).toUpperCase() + name().substring(1).toLowerCase();
    }

}