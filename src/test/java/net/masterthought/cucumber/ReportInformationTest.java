package net.masterthought.cucumber;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import net.masterthought.cucumber.json.Feature;
import net.masterthought.cucumber.json.support.TagObject;

public class ReportInformationTest {

    ReportInformation reportInformation;
    ReportParser reportParser;

    @Before
    public void setUpReportInformation() throws IOException, URISyntaxException {
        ConfigurationOptions configuration = ConfigurationOptions.instance();
        configuration.setSkippedFailsBuild(false);
        configuration.setUndefinedFailsBuild(false);
        List<String> jsonReports = new ArrayList<String>();
        //will work iff the resources are not jarred up, otherwise use IOUtils to copy to a temp file.
        jsonReports.add(new File(ReportInformationTest.class.getClassLoader().getResource("net/masterthought/cucumber/project1.json").toURI()).getAbsolutePath());
        jsonReports.add(new File(ReportInformationTest.class.getClassLoader().getResource("net/masterthought/cucumber/project2.json").toURI()).getAbsolutePath());
        reportParser = new ReportParser(jsonReports);
        reportInformation = new ReportInformation(reportParser.getFeatures());
    }

    @Test
    public void shouldListAllFeatures() throws IOException {
        assertThat(reportInformation.getFeatures().get(0), is(Feature.class));
    }

    @Test
    public void shouldListAllTags() {
        assertThat(reportInformation.getTags().get(0), is(TagObject.class));
    }

    @Test
    public void shouldListFeaturesInAMap() {
	//not really needed now -- have type safety with generics in object usage and would have failed had we not found the resource.
        assertThat(reportInformation.getFeatureMap().keySet(), hasItem(containsString("project1.json")));
        assertThat(reportInformation.getFeatureMap().entrySet().iterator().next().getValue().get(0), is(Feature.class));
    }

    @Test
    public void shouldReturnTotalNumberOfScenarios() {
        assertThat(reportInformation.getTotalScenarios(), is(10));
    }

    @Test
    public void shouldReturnTotalNumberOfFeatures() {
        assertThat(reportInformation.getTotalFeatures(), is(4));
    }

    @Test
    public void shouldReturnTotalNumberOfSteps() {
        assertThat(reportInformation.getTotalSteps(), is(98));
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
        assertThat(reportInformation.timeStamp(), is(String.class));
    }

    @Test
    public void shouldReturnReportStatusColour() {
        assertThat(reportInformation.getReportStatusColour(reportInformation.getFeatures().get(0)), is("#00CE00"));
    }

    @Test
    public void shouldReturnTagReportStatusColour() {
        assertThat(reportInformation.getTagReportStatusColour(reportInformation.getTags().get(0)), is("#00CE00"));
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
        assertThat(reportInformation.getTotalTagPasses(), is(140));
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
    public void shouldReturnTotalTagDuration() {
        assertThat(reportInformation.getTotalTagDuration(), containsString("ms"));
    }

    @Test
    public void shouldReturnTotalScenariosPassed() {
        assertThat(reportInformation.getTotalScenariosPassed(), is(8));
    }

    @Test
    public void shouldReturnTotalScenariosFailed() {
        assertThat(reportInformation.getTotalScenariosFailed(), is(2));
    }


}
