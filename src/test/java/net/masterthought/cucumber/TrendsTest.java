package net.masterthought.cucumber;

import static org.assertj.core.api.Assertions.assertThat;

import mockit.Deencapsulation;
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

    @Test
    public void limitItems_ReducesNumberOfItems() {

        // given
        final int limit = 1;
        Trends trends = new Trends();
        // make sure that there is some data added already
        for (int i = 0; i < limit + 3; i++) {
            trends.addBuild("1", 2, 3, 4, 5, 6, 7);
        }

        // when
        trends.limitItems(limit);

        // then
        assertThat(trends.getBuildNumbers()).hasSize(limit).containsExactly("1");
        assertThat(trends.getFailedFeatures()).hasSize(limit).containsExactly(2);
        assertThat(trends.getTotalFeatures()).hasSize(limit).containsExactly(3);
        assertThat(trends.getFailedScenarios()).hasSize(limit).containsExactly(4);
        assertThat(trends.getTotalScenarios()).hasSize(limit).containsExactly(5);
        assertThat(trends.getFailedSteps()).hasSize(limit).containsExactly(6);
        assertThat(trends.getTotalSteps()).hasSize(limit).containsExactly(7);
    }

    @Test
    public void copyLastElements_OnBigLimit_ReturnsPassedIntArray() {

        // given
        final int[] array = new int[]{3, 4, 5, 6, 7, 8};
        final int limit = array.length + 1;

        // when
        int[] limitedArray = Deencapsulation.invoke(Trends.class, "copyLastElements", array, limit);

        // then
        assertThat(limitedArray).isSameAs(array);
    }

    @Test
    public void copyLastElements_OnBigLimit_ReturnsPassedStringArray() {

        // given
        final String[] array = new String[]{"3", "4", "5", "6", "7", "8"};
        final int limit = array.length + 1;

        // when
        String[] limitedArray = Deencapsulation.invoke(Trends.class, "copyLastElements", array, limit);

        // then
        assertThat(limitedArray).isSameAs(array);
    }
}