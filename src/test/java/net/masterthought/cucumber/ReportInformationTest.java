package net.masterthought.cucumber;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.isA;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import net.masterthought.cucumber.json.Feature;
import net.masterthought.cucumber.json.support.Status;
import net.masterthought.cucumber.json.support.TagObject;

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
    public void shouldListAllTags() {
        assertThat(reportResult.getAllTags().get(0), isA(TagObject.class));
    }

    @Test
    public void shouldReturnTotalNumberOfScenarios() {
        assertThat(reportResult.getFeatureReport().getScenarios(), is(10));
    }

    @Test
    public void shouldReturnTotalNumberOfSteps() {
        assertThat(reportResult.getFeatureReport().getSteps(), is(98));
    }

    @Test
    public void shouldReturnTotalNumberPassingSteps() {
        assertThat(reportResult.getFeatureReport().getPassedSteps(), is(90));
    }

    @Test
    public void shouldReturnTotalNumberFailingSteps() {
        assertThat(reportResult.getFeatureReport().getFailedSteps(), is(2));
    }

    @Test
    public void shouldReturnTotalNumberSkippedSteps() {
        assertThat(reportResult.getFeatureReport().getSkippedSteps(), is(6));
    }

    @Test
    public void shouldReturnTotalNumberPendingSteps() {
        assertThat(reportResult.getFeatureReport().getPendingSteps(), is(0));
    }

    @Test
    public void shouldReturnTotalNumberMissingSteps() {
        assertThat(reportResult.getFeatureReport().getMissingSteps(), is(0));
    }

    @Test
    public void shouldReturnTotalDurationAsString() {
        assertThat(reportResult.getFeatureReport().getFormattedDurations(), is("236 ms"));
    }

    @Test
    public void shouldReturnTimeStamp() {
        assertThat(reportResult.getBuildTime(), isA(String.class));
    }

    @Test
    public void shouldReturnTagReportStatusColour() {
        assertThat(reportResult.getAllTags().get(0).getStatus(), is(Status.PASSED));
    }

    @Test
    public void shouldReturnTotalTags() {
        assertThat(reportResult.getAllTags().size(), is(5));
    }

    @Test
    public void shouldReturnTotalTagScenarios() {
        assertThat(reportResult.getTagReport().getScenarios(), is(21));
    }

    @Test
    public void shouldReturnTotalPassingTagScenarios() {
        assertThat(reportResult.getTagReport().getPassedScenarios(), is(21));
    }

    @Test
    public void shouldReturnTotalFailingTagScenarios() {
        assertThat(reportResult.getTagReport().getFailedScenarios(), is(0));
    }

    @Test
    public void shouldReturnTotalTagSteps() {
        assertThat(reportResult.getTagReport().getSteps(), is(147));
    }

    @Test
    public void shouldReturnTotalTagPasses() {
        assertThat(reportResult.getTagReport().getPassedSteps(), is(147));
    }

    @Test
    public void shouldReturnTotalTagFails() {
        assertThat(reportResult.getTagReport().getFailedSteps(), is(0));
    }

    @Test
    public void shouldReturnTotalTagSkipped() {
        assertThat(reportResult.getTagReport().getSkippedSteps(), is(0));
    }

    @Test
    public void shouldReturnTotalTagPending() {
        assertThat(reportResult.getTagReport().getPendingSteps(), is(0));
    }

    @Test
    public void shouldReturnTotalScenariosPassed() {
        assertThat(reportResult.getFeatureReport().getPassedScenarios(), is(8));
    }

    @Test
    public void shouldReturnTotalScenariosFailed() {
        assertThat(reportResult.getFeatureReport().getFailedScenarios(), is(2));
    }
}
