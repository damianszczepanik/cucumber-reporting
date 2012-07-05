package net.masterthought.cucumber;

import net.masterthought.cucumber.json.Feature;
import net.masterthought.cucumber.util.Util;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class FeatureTest {

    ReportParser reportParser;
    Feature feature;

    @Before
    public void setUpJsonReports() throws IOException {
        List<String> jsonReports = new ArrayList<String>();
        jsonReports.add("src/test/resources/net/masterthought/cucumber/project1.json");
        jsonReports.add("src/test/resources/net/masterthought/cucumber/project2.json");
        reportParser = new ReportParser(jsonReports);
        feature = reportParser.getFeatures().entrySet().iterator().next().getValue().get(0);
        feature.processSteps();
    }

//    @Test
//    public void shouldReturnListOfScenarios() throws IOException {
//        assertThat(feature.getElements()[0], is(Element.class));
//        assertThat(feature.getElements().length, is(4));
//    }

    @Test
    public void shouldReturnManagedFileName() {
        assertThat(feature.getFileName(), is("masterthought-example-ATM.feature.html"));
    }

    @Test
    public void shouldKnowIfTagsExists() {
        assertThat(feature.hasTags(), is(true));
    }

    @Test
    public void shouldListTheTags() {
        List<String> expectedList = new ArrayList<String>();
        expectedList.add("@super");
        assertThat(feature.getTagList(), is(expectedList));
    }

    @Test
    public void shouldListTheTagsAsHtml() {
        assertThat(feature.getTags(), is("<div class=\"feature-tags\">@super</div>"));
    }

    @Test
    public void shouldGetStatus() {
        assertThat(feature.getStatus(), is(Util.Status.PASSED));
    }

    @Test
    public void shouldReturnName() {
        assertThat(feature.getName(), is("<div class=\"passed\"><div class=\"feature-line\"><span class=\"feature-keyword\">Feature:</span> Account Holder withdraws cash Project 2</div></div>"));
    }

    @Test
    public void shouldReturnRawName() {
        assertThat(feature.getRawName(), is("Account Holder withdraws cash Project 2"));
    }

    @Test
    public void shouldReturnRawStatus() {
        assertThat(feature.getRawStatus(), is("passed"));
    }

    @Test
    public void shouldGetNumberOfSteps() {
        assertThat(feature.getNumberOfSteps(), is(40));

    }

    @Test
    public void shouldGetNumberOfPassingSteps() {
        assertThat(feature.getNumberOfPasses(), is(40));

    }

    @Test
    public void shouldGetNumberOfFailingSteps() {
        assertThat(feature.getNumberOfFailures(), is(0));

    }

    @Test
    public void shouldGetNumberOfSkippedSteps() {
        assertThat(feature.getNumberOfSkipped(), is(0));

    }

    @Test
    public void shouldGetNumberOfPendingSteps() {
        assertThat(feature.getNumberOfPending(), is(0));

    }

    @Test
    public void shouldGetNumberOfMissingSteps() {
        assertThat(feature.getNumberOfMissing(), is(0));

    }

    @Test
    public void shouldGetDurationOfSteps() {
        assertThat(feature.getDurationOfSteps(), is("113 ms"));

    }

//    @Test
//    public void shouldGetNumberOScenarios() {
//        assertThat(feature.getNumberOfScenarios(), is(4));
//
//    }


}
