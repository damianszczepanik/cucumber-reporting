package net.masterthought.cucumber;

import net.masterthought.cucumber.json.support.Status;

/**
 * Defines methods required to generate single report. Implementations of this interface are used by Velocity template.
 *
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public interface Reportable {

    /** Returns name of the element that will be displayed to user. */
    String getName();

    /** Returns number of features for this element. */
    int getFeatures();

    /** Returns number of passed features for this element. */
    int getPassedFeatures();

    /** Returns number of failed features for this element. */
    int getFailedFeatures();

    /** Returns number of scenarios for this element. */
    int getScenarios();

    /** Returns number of passed scenarios for this element. */
    int getPassedScenarios();

    /** Returns number of failed scenarios for this element. */
    int getFailedScenarios();

    /** Returns number of all steps for this element. */
    int getSteps();

    /** Returns number of passed steps for this element. */
    int getPassedSteps();

    /** Returns number of failed steps for this element. */
    int getFailedSteps();

    /** Returns number of skipped steps for this element. */
    int getSkippedSteps();

    /** Returns number of undefined steps for this element. */
    int getUndefinedSteps();

    /** Returns number of pending steps for this element. */
    int getPendingSteps();

    /** Returns duration as milliseconds for this element. */
    long getDuration();

    /** Returns formatted duration for this element. */
    String getFormattedDuration();

    /** Returns status for this element. */
    Status getStatus();
}
