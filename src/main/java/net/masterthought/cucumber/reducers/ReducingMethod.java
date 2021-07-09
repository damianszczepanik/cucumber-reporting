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
     * Merge features and scenarios from different JSON files of different runs
     * into a single report by features' and scenarios' ids.
     *
     * Merging rules:
     * - Every new feature which is not in the result list is appended to the end.
     *
     * - When the results list already has a feature with such Id then we go down and apply the rules below to the scenarios:
     *
     *      1. if there is no scenario with a given Id in the feature's elements list
     *         then add the scenario to the end of the list.
     *
     *      2. if there are no scenario with a background (which is a previous element in the elements list)
     *        then both elements are added to the end of the current feature's elements list.
     *        As the feature file has a structure like:
     *        {
     *            elements: [
     *              {
     *                  name: ...
     *                  type: "background";
     *              },
     *              {
     *                  name: ...
     *                  type: "scenario";
     *              },
     *              {
     *                  name: ...
     *                  type: "background";
     *              },
     *              {
     *                  name: ...
     *                  type: "scenario";
     *              }
     *              ....
     *            ]
     *        }
     *
     *      3. if there is a scenario with a given Id then:
     *          scenario + background case: replace both elements (existing element with Id and its background with new ones)
     *          scenario only: replace only given scenario by index in the array.
     *
     * Example:
     * Original cucumber report is "cucumber.json". Let's look a situation when couple of tests failed there.
     * Cucumber runner generates a new report, for example, cucumber-rerun.json as a result of rerun the failed tests.
     *
     * In that case you will have a merged report where all failed tests from the original cucumber.json file
     * are overridden with the results from the cucumber-rerun.json.
     */
    MERGE_FEATURES_WITH_RETEST,

    /**
     * Works as MERGE_FEATURES_WITH_RETEST plus marking as FLACKY tests that after rerun were passed
     * Before merging it checks that scenario was failed and now it's passed and mark it as flacky (add @flacky tag).
     * Also it gets an exception from firstly failed scenario and sets it to passed scenario in the same step/hook so
     * you can analyze the reason but not to fail test suite.
     * Plus it will also move any attachments to passed scenario in the same step/hook from firstly failed scenario AFTER HOOK
     * (The common cucumber solution to embed screenshot in after hook if scenario failed)
     */
    MERGE_FEATURES_WITH_RETEST_MARKING_FLACKY,

    /**
     * Skip empty JSON reports. If this flag is not selected then report generation fails on empty file.
     */
    SKIP_EMPTY_JSON_FILES,

    /**
     * Does not display hooks (@Before and @After) which do not have attachment or error message.
     */
    HIDE_EMPTY_HOOKS
}