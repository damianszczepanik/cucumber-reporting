package net.masterthought.cucumber;

import static net.masterthought.cucumber.FileReaderUtil.getAbsolutePathFromResource;

import net.masterthought.cucumber.json.Feature;
import net.masterthought.cucumber.util.Util;
import org.junit.Before;
import org.junit.Test;
import org.junit.internal.matchers.StringContains;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class FeatureTest {

    ReportParser reportParser;
    Feature passingFeature;
    Feature failingFeature;

    @Before
    public void setUpJsonReports() throws IOException {
        List<String> jsonReports = new ArrayList<String>();
        jsonReports.add(getAbsolutePathFromResource("net/masterthought/cucumber/project1.json"));
        reportParser = new ReportParser(jsonReports);
        passingFeature = reportParser.getFeatures().entrySet().iterator().next().getValue().get(0);
        failingFeature = reportParser.getFeatures().entrySet().iterator().next().getValue().get(1);
        passingFeature.processSteps();
        failingFeature.processSteps();
    }

    @Test
    public void shouldReturnManagedFileName() {
        assertThat(passingFeature.getFileName(), is("masterthought-example-ATM.feature.html"));
    }

    @Test
    public void shouldGetDescription() {
        assertThat(passingFeature.getDescription(), is("<div class=\"feature-description\">As a Account Holder<br/><span class=\"feature-action\">I want to</span> withdraw cash from an ATM<br/><span class=\"feature-value\">So that</span> I can get money when the bank is closed</div>"
        ));
    }

    @Test
    public void shouldKnowIfTagsExists() {
        assertThat(passingFeature.hasTags(), is(true));
    }

    @Test
    public void shouldListTheTags() {
        List<String> expectedList = new ArrayList<String>();
        expectedList.add("@super");
        assertThat(passingFeature.getTagList(), is(expectedList));
    }

    @Test
    public void shouldListTheTagsAsHtml() {
        assertThat(passingFeature.getTags(), is("<div class=\"feature-tags\">@super</div>"));
    }

    @Test
    public void shouldGetStatus() {
        assertThat(passingFeature.getStatus(), is(Util.Status.PASSED));
    }

    @Test
    public void shouldReturnName() {
        assertThat(passingFeature.getName(), is("<div class=\"passed\"><div class=\"feature-line\"><span class=\"feature-keyword\">Feature:</span> Account Holder withdraws cash</div></div>"));
    }

    @Test
    public void shouldReturnRawName() {
        assertThat(passingFeature.getRawName(), is("Account Holder withdraws cash"));
    }

    @Test
    public void shouldReturnRawStatus() {
        assertThat(passingFeature.getRawStatus(), is("passed"));
    }

    @Test
    public void shouldGetNumberOfSteps() {
        assertThat(passingFeature.getNumberOfSteps(), is(40));
    }

    @Test
    public void shouldGetNumberOfPassingSteps() {
        assertThat(passingFeature.getNumberOfPasses(), is(40));
        assertThat(failingFeature.getNumberOfPasses(), is(5));

    }

    @Test
    public void shouldGetNumberOfFailingSteps() {
        assertThat(passingFeature.getNumberOfFailures(), is(0));
        assertThat(failingFeature.getNumberOfFailures(), is(1));
    }

    @Test
    public void shouldGetNumberOfSkippedSteps() {
        assertThat(passingFeature.getNumberOfSkipped(), is(0));
        assertThat(failingFeature.getNumberOfSkipped(), is(3));
    }

    @Test
    public void shouldGetNumberOfPendingSteps() {
        assertThat(passingFeature.getNumberOfPending(), is(0));
    }

    @Test
    public void shouldGetNumberOfMissingSteps() {
        assertThat(passingFeature.getNumberOfMissing(), is(0));
    }

    @Test
    public void shouldGetDurationOfSteps() {
        assertThat(passingFeature.getDurationOfSteps(), StringContains.containsString("ms"));
    }

    @Test
    public void shouldGetNumberOScenarios() {
        assertThat(passingFeature.getNumberOfScenarios(), is(4));
    }

    @Test
    public void shouldProcessFeatureWhenNoScenarios() throws IOException {
        List<String> jsonReports = new ArrayList<String>();
        jsonReports.add(getAbsolutePathFromResource("net/masterthought/cucumber/noscenario.json"));
        ReportParser reportParser = new ReportParser(jsonReports);
        Feature feature = reportParser.getFeatures().entrySet().iterator().next().getValue().get(0);
        feature.processSteps();
    }

    @Test
    public void shouldGetNumberOfPassingScenarios() {
        assertThat(passingFeature.getNumberOfScenariosPassed(), is(4));
    }

    @Test
    public void shouldGetNumberOfFailingScenarios() {
        assertThat(failingFeature.getNumberOfScenariosFailed(), is(1));
    }
}
