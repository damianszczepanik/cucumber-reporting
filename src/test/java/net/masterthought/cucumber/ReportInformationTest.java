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

    private ReportInformation reportInformation;

    @Before
    public void setUpReportInformation() throws IOException, URISyntaxException {
        ConfigurationOptions configuration = ConfigurationOptions.instance();
        configuration.setSkippedFailsBuild(false);
        configuration.setUndefinedFailsBuild(false);
        List<String> jsonReports = new ArrayList<String>();
        //will work iff the resources are not jarred up, otherwise use IOUtils to copy to a temp file.
        jsonReports.add(new File(ReportInformationTest.class.getClassLoader().getResource("net/masterthought/cucumber/project1.json").toURI()).getAbsolutePath());
        jsonReports.add(new File(ReportInformationTest.class.getClassLoader().getResource("net/masterthought/cucumber/project2.json").toURI()).getAbsolutePath());
        List<Feature> features = new ReportParser().parseJsonResults(jsonReports);
        reportInformation = new ReportInformation(features);
    }

    @Test
    public void shouldListAllFeatures() throws IOException {
        assertThat(reportInformation.getFeatures().get(0), isA(Feature.class));
    }

    @Test
    public void shouldListAllTags() {
        assertThat(reportInformation.getTags().get(0), isA(TagObject.class));
    }

    @Test
    public void shouldReturnTotalNumberOfScenarios() {
        assertThat(reportInformation.getTotalScenarios(), is(18));
    }

    @Test
    public void shouldReturnTotalNumberOfSteps() {
        assertThat(reportInformation.getStepsCounter().size(), is(98));
    }

    @Test
    public void shouldReturnTotalNumberPassingSteps() {
        assertThat(reportInformation.getTotalStepsPassed(), is(90));
    }

    @Test
    public void shouldReturnTotalNumberFailingSteps() {
        assertThat(reportInformation.getTotalStepsFailed(), is(2));
    }

    @Test
    public void shouldReturnTotalNumberSkippedSteps() {
        assertThat(reportInformation.getTotalStepsSkipped(), is(6));
    }

    @Test
    public void shouldReturnTotalNumberPendingSteps() {
        assertThat(reportInformation.getTotalStepsPending(), is(0));
    }

    @Test
    public void shouldReturnTotalNumberMissingSteps() {
        assertThat(reportInformation.getTotalStepsMissing(), is(0));
    }

    @Test
    public void shouldReturnTotalDuration() {
        assertThat(reportInformation.getTotalDuration(), is(236050000L));
    }

    @Test
    public void shouldReturnTotalDurationAsString() {
        assertThat(reportInformation.getTotalDurationAsString(), is("236ms"));
    }

    @Test
    public void shouldReturnTimeStamp() {
        assertThat(reportInformation.timeStamp(), isA(String.class));
    }

    @Test
    public void shouldReturnReportStatusColour() {
        assertThat(reportInformation.getFeatures().get(0).getStatus().color, is(Status.PASSED.color));
    }

    @Test
    public void shouldReturnTagReportStatusColour() {
        assertThat(reportInformation.getTags().get(0).getStatus().color, is(Status.PASSED.color));
    }

    @Test
    public void shouldReturnTotalTags() {
        assertThat(reportInformation.getTags().size(), is(3));
    }

    @Test
    public void shouldReturnTotalTagScenarios() {
        assertThat(reportInformation.getTotalTagScenarios(), is(20));
    }

    @Test
    public void shouldReturnTotalPassingTagScenarios() {
        assertThat(reportInformation.getTotalTagScenariosPassed(), is(20));
    }

    @Test
    public void shouldReturnTotalFailingTagScenarios() {
        assertThat(reportInformation.getTotalTagScenariosFailed(), is(0));
    }

    @Test
    public void shouldReturnTotalTagSteps() {
        assertThat(reportInformation.getTotalTagSteps(), is(140));
    }

    @Test
    public void shouldReturnTotalTagPasses() {
        assertThat(reportInformation.getTotalTagPasses(), is(20));
    }

    @Test
    public void shouldReturnTotalTagFails() {
        assertThat(reportInformation.getTotalTagFails(), is(0));
    }

    @Test
    public void shouldReturnTotalTagSkipped() {
        assertThat(reportInformation.getTotalTagSkipped(), is(0));
    }

    @Test
    public void shouldReturnTotalTagPending() {
        assertThat(reportInformation.getTotalTagPending(), is(0));
    }

    @Test
    public void shouldReturnTotalScenariosPassed() {
        assertThat(reportInformation.getTotalScenariosPassed(), is(16));
    }

    @Test
    public void shouldReturnTotalScenariosFailed() {
        assertThat(reportInformation.getTotalScenariosFailed(), is(2));
    }
}
