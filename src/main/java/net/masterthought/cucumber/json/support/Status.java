package net.masterthought.cucumber.json.support;

/**
 * Defines all possible statuses provided by cucumber-jvm.
 * 
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public enum Status {

    // use RGB color instead of predefined such as blue, yellow!
    PASSED("#00A000"),
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

    /** Returns status colors in order they are displayed (is the same as defined above). */
    public static String[] getOrderedColors() {
        Status[] allStatuses = Status.values();
        String[] colors = new String[Status.values().length];
        for (int i = 0; i < colors.length; i++) {
            colors[i] = allStatuses[i].color;
        }

        return colors;
    }

    /** Returns name of the status as lower case characters. */
    public String getName() {
        return name().toLowerCase();
    }

    /** Returns name of the status changing first letter to upper case character. */
    public String getLabel() {
        return name().substring(0, 1).toUpperCase() + name().substring(1).toLowerCase();
    }

}