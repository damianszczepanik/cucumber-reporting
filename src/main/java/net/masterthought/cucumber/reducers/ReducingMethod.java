package net.masterthought.cucumber.reducers;

/**
 * Supported reducing methods.
 * This list contains supported methods that allow to modify the way how reports are displayed.
 *
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public enum ReducingMethod {

    /**
     * Merge features with the same ID so scenarios are be merged into single feature.
     */
    MERGE_FEATURES_BY_ID,

    /**
     * Skip JSON reports which have been parsed but have none features or are empty file at all.
     */
    SKIP_EMPTY_JSON_FILES
}