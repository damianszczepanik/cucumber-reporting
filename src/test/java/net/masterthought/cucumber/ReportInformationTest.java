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
    public void shouldListAllFeatures() throws IOException {
        assertThat(reportResult.getAllFeatures().get(0), isA(Feature.class));
    }

    @Test
    public void shouldListAllTags() {
        assertThat(reportResult.getAllTags().get(0), isA(TagObject.class));
    }

    @Test
    public void shouldReturnTotalNumberOfScenarios() {
        assertThat(reportResult.getAllScenarios().size(), is(10));
    }

    @Test
    public void shouldReturnTotalNumberOfSteps() {
        assertThat(reportResult.getStepsCounter().size(), is(98));
    }

    @Test
    public void shouldReturnTotalNumberPassingSteps() {
        assertThat(reportResult.getAllPassedSteps(), is(90));
    }

    @Test
    public void shouldReturnTotalNumberFailingSteps() {
        assertThat(reportResult.getAllFailedSteps(), is(2));
    }

    @Test
    public void shouldReturnTotalNumberSkippedSteps() {
        assertThat(reportResult.getAllSkippedSteps(), is(6));
    }

    @Test
    public void shouldReturnTotalNumberPendingSteps() {
        assertThat(reportResult.getPendingStepsl(), is(0));
    }

    @Test
    public void shouldReturnTotalNumberMissingSteps() {
        assertThat(reportResult.getTotalStepsMissing(), is(0));
    }

    @Test
    public void shouldReturnTotalDuration() {
        assertThat(reportResult.getAllDurations(), is(236050000L));
    }

    @Test
    public void shouldReturnTotalDurationAsString() {
        assertThat(reportResult.getAllDurationsAsString(), is("236ms"));
    }

    @Test
    public void shouldReturnTimeStamp() {
        assertThat(reportResult.timeStamp(), isA(String.class));
    }

    @Test
    public void shouldReturnReportStatusColour() {
        assertThat(reportResult.getAllFeatures().get(0).getStatus().color, is(Status.PASSED.color));
    }

    @Test
    public void shouldReturnTagReportStatusColour() {
        assertThat(reportResult.getAllTags().get(0).getStatus().color, is(Status.PASSED.color));
    }

    @Test
    public void shouldReturnTotalTags() {
        assertThat(reportResult.getAllTags().size(), is(5));
    }

    @Test
    public void shouldReturnTotalTagScenarios() {
        assertThat(reportResult.getAllTagScenarios(), is(21));
    }

    @Test
    public void shouldReturnTotalPassingTagScenarios() {
        assertThat(reportResult.getAllPassedTagScenarios(), is(21));
    }

    @Test
    public void shouldReturnTotalFailingTagScenarios() {
        assertThat(reportResult.getAllFailedTagScenarios(), is(0));
    }

    @Test
    public void shouldReturnTotalTagSteps() {
        assertThat(reportResult.getAllTagSteps(), is(147));
    }

    @Test
    public void shouldReturnTotalTagPasses() {
        assertThat(reportResult.getAllPassesTags(), is(147));
    }

    @Test
    public void shouldReturnTotalTagFails() {
        assertThat(reportResult.getAllFailsTags(), is(0));
    }

    @Test
    public void shouldReturnTotalTagSkipped() {
        assertThat(reportResult.getAllSkippedTags(), is(0));
    }

    @Test
    public void shouldReturnTotalTagPending() {
        assertThat(reportResult.getAllPendingTags(), is(0));
    }

    @Test
    public void shouldReturnTotalScenariosPassed() {
        assertThat(reportResult.getAllPassedScenarios(), is(8));
    }

    @Test
    public void shouldReturnTotalScenariosFailed() {
        assertThat(reportResult.getAllFailedScenarios(), is(2));
    }
}
