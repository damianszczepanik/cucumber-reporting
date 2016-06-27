package net.masterthought.cucumber.generators.integrations.helpers;

import net.masterthought.cucumber.Configuration;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class NavigationItemAssertion extends LinkAssertion {

    public void hasLinkToJenkins(Configuration configuration) {
        hasLabelAndAddress("Jenkins", "/job/" + configuration.getProjectName() + "/" + configuration.getBuildNumber());
    }

    public void hasLinkToPreviousResult(Configuration configuration, String page) {
        hasLabelAndAddress("Previous results", configuration.getJenkinsPreviousBuildURL() + "/cucumber-html-reports/" + page);
    }

    public void hasLinkToNextResult(Configuration configuration, String page) {
        hasLabelAndAddress("Next results", configuration.getJenkinsNextBuildURL() + "/cucumber-html-reports/" + page);
    }

    public void hasLinkToFeatures() {
        hasLabelAndAddress("Features", "feature-overview.html");
    }

    public void hasLinkToTags() {
        hasLabelAndAddress("Tags", "tag-overview.html");
    }

    public void hasLinkToSteps() {
        hasLabelAndAddress("Steps", "step-overview.html");
    }

    public void hasLinkToFailures() {
        hasLabelAndAddress("Failures", "failures-overview.html");
    }
}
