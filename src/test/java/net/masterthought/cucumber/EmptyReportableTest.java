package net.masterthought.cucumber;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
class EmptyReportableTest {

    @Test
    void allMethods_ReturnsDefaultValues() {

        // given
        EmptyReportable reportable = new EmptyReportable();

        // then
        assertThat(reportable.getName()).isNull();
        assertThat(reportable.getFeatures()).isZero();
        assertThat(reportable.getPassedFeatures()).isZero();
        assertThat(reportable.getFailedFeatures()).isZero();
        assertThat(reportable.getScenarios()).isZero();
        assertThat(reportable.getPassedScenarios()).isZero();
        assertThat(reportable.getFailedScenarios()).isZero();
        assertThat(reportable.getSteps()).isZero();
        assertThat(reportable.getPassedSteps()).isZero();
        assertThat(reportable.getFailedSteps()).isZero();
        assertThat(reportable.getSkippedSteps()).isZero();
        assertThat(reportable.getUndefinedSteps()).isZero();
        assertThat(reportable.getPendingSteps()).isZero();
        assertThat(reportable.getUndefinedSteps()).isZero();
        assertThat(reportable.getDuration()).isZero();
        assertThat(reportable.getFormattedDuration()).isNull();
        assertThat(reportable.getStatus()).isNull();
    }
}
