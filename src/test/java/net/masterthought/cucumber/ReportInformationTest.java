package net.masterthought.cucumber;

import net.masterthought.cucumber.json.Artifact;
import net.masterthought.cucumber.json.Feature;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ReportInformationTest {

    ReportInformation reportInformation;
    ReportParser reportParser;

    @Before
    public void setUpReportInformation() throws IOException {
        ConfigurationOptions.setSkippedFailsBuild(false);
        ConfigurationOptions.setUndefinedFailsBuild(false);
        List<String> jsonReports = new ArrayList<String>();
        jsonReports.add("src/test/resources/net/masterthought/cucumber/project1.json");
        jsonReports.add("src/test/resources/net/masterthought/cucumber/project2.json");
        reportParser = new ReportParser(jsonReports);
        reportInformation = new ReportInformation(reportParser.getFeatures());
    }

    @Test
    public void shouldDisplayArtifacts() throws Exception {
        ConfigurationOptions.setArtifactsEnabled(true);
        String configuration = "Account has sufficient funds again~the account balance is 300~account~evidence/account_balance.txt";
        ArtifactProcessor artifactProcessor = new ArtifactProcessor(configuration);
        Map<String, Artifact> map = artifactProcessor.process();
        ConfigurationOptions.setArtifactConfiguration(map);

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
        assertThat(reportInformation.getProjectFeatureMap().keySet().iterator().next(), is("src/test/resources/net/masterthought/cucumber/project2.json"));
        assertThat(reportInformation.getProjectFeatureMap().entrySet().iterator().next().getValue().get(0), is(Feature.class));
    }

    @Test
    public void shouldReturnTotalNumberOfScenarios() {
        assertThat(reportInformation.getTotalNumberOfScenarios(), is(18));
    }

    @Test
    public void shouldReturnTotalNumberOfFeatures() {
        assertThat(reportInformation.getTotalNumberOfFeatures(), is(4));
    }

    @Test
    public void shouldReturnTotalNumberOfSteps() {
        assertThat(reportInformation.getTotalNumberOfSteps(), is(98));
    }

    @Test
    public void shouldReturnTotalNumberPassingSteps() {
        assertThat(reportInformation.getTotalNumberPassingSteps(), is(90));
    }

    @Test
    public void shouldReturnTotalNumberFailingSteps() {
        assertThat(reportInformation.getTotalNumberFailingSteps(), is(2));
    }

    @Test
    public void shouldReturnTotalNumberSkippedSteps() {
        assertThat(reportInformation.getTotalNumberSkippedSteps(), is(6));
    }

    @Test
    public void shouldReturnTotalNumberPendingSteps() {
        assertThat(reportInformation.getTotalNumberPendingSteps(), is(0));
    }

    @Test
    public void shouldReturnTotalNumberMissingSteps() {
        assertThat(reportInformation.getTotalNumberMissingSteps(), is(0));
    }

    @Test
    public void shouldReturnTotalDuration() {
        assertThat(reportInformation.getTotalDuration(), is(236050000L));
    }

    @Test
    public void shouldReturnTotalDurationAsString() {
        assertThat(reportInformation.getTotalDurationAsString(), is("236 ms"));
    }

    @Test
    public void shouldReturnTimeStamp() {
        assertThat(reportInformation.timeStamp(), is(String.class));
    }

    @Test
    public void shouldReturnReportStatusColour() {
        assertThat(reportInformation.getReportStatusColour(reportInformation.getFeatures().get(0)), is("#C5D88A"));
    }

    @Test
    public void shouldReturnTagReportStatusColour() {
        assertThat(reportInformation.getTagReportStatusColour(reportInformation.tagMap.get(0)), is("#C5D88A"));
    }

    @Test
    public void shouldReturnTotalTags() {
        assertThat(reportInformation.getTotalTags(), is(3));
    }

    @Test
    public void shouldReturnTotalTagScenarios() {
        assertThat(reportInformation.getTotalTagScenarios(), is(24));
    }

    @Test
    public void shouldReturnTotalTagSteps() {
        assertThat(reportInformation.getTotalTagSteps(), is(696));
    }

    @Test
    public void shouldReturnTotalTagPasses() {
        assertThat(reportInformation.getTotalTagPasses(), is(120));
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
        assertThat(reportInformation.getTotalTagDuration(), is("340 ms"));
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
