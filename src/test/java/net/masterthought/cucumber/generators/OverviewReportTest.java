package net.masterthought.cucumber.generators;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.commons.lang.NotImplementedException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import net.masterthought.cucumber.json.support.Status;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class OverviewReportTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void incFeaturesFor_AddsFeatures() {

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
    public void incScenarioFor_AddsScenario() {

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
    public void getFeatures_ReturnsFeaturesCount() {

        // given
        OverviewReport report = buildSampleReport();

        // when
        int featuresSize = report.getFeatures();

        assertThat(featuresSize).isEqualTo(1);
    }

    @Test
    public void getPassedFeatures_ReturnsPassedFeaturesCount() {

        // given
        OverviewReport report = buildSampleReport();

        // when
        int featuresSize = report.getPassedFeatures();

        assertThat(featuresSize).isEqualTo(0);
    }


    @Test
    public void getFailedFeatures_ReturnsFAiledFeaturesCount() {

        // given
        OverviewReport report = buildSampleReport();

        // when
        int featuresSize = report.getFailedFeatures();

        assertThat(featuresSize).isEqualTo(1);
    }


    @Test
    public void getScenarios_ReturnsNumberOfScenarios() {

        // given
        OverviewReport report = buildSampleReport();

        // then
        assertThat(report.getScenarios()).isEqualTo(2);
    }

    @Test
    public void getXXXScenarios_ReturnsNumberOfScenariosForStatus() {

        // given
        OverviewReport report = buildSampleReport();

        // then
        assertThat(report.getPassedScenarios()).isEqualTo(1);
        assertThat(report.getFailedScenarios()).isZero();
    }

    @Test
    public void incStepsFor_AddsScenario() {

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
    public void getSteps_ReturnsNumberOfSteps() {

        // given
        OverviewReport report = buildSampleReport();

        // then
        assertThat(report.getSteps()).isEqualTo(2);
    }

    @Test
    public void getXXXSteps_ReturnsNumberOfStepsForStatus() {

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
    public void incDuration_AddsDuration() {

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
    public void getDuration_ReturnsDuration() {

        // given
        OverviewReport report = buildSampleReport();

        // then
        assertThat(report.getDuration()).isEqualTo(1234567L);
    }

    @Test
    public void getFormattedDuration_ReturnsFormattedDuration() {

        // given
        OverviewReport report = buildSampleReport();

        // then
        assertThat(report.getFormattedDuration()).isEqualTo("001ms");
    }

    @Test
    public void getDeviceName_ThrowsException() {

        // given
        OverviewReport report = buildSampleReport();

        // then
        thrown.expect(NotImplementedException.class);
        report.getDeviceName();
    }

    @Test
    public void getName_ThrowsException() {

        // given
        OverviewReport report = buildSampleReport();

        // then
        thrown.expect(NotImplementedException.class);
        report.getName();
    }

    @Test
    public void getStatus_ThrowsException() {

        // given
        OverviewReport report = buildSampleReport();

        // then
        thrown.expect(NotImplementedException.class);
        report.getStatus();
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
