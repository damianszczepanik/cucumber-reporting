package net.masterthought.cucumber;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import net.masterthought.cucumber.json.Feature;
import net.masterthought.cucumber.json.support.Status;

public class ReportInformationTest {

    private final Configuration configuration = new Configuration(new File(""), "testProject");

    private ReportResult reportResult;

    @Before
    public void setUpReportInformation() throws IOException, URISyntaxException {
        configuration.setStatusFlags(true, false, true, false);

        List<String> jsonReports = new ArrayList<>();
        //will work iff the resources are not jarred up, otherwise use IOUtils to copy to a temp file.
        jsonReports.add(new File(ReportInformationTest.class.getClassLoader().getResource("net/masterthought/cucumber/project1.json").toURI()).getAbsolutePath());
        jsonReports.add(new File(ReportInformationTest.class.getClassLoader().getResource("net/masterthought/cucumber/project2.json").toURI()).getAbsolutePath());
        List<Feature> features = new ReportParser(configuration).parseJsonResults(jsonReports);
        reportResult = new ReportResult(features);
    }

    @Test
    public void shouldReturnTotalNumberOfScenarios() {
        assertThat(reportResult.getFeatureReport().getScenarios()).isEqualTo(10);
    }

    @Test
    public void shouldReturnTotalNumberOfSteps() {
        assertThat(reportResult.getFeatureReport().getSteps()).isEqualTo(98);
    }

    @Test
    public void shouldReturnTotalNumberPassingSteps() {
        assertThat(reportResult.getFeatureReport().getPassedSteps()).isEqualTo(90);
    }

    @Test
    public void shouldReturnTotalNumberFailingSteps() {
        assertThat(reportResult.getFeatureReport().getFailedSteps()).isEqualTo(2);
    }

    @Test
    public void shouldReturnTotalNumberSkippedSteps() {
        assertThat(reportResult.getFeatureReport().getSkippedSteps()).isEqualTo(6);
    }

    @Test
    public void shouldReturnTotalNumberPendingSteps() {
        assertThat(reportResult.getFeatureReport().getPendingSteps()).isEqualTo(0);
    }

    @Test
    public void shouldReturnTotalNumberMissingSteps() {
        assertThat(reportResult.getFeatureReport().getMissingSteps()).isEqualTo(0);
    }

    @Test
    public void shouldReturnTotalDurationAsString() {
        assertThat(reportResult.getFeatureReport().getFormattedDurations()).isEqualTo("236ms");
    }

    @Test
    public void shouldReturnTagReportStatusColour() {
        assertThat(reportResult.getAllTags().get(0).getStatus()).isEqualTo(Status.PASSED);
    }

    @Test
    public void shouldReturnTotalTags() {
        assertThat(reportResult.getAllTags().size()).isEqualTo(5);
    }

    @Test
    public void shouldReturnTotalTagScenarios() {
        assertThat(reportResult.getTagReport().getScenarios()).isEqualTo(21);
    }

    @Test
    public void shouldReturnTotalPassingTagScenarios() {
        assertThat(reportResult.getTagReport().getPassedScenarios()).isEqualTo(21);
    }

    @Test
    public void shouldReturnTotalFailingTagScenarios() {
        assertThat(reportResult.getTagReport().getFailedScenarios()).isEqualTo(0);
    }

    @Test
    public void shouldReturnTotalTagSteps() {
        assertThat(reportResult.getTagReport().getSteps()).isEqualTo(147);
    }

    @Test
    public void shouldReturnTotalTagPasses() {
        assertThat(reportResult.getTagReport().getPassedSteps()).isEqualTo(147);
    }

    @Test
    public void shouldReturnTotalTagFails() {
        assertThat(reportResult.getTagReport().getFailedSteps()).isEqualTo(0);
    }

    @Test
    public void shouldReturnTotalTagSkipped() {
        assertThat(reportResult.getTagReport().getSkippedSteps()).isEqualTo(0);
    }

    @Test
    public void shouldReturnTotalTagPending() {
        assertThat(reportResult.getTagReport().getPendingSteps()).isEqualTo(0);
    }

    @Test
    public void shouldReturnTotalScenariosPassed() {
        assertThat(reportResult.getFeatureReport().getPassedScenarios()).isEqualTo(8);
    }

    @Test
    public void shouldReturnTotalScenariosFailed() {
        assertThat(reportResult.getFeatureReport().getFailedScenarios()).isEqualTo(2);
    }
}
