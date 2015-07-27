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

    /*
    project1.json and project2.json contains same entries. So, multiplying by 2 to get the count

    Scenarios:
    @fast:
    3 *2 = 6
    @checkout:
    3 *2 = 6
    @super:
    3 + 1 (feature level tag) = 4 *2 = 8

    Total: 20

    json file contains 7 steps per scenario
    Steps:
    @fast:
    6 * 7 = 42
    @checkout:
    6 * 7 = 42
    @super:
    8 * 7 = 56

    Total: 140


    Background Scenario:
    Activate Credit Card - appears 4 times in each json file, steps : 3

    Scenarios:
    4 *2 = 8

    Steps:
    8 * 3 (steps per scenario) = 24


    Total Scenarios (background + tagged) : 20 + 8 = 28
    Total Steps (background + tagged) : 140 + 24 = 164

     */

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
        reportInformation = new ReportInformation(reportParser.getFeatures(), true);
    }

    @Test
    public void shouldDisplayArtifacts() throws Exception {
        ConfigurationOptions configuration = ConfigurationOptions.instance();
        configuration.setArtifactsEnabled(true);
        String config = "Account has sufficient funds again~the account balance is 300~balance~account_balance.txt~xml";
        ArtifactProcessor artifactProcessor = new ArtifactProcessor(config);
        Map<String, Artifact> map = artifactProcessor.process();
        configuration.setArtifactConfiguration(map);
        reportInformation = new ReportInformation(reportParser.getFeatures(), true);
        assertThat(reportInformation.getFeatures().get(2).getElements().get(7).getSteps().get(0).getName(), is("<div class=\"passed\"><span class=\"step-keyword\">Given  </span><span class=\"step-name\">the account &lt;div style=&quot;display:none;&quot;&gt;&lt;textarea id=&quot;Account_has_sufficient_funds_againthe_account_balance_is_300&quot; class=&quot;brush: xml;&quot;&gt;&lt;/textarea&gt;&lt;/div&gt;&lt;a onclick=&quot;applyArtifact('Account_has_sufficient_funds_againthe_account_balance_is_300','account_balance.txt')&quot; href=&quot;#&quot;&gt;balance&lt;/a&gt; is 300</span><span class=\"step-duration\">000ms</span></div>"));
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
        assertThat(reportInformation.getTotalTags(), is(3));
    }

    @Test
    public void shouldReturnTotalTagScenarios() {
        assertThat(reportInformation.getTotalTagScenarios(), is(28));
    }

    @Test
    public void shouldReturnTotalPassingTagScenarios() {
        assertThat(reportInformation.getTotalTagScenariosPassed(), is(28));
    }

    @Test
    public void shouldReturnTotalFailingTagScenarios() {
        assertThat(reportInformation.getTotalTagScenariosFailed(), is(0));
    }

    @Test
    public void shouldReturnTotalTagSteps() {
        assertThat(reportInformation.getTotalTagSteps(), is(164));
    }

    @Test
    public void shouldReturnTotalTagPasses() {
        assertThat(reportInformation.getTotalTagPasses(), is(164));
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
