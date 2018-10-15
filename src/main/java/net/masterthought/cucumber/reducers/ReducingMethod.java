package net.masterthought.cucumber.reducers;

/**
 * Supported reducing methods.
 * This list contains supported methods that allow to modify the way how reports are displayed.
 *
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public enum ReducingMethod {

    /**
     * Adds index postfix to each feature name.
     * This is strongly recommended when there are more features with the same ID,
     * so each one is stored in different file.
     */
    FEATURE_FILE_NAME_WITH_NO,
    /**
     * Merge features if they have same ID so scenrios will be merged into one scenario.
     */
    MERGE_FEATURES_BY_ID
}