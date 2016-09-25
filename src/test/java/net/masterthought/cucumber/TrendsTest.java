package net.masterthought.cucumber;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class TrendsTest {

    @Test
    public void addBuild_AddsNewResultAtTheLastPosition() {

        // given
        Trends trends = new Trends();
        // make sure that there is some data added already
        trends.addBuild("1", 2, 3, 4, 5, 6, 7);

        final String buildNumber = "this is the build!";
        final int failedFeature = 10;
        final int totalFeature = 30;
        final int failedScenario = 40;
        final int totalScenario = 300;
        final int failedStep = 400;
        final int totalStep = 700;

        // when
        trends.addBuild(buildNumber, failedFeature, totalFeature, failedScenario, totalScenario, failedStep, totalStep);

        // then
        assertThat(trends.getBuildNumbers()).hasSize(2).endsWith(buildNumber);
        assertThat(trends.getFailedFeatures()).hasSize(2).endsWith(failedFeature);
        assertThat(trends.getTotalFeatures()).hasSize(2).endsWith(totalFeature);
        assertThat(trends.getFailedScenarios()).hasSize(2).endsWith(failedScenario);
        assertThat(trends.getTotalScenarios()).hasSize(2).endsWith(totalScenario);
        assertThat(trends.getFailedSteps()).hasSize(2).endsWith(failedStep);
        assertThat(trends.getTotalSteps()).hasSize(2).endsWith(totalStep);
    }
}