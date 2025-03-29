package net.masterthought.cucumber;

import net.masterthought.cucumber.generators.ErrorPage;
import net.masterthought.cucumber.generators.FeaturesOverviewPage;

public class ReportConstants {
    /**
     * Page that should be displayed when the reports is generated. Shared between {@link FeaturesOverviewPage} and
     * {@link ErrorPage}.
     */
    public static final String HOME_PAGE = "overview-features.html";

    /**
     * Subdirectory where the report will be created.
     */

    public static final String BASE_DIRECTORY = "cucumber-html-reports";
    /**
     * Separator between main directory name and specified suffix
     */
    public static final String SUFFIX_SEPARATOR = "_";
}