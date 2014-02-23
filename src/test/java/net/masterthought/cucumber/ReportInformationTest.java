package net.masterthought.cucumber;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.internal.matchers.IsCollectionContaining.hasItem;
import static org.junit.internal.matchers.StringContains.containsString;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.masterthought.cucumber.json.Artifact;
import net.masterthought.cucumber.json.Feature;

import org.junit.Before;
import org.junit.Test;

public class ReportInformationTest {

    ReportInformation reportInformation;
    ReportParser reportParser;

    @Before
    public void setUpReportInformation() throws IOException, URISyntaxException {
        ConfigurationOptions.setSkippedFailsBuild(false);
        ConfigurationOptions.setUndefinedFailsBuild(false);
        List<String> jsonReports = new ArrayList<String>();
        //will work iff the resources are not jarred up, otherwise use IOUtils to copy to a temp file.
        jsonReports.add(new File(ReportInformationTest.class.getClassLoader().getResource("net/masterthought/cucumber/project1.json").toURI()).getAbsolutePath());
        jsonReports.add(new File(ReportInformationTest.class.getClassLoader().getResource("net/masterthought/cucumber/project2.json").toURI()).getAbsolutePath());
        reportParser = new ReportParser(jsonReports);
        reportInformation = new ReportInformation(reportParser.getFeatures());
    }

    @Test
    public void shouldDisplayArtifacts() throws Exception {
        ConfigurationOptions.setArtifactsEnabled(true);
        String configuration = "Account has sufficient funds again~the account balance is 300~balance~account_balance.txt~xml";
        ArtifactProcessor artifactProcessor = new ArtifactProcessor(configuration);
        Map<String, Artifact> map = artifactProcessor.process();
        ConfigurationOptions.setArtifactConfiguration(map);
        reportInformation = new ReportInformation(reportParser.getFeatures());
        assertThat(reportInformation.getFeatures().get(2).getElements().get(7).getSteps().get(0).getName(), is("<div class=\"passed\"><span class=\"step-keyword\">Given  </span><span class=\"step-name\">the account <div style=\"display:none;\"><textarea id=\"Account_has_sufficient_funds_againthe_account_balance_is_300\" class=\"brush: xml;\"></textarea></div><a onclick=\"applyArtifact('Account_has_sufficient_funds_againthe_account_balance_is_300','account_balance.txt')\" href=\"#\">balance</a> is 300</span><span class=\"step-duration\">0 ms</span></div>"));
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
        assertThat(reportInformation.getProjectFeatureMap().keySet(), hasItem(containsString("project1.json")));
        assertThat(reportInformation.getProjectFeatureMap().entrySet().iterator().next().getValue().get(0), is(Feature.class));
    }

    @Test
    public void shouldReturnTotalNumberOfScenarios() {
        assertThat(reportInformation.getTotalNumberOfScenarios(), is(10));
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
        assertThat(reportInformation.getTotalTagScenarios(), is(10));
    }

    @Test
    public void shouldReturnTotalPassingTagScenarios() {
        assertThat(reportInformation.getTotalPassingTagScenarios(), is(10));
    }

    @Test
    public void shouldReturnTotalFailingTagScenarios() {
        assertThat(reportInformation.getTotalFailingTagScenarios(), is(0));
    }

    @Test
    public void shouldReturnTotalTagSteps() {
        assertThat(reportInformation.getTotalTagSteps(), is(70));
    }

    @Test
    public void shouldReturnTotalTagPasses() {
        assertThat(reportInformation.getTotalTagPasses(), is(70));
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
