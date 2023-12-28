package net.masterthought.cucumber.generators;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import net.masterthought.cucumber.json.support.Status;
import org.apache.commons.lang3.NotImplementedException;
import org.junit.jupiter.api.Test;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
class OverviewReportTest {

    @Test
    void incFeaturesFor_AddsFeatures() {

        // given
        OverviewReport refReport = buildSampleReport();
        OverviewReport report = buildSampleReport();

        // then
        report.incFeaturesFor(Status.FAILED);

        // then
        assertThat(report.getFeatures()).isEqualTo(refReport.getFeatures() + 1);
        assertThat(report.getPassedFeatures()).isEqualTo(refReport.getPassedFeatures());
        assertThat(report.getFailedFeatures()).isEqualTo(refReport.getFailedFeatures() + 1);
    }

    @Test
    void incScenarioFor_AddsScenario() {

        // given
        OverviewReport refReport = buildSampleReport();
        OverviewReport report = buildSampleReport();

        // then
        report.incScenarioFor(Status.FAILED);

        // then
        assertThat(report.getScenarios()).isEqualTo(refReport.getScenarios() + 1);
        assertThat(report.getPassedScenarios()).isEqualTo(refReport.getPassedScenarios());
        assertThat(report.getFailedScenarios()).isEqualTo(refReport.getFailedScenarios() + 1);
    }

    @Test
    void getFeatures_ReturnsFeaturesCount() {

        // given
        OverviewReport report = buildSampleReport();

        // when
        int featuresSize = report.getFeatures();

        assertThat(featuresSize).isEqualTo(1);
    }

    @Test
    void getPassedFeatures_ReturnsPassedFeaturesCount() {

        // given
        OverviewReport report = buildSampleReport();

        // when
        int featuresSize = report.getPassedFeatures();

        assertThat(featuresSize).isZero();
    }


    @Test
    void getFailedFeatures_ReturnsFAiledFeaturesCount() {

        // given
        OverviewReport report = buildSampleReport();

        // when
        int featuresSize = report.getFailedFeatures();

        assertThat(featuresSize).isEqualTo(1);
    }


    @Test
    void getScenarios_ReturnsNumberOfScenarios() {

        // given
        OverviewReport report = buildSampleReport();

        // then
        assertThat(report.getScenarios()).isEqualTo(2);
    }

    @Test
    void getXXXScenarios_ReturnsNumberOfScenariosForStatus() {

        // given
        OverviewReport report = buildSampleReport();

        // then
        assertThat(report.getPassedScenarios()).isEqualTo(1);
        assertThat(report.getFailedScenarios()).isZero();
    }

    @Test
    void incStepsFor_AddsScenario() {

        // given
        OverviewReport refReport = buildSampleReport();
        OverviewReport report = buildSampleReport();

        // then
        report.incStepsFor(Status.FAILED);

        // then
        assertThat(report.getSteps()).isEqualTo(refReport.getSteps() + 1);
        assertThat(report.getPassedSteps()).isEqualTo(refReport.getPassedSteps());
        assertThat(report.getFailedSteps()).isEqualTo(refReport.getFailedSteps() + 1);
    }

    @Test
    void getSteps_ReturnsNumberOfSteps() {

        // given
        OverviewReport report = buildSampleReport();

        // then
        assertThat(report.getSteps()).isEqualTo(2);
    }

    @Test
    void getXXXSteps_ReturnsNumberOfStepsForStatus() {

        // given
        OverviewReport report = buildSampleReport();

        // then
        assertThat(report.getPassedSteps()).isZero();
        assertThat(report.getFailedSteps()).isEqualTo(1);
        assertThat(report.getSkippedSteps()).isZero();
        assertThat(report.getPendingSteps()).isEqualTo(1);
        assertThat(report.getUndefinedSteps()).isZero();
    }

    @Test
    void incDuration_AddsDuration() {

        // given
        long offset = 5555555;
        OverviewReport RefReport = buildSampleReport();
        OverviewReport report = buildSampleReport();

        // when
        report.incDurationBy(offset);

        // then
        assertThat(report.getDuration()).isEqualTo(RefReport.getDuration() + offset);
    }

    @Test
    void getDuration_ReturnsDuration() {

        // given
        OverviewReport report = buildSampleReport();

        // then
        assertThat(report.getDuration()).isEqualTo(1234567L);
    }

    @Test
    void getFormattedDuration_ReturnsFormattedDuration() {

        // given
        OverviewReport report = buildSampleReport();

        // then
        assertThat(report.getFormattedDuration()).isEqualTo("0.001");
    }

    @Test
    void getName_ThrowsException() {

        // given
        OverviewReport report = buildSampleReport();

        // when & then
        assertThatThrownBy(() -> report.getName()).
                isInstanceOf(NotImplementedException.class);
    }

    @Test
    void getStatus_ThrowsException() {

        // given
        OverviewReport report = buildSampleReport();

        // when & then
        assertThatThrownBy(() -> report.getStatus())
                .isInstanceOf(NotImplementedException.class);
    }

    private static OverviewReport buildSampleReport() {
        OverviewReport report = new OverviewReport();
        report.incDurationBy(1234567L);

        report.incFeaturesFor(Status.FAILED);

        report.incScenarioFor(Status.PASSED);
        report.incScenarioFor(Status.UNDEFINED);

        report.incStepsFor(Status.FAILED);
        report.incStepsFor(Status.PENDING);

        return report;
    }
}
