package net.masterthought.cucumber;

import net.masterthought.cucumber.json.Feature;
import net.masterthought.cucumber.json.Row;
import net.masterthought.cucumber.json.Step;
import net.masterthought.cucumber.util.Util;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static net.masterthought.cucumber.FileReaderUtil.getAbsolutePathFromResource;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class StepTest {

    ReportParser reportParser;
    Step passingStep;
    Step failingStep;
    Step skippedStep;

    @Before
    public void setUpJsonReports() throws IOException {
        List<String> jsonReports = new ArrayList<String>();
        jsonReports.add(getAbsolutePathFromResource("net/masterthought/cucumber/project1.json"));
        reportParser = new ReportParser(jsonReports);
        Feature passingFeature = reportParser.getFeatures().entrySet().iterator().next().getValue().get(0);
        Feature failingFeature = reportParser.getFeatures().entrySet().iterator().next().getValue().get(1);
        passingFeature.processSteps();
        failingFeature.processSteps();
        passingStep = passingFeature.getElements()[0].getSteps()[0];
        failingStep = failingFeature.getElements()[0].getSteps()[5];
        skippedStep = failingFeature.getElements()[0].getSteps()[6];
    }

    @Test
    public void shouldReturnRows() throws IOException {
        List<String> jsonReports = new ArrayList<String>();
        jsonReports.add(getAbsolutePathFromResource("net/masterthought/cucumber/cells.json"));
        ReportParser reportParser = new ReportParser(jsonReports);
        Feature feature = reportParser.getFeatures().entrySet().iterator().next().getValue().get(0);
        Step step = feature.getElements()[0].getSteps()[0];
        feature.processSteps();
        assertThat(step.getRows()[0], is(Row.class));
    }

    @Test
    public void shouldKnowIfHasRows() {
        assertThat(passingStep.hasRows(), is(false));
    }

    @Test
    public void shouldReturnStatus() {
        assertThat(passingStep.getStatus(), is(Util.Status.PASSED));
        assertThat(failingStep.getStatus(), is(Util.Status.FAILED));
    }

    @Test
    public void shouldReturnDuration() {
        assertThat(passingStep.getDuration(), is(107447000L));
    }

    @Test
    public void shouldReturnDataTableClass() {
        assertThat(passingStep.getDataTableClass(), is("passed"));
        assertThat(failingStep.getDataTableClass(), is("failed"));
    }

    @Test
    public void shouldReturnName() {
        assertThat(passingStep.getName(), is("<div class=\"passed\"><span class=\"step-keyword\">Given  </span><span class=\"step-name\">I have a new credit card</span></div>"
        ));
    }

    @Test
    public void shouldReturnNameWhenStepSkipped() {
        ConfigurationOptions.setSkippedFailsBuild(false);
        assertThat(skippedStep.getName(), is("<div class=\"skipped\"><span class=\"step-keyword\">And  </span><span class=\"step-name\">the card should be returned</span></div>"
        ));
    }

    @Test
    public void shouldReturnNameWhenConfigSkippedTurnedOn() {
        ConfigurationOptions.setSkippedFailsBuild(true);
        assertThat(skippedStep.getName(), is("<div class=\"failed\"><span class=\"step-keyword\">And  </span><span class=\"step-name\">the card should be returned</span><div class=\"step-error-message\"><pre>Mode: Skipped causes Failure<br/><span class=\"skipped\">This step was skipped</span></pre></div></div>"
        ));
    }



}

