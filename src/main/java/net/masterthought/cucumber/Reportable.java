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

    /** In case of running the same tests on many devices it displays name of the device. */
    String getDeviceName();

    /** Returns number of features for this element. */
    int getFeatures();

    /** Returns number of passed features for this element.
     * a feature is considered passed if all scenarios passed */
    int getPassedFeatures();

    /** Returns number of failed features for this element.
     * a feature is considered failed if there is at least one failed scenario */
    int getFailedFeatures();

    /** A feature that has 0 or more passing, 1 or more pending, but no failures */
    int getPendingFeatures();

    /** A feature that has 0 or more passing, 1 or more undefined, but no failures */
    int getUndefinedFeatures();

    /** Returns number of scenarios for this element. */
    int getScenarios();

    /** Returns number of passed scenarios for this element. */
    int getPassedScenarios();

    /** Returns number of failed scenarios for this element. */
    int getFailedScenarios();

    /** Returns number of scenarios that have pending steps for this element */
    int getPendingScenarios();

    /** Returns number of scenarios that have undefined steps for this element */
    int getUndefinedScenarios();

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
