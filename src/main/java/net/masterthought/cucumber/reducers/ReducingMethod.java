package net.masterthought.cucumber.reducers;

/**
 * Supported reducing methods.
 * This list contains supported methods that allow to modify the way how reports are displayed.
 *
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public enum ReducingMethod {

    /**
     * Merge features with different JSON files that have same ID so scenarios are be stored in single feature.
     */
    MERGE_FEATURES_BY_ID,

    /**
     * Skip empty JSON reports. If this flag is not selected then report generation fails on empty file.
     */
    SKIP_EMPTY_JSON_FILES
}