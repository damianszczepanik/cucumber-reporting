package net.masterthought.cucumber;

import net.masterthought.cucumber.json.support.Status;

/**
 * Defines methods required to generate single report. Implementations of this interface are used by Velocity template.
 *
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public interface Reportable {

    /**
     * @return name of the element that will be displayed to user.
     */
    String getName();

    /**
     * @return number of features for this element.
     */
    int getFeatures();

    /**
     * @return number of passed features for this element.
     */
    int getPassedFeatures();

    /**
     * @return number of failed features for this element.
     */
    int getFailedFeatures();

    /**
     * @return number of scenarios for this element.
     */
    int getScenarios();

    /**
     * @return number of passed scenarios for this element.
     */
    int getPassedScenarios();

    /**
     * @return number of failed scenarios for this element.
     */
    int getFailedScenarios();

    /**
     * @return number of all steps for this element.
     */
    int getSteps();

    /**
     * @return number of passed steps for this element.
     */
    int getPassedSteps();

    /**
     * @return number of failed steps for this element.
     */
    int getFailedSteps();

    /**
     * @return number of skipped steps for this element.
     */
    int getSkippedSteps();

    /**
     * @return number of undefined steps for this element.
     */
    int getUndefinedSteps();

    /**
     * @return number of pending steps for this element.
     */
    int getPendingSteps();

    /**
     * @return duration as milliseconds for this element.
     */
    long getDuration();

    /**
     * @return formatted duration for this element.
     */
    String getFormattedDuration();

    /**
     * @return status for this element.
     */
    Status getStatus();
}
