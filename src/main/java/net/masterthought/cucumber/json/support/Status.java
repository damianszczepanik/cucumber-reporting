package net.masterthought.cucumber.json.support;

/**
 * Defines all possible statuses provided by cucumber-jvm library.
 * 
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public enum Status {

    PASSED,
    FAILED,
    SKIPPED,
    PENDING,
    UNDEFINED,
    MISSING;

    /**
     * Returns name of the status converted to lower case characters.
     */
    public String getRawName() {
        return name().toLowerCase();
    }

    /** Returns name of the status changing first letter to upper case character. */
    public String getLabel() {
        return name().substring(0, 1).toUpperCase() + name().substring(1).toLowerCase();
    }

    public static Status toStatus(String status) {
        return valueOf(status.toUpperCase());
    }

    /** Returns true if status is equal to {@link #PASSED}. */
    public boolean isPassed() {
        return this == PASSED;
    }
}