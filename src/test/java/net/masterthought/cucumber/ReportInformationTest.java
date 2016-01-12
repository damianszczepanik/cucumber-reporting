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

    private final Configuration configuration = new Configuration();

    private ReportInformation reportInformation;

    @Before
    public void setUpReportInformation() throws IOException, URISyntaxException {
        configuration.setStatusFlags(true, false, true, false);

        List<String> jsonReports = new ArrayList<>();
        //will work iff the resources are not jarred up, otherwise use IOUtils to copy to a temp file.
        jsonReports.add(new File(ReportInformationTest.class.getClassLoader().getResource("net/masterthought/cucumber/project1.json").toURI()).getAbsolutePath());
        jsonReports.add(new File(ReportInformationTest.class.getClassLoader().getResource("net/masterthought/cucumber/project2.json").toURI()).getAbsolutePath());
        List<Feature> features = new ReportParser(configuration).parseJsonResults(jsonReports);
        reportInformation = new ReportInformation(features);
    }

    @Test
    public void shouldListAllFeatures() throws IOException {
        assertThat(reportInformation.getAllFeatures().get(0), isA(Feature.class));
    }

    @Test
    public void shouldListAllTags() {
        assertThat(reportInformation.getAllTags().get(0), isA(TagObject.class));
    }

    @Test
    public void shouldReturnTotalNumberOfScenarios() {
        assertThat(reportInformation.getAllScenarios().size(), is(10));
    }

    @Test
    public void shouldReturnTotalNumberOfSteps() {
        assertThat(reportInformation.getStepsCounter().size(), is(98));
    }

    @Test
    public void shouldReturnTotalNumberPassingSteps() {
        assertThat(reportInformation.getAllPassedSteps(), is(90));
    }

    @Test
    public void shouldReturnTotalNumberFailingSteps() {
        assertThat(reportInformation.getAllFailedSteps(), is(2));
    }

    @Test
    public void shouldReturnTotalNumberSkippedSteps() {
        assertThat(reportInformation.getAllSkippedSteps(), is(6));
    }

    @Test
    public void shouldReturnTotalNumberPendingSteps() {
        assertThat(reportInformation.getPendingStepsl(), is(0));
    }

    @Test
    public void shouldReturnTotalNumberMissingSteps() {
        assertThat(reportInformation.getTotalStepsMissing(), is(0));
    }

    @Test
    public void shouldReturnTotalDuration() {
        assertThat(reportInformation.getAllDurations(), is(236050000L));
    }

    @Test
    public void shouldReturnTotalDurationAsString() {
        assertThat(reportInformation.getAllDurationsAsString(), is("236ms"));
    }

    @Test
    public void shouldReturnTimeStamp() {
        assertThat(reportInformation.timeStamp(), isA(String.class));
    }

    @Test
    public void shouldReturnReportStatusColour() {
        assertThat(reportInformation.getAllFeatures().get(0).getStatus().color, is(Status.PASSED.color));
    }

    @Test
    public void shouldReturnTagReportStatusColour() {
        assertThat(reportInformation.getAllTags().get(0).getStatus().color, is(Status.PASSED.color));
    }

    @Test
    public void shouldReturnTotalTags() {
        assertThat(reportInformation.getAllTags().size(), is(5));
    }

    @Test
    public void shouldReturnTotalTagScenarios() {
        assertThat(reportInformation.getAllTagScenarios(), is(21));
    }

    @Test
    public void shouldReturnTotalPassingTagScenarios() {
        assertThat(reportInformation.getAllPassedTagScenarios(), is(21));
    }

    @Test
    public void shouldReturnTotalFailingTagScenarios() {
        assertThat(reportInformation.getAllFailedTagScenarios(), is(0));
    }

    @Test
    public void shouldReturnTotalTagSteps() {
        assertThat(reportInformation.getAllTagSteps(), is(147));
    }

    @Test
    public void shouldReturnTotalTagPasses() {
        assertThat(reportInformation.getAllPassesTags(), is(147));
    }

    @Test
    public void shouldReturnTotalTagFails() {
        assertThat(reportInformation.getAllFailsTags(), is(0));
    }

    @Test
    public void shouldReturnTotalTagSkipped() {
        assertThat(reportInformation.getAllSkippedTags(), is(0));
    }

    @Test
    public void shouldReturnTotalTagPending() {
        assertThat(reportInformation.getAllPendingTags(), is(0));
    }

    @Test
    public void shouldReturnTotalScenariosPassed() {
        assertThat(reportInformation.getAllPassedScenarios(), is(8));
    }

    @Test
    public void shouldReturnTotalScenariosFailed() {
        assertThat(reportInformation.getAllFailedScenarios(), is(2));
    }
}
