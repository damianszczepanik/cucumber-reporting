package net.masterthought.cucumber.generators.integrations.helpers;

import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportBuilder;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class NavigationItemAssertion extends LinkAssertion {

    public void hasLinkToJenkins(Configuration configuration) {
        hasLabelAndAddress("Jenkins", "../" + configuration.getJenkinsBasePath());
    }

    public void hasLinkToPreviousResult(Configuration configuration, String page) {
        final Integer prevBuildNumber = Integer.parseInt(configuration.getBuildNumber()) - 1;
        hasLabelAndAddress("Previous results", "../.." + configuration.getJenkinsBasePath() + "/" + prevBuildNumber
                + "/" + ReportBuilder.BASE_DIRECTORY + "/" + page);
    }

    public void hasLinkToLastResult(Configuration configuration, String page) {
        hasLabelAndAddress("Latest results", "../../lastBuild/" + ReportBuilder.BASE_DIRECTORY + "/" + page);
    }

    public void hasLinkToFeatures() {
        hasLabelAndAddress("Features", ReportBuilder.HOME_PAGE);
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
