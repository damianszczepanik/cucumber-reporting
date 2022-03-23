package net.masterthought.cucumber.json.support;

/**
 * Ensures that class delivers methods for duration.
 *
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public interface Durationable {

    /**
     * Returns duration for given item.
     *
     * @return duration
     */
    long getDuration();

    /**
     * Returns duration displayed in humanable format.
     *
     * @return formatted duration
     */
    String getFormattedDuration();
}
