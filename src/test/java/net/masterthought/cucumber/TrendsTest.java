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
        Reportable result = ReportableBuilder.buildSample();
        trends.addBuild("buildName", result);

        final String buildNumber = "this is the build!";

        // when
        trends.addBuild(buildNumber, result);

        // then
        assertThat(trends.getBuildNumbers()).hasSize(2).endsWith(buildNumber);

        assertThat(trends.getPassedFeatures()).hasSize(2).endsWith(result.getPassedFeatures());
        assertThat(trends.getFailedFeatures()).hasSize(2).endsWith(result.getFailedFeatures());
        assertThat(trends.getTotalFeatures()).hasSize(2).endsWith(result.getFeatures());

        assertThat(trends.getPassedScenarios()).hasSize(2).endsWith(result.getPassedScenarios());
        assertThat(trends.getFailedScenarios()).hasSize(2).endsWith(result.getFailedScenarios());
        assertThat(trends.getTotalScenarios()).hasSize(2).endsWith(result.getScenarios());

        assertThat(trends.getPassedSteps()).hasSize(2).endsWith(result.getPassedSteps());
        assertThat(trends.getFailedSteps()).hasSize(2).endsWith(result.getFailedSteps());
        assertThat(trends.getSkippedSteps()).hasSize(2).endsWith(result.getSkippedSteps());
        assertThat(trends.getPendingSteps()).hasSize(2).endsWith(result.getPendingSteps());
        assertThat(trends.getUndefinedSteps()).hasSize(2).endsWith(result.getUndefinedSteps());
        assertThat(trends.getTotalSteps()).hasSize(2).endsWith(result.getSteps());

        assertThat(trends.getDurations()).hasSize(2).endsWith(3206126182390L);
    }

    @Test
    public void addBuild_OnMissingDataForSteps_FillsMissingDataForSteps() {

        // given
        Trends trends = new Trends();
        // make sure that there is some data added already
        Reportable result = ReportableBuilder.buildSample();
        trends.addBuild("buildName", result);
        final String[] buildNumbers = new String[]{"a", "b", "e"};
        Deencapsulation.setField(trends, "buildNumbers", buildNumbers);
        
        // when
        trends.addBuild("the build!", result);

        // then
        assertThat(trends.getBuildNumbers()).hasSize(buildNumbers.length + 1).containsExactly("a", "b", "e", "the build!");

        assertThat(trends.getPassedFeatures()).hasSize(buildNumbers.length + 1).containsExactly(0, 0, 2, 2);

        assertThat(trends.getPassedScenarios()).hasSize(buildNumbers.length + 1).containsExactly(0, 0, 7, 7);

        assertThat(trends.getPassedSteps()).hasSize(buildNumbers.length + 1).containsExactly(0, 0, 17, 17);
        assertThat(trends.getSkippedSteps()).hasSize(buildNumbers.length + 1).containsExactly(0, 0, 23, 23);
        assertThat(trends.getPendingSteps()).hasSize(buildNumbers.length + 1).containsExactly(0, 0, 29, 29);
        assertThat(trends.getUndefinedSteps()).hasSize(buildNumbers.length + 1).containsExactly(0, 0, 31, 31);

        assertThat(trends.getDurations()).hasSize(buildNumbers.length + 1).containsExactly(-1L, -1L, 3206126182390L, 3206126182390L);
    }

    @Test
    public void limitItems_ReducesNumberOfItems() {

        // given
        final int limit = 1;
        final String buildName = "a, e -> c";
        Trends trends = new Trends();
        Reportable result = ReportableBuilder.buildSample();
        // make sure that there is some data added already
        for (int i = 0; i < limit + 3; i++) {
            trends.addBuild(buildName, result);
        }

        // when
        trends.limitItems(limit);

        // then
        assertThat(trends.getBuildNumbers()).hasSize(limit).containsExactly(buildName);

        assertThat(trends.getPassedFeatures()).hasSize(limit).containsExactly(result.getPassedFeatures());
        assertThat(trends.getFailedFeatures()).hasSize(limit).containsExactly(result.getFailedFeatures());
        assertThat(trends.getTotalFeatures()).hasSize(limit).containsExactly(result.getFeatures());

        assertThat(trends.getPassedScenarios()).hasSize(limit).containsExactly(result.getPassedScenarios());
        assertThat(trends.getFailedScenarios()).hasSize(limit).containsExactly(result.getFailedScenarios());
        assertThat(trends.getTotalScenarios()).hasSize(limit).containsExactly(result.getScenarios());

        assertThat(trends.getPassedSteps()).hasSize(limit).containsExactly(result.getPassedSteps());
        assertThat(trends.getFailedSteps()).hasSize(limit).containsExactly(result.getFailedSteps());
        assertThat(trends.getPendingSteps()).hasSize(limit).containsExactly(result.getPendingSteps());
        assertThat(trends.getSkippedSteps()).hasSize(limit).containsExactly(result.getSkippedSteps());
        assertThat(trends.getUndefinedSteps()).hasSize(limit).containsExactly(result.getUndefinedSteps());
        assertThat(trends.getTotalSteps()).hasSize(limit).containsExactly(result.getSteps());

        assertThat(trends.getDurations()).hasSize(limit).containsExactly(result.getDuration());
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
    public void copyLastElements_OnBigLimit_ReturnsPassedLongArray() {

        // given
        final long[] array = new long[]{3, 4, 5, 6, 7, 8};
        final int limit = array.length + 1;

        // when
        long[] limitedArray = Deencapsulation.invoke(Trends.class, "copyLastElements", array, limit);

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

    @Test
    public void applyPatchForFeatures_OnFailedGreaterThanTotal_ChangesTotalFeatureAndFailed() {

        // given
        final int totalFeatures = 1000;
        final int failedFeatures = totalFeatures + 1;
        Trends trends = new Trends();
        Reportable result = new ReportableBuilder(0, failedFeatures, totalFeatures, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3206126182398L);
        trends.addBuild("buildNumber", result);

        // when
        Deencapsulation.invoke(trends, "applyPatchForFeatures");

        // then
        assertThat(trends.getTotalFeatures()[0]).isGreaterThan(trends.getFailedFeatures()[0]);
        // check if the values were reversed
        assertThat(trends.getTotalFeatures()).containsExactly(failedFeatures);
        assertThat(trends.getFailedFeatures()).containsExactly(totalFeatures);
    }
}