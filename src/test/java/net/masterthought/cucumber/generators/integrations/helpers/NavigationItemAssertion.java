package net.masterthought.cucumber.generators.integrations.helpers;

import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportBuilder;
import net.masterthought.cucumber.ReportConstants;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class NavigationItemAssertion extends LinkAssertion {

    public void hasLinkToJenkins(Configuration configuration) {
        hasLabelAndAddress("Jenkins", "../");
    }

    public void hasLinkToPreviousResult(Configuration configuration, String page) {
        final Integer prevBuildNumber = Integer.parseInt(configuration.getBuildNumber()) - 1;
        hasLabelAndAddress("Previous results", "../../" + prevBuildNumber
                + "/" + ReportConstants.BASE_DIRECTORY + configuration.getDirectorySuffixWithSeparator() + "/" + page);
    }

    public void hasLinkToLastResult(Configuration configuration, String page) {
        hasLabelAndAddress("Latest results", "../../lastCompletedBuild/" + ReportConstants.BASE_DIRECTORY + configuration.getDirectorySuffixWithSeparator() + "/" + page);
    }

    public void hasLinkToFeatures() {
        hasLabelAndAddress("Features", ReportConstants.HOME_PAGE);
    }

    public void hasLinkToTags() {
        hasLabelAndAddress("Tags", "overview-tags.html");
    }

    public void hasLinkToSteps() {
        hasLabelAndAddress("Steps", "overview-steps.html");
    }

    public void hasLinkToTrends() {
        hasLabelAndAddress("Trends", "overview-trends.html");
    }

    public void hasLinkToFailures() {
        hasLabelAndAddress("Failures", "overview-failures.html");
    }
}
