package net.masterthought.cucumber.reducers;

/**
 * Supported reducing methods.
 *
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public enum ReducingMethod {

    /**
     * Merge features if they have same ID so scenrios will be merged into one scenario.
     */
    MERGE_FEATURES_BY_ID,
    /**
     * Displays report as it was passed, without any modification.
     */
    NONE
}