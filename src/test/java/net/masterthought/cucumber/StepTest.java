package net.masterthought.cucumber;

import static net.masterthought.cucumber.FileReaderUtil.getAbsolutePathFromResource;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.Is.isA;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import net.masterthought.cucumber.json.Feature;
import net.masterthought.cucumber.json.Row;
import net.masterthought.cucumber.json.Step;
import net.masterthought.cucumber.json.support.Status;

public class StepTest {

    private final Configuration configuration = new Configuration(new File(""), "testProject");

    private Step passingStep;
    private Step failingStep;

    @Before
    public void setUpJsonReports() throws IOException {
        configuration.setStatusFlags(false, false, false, false);

        List<String> jsonReports = new ArrayList<String>();
        jsonReports.add(getAbsolutePathFromResource("net/masterthought/cucumber/project1.json"));
        List<Feature> features = new ReportParser(configuration).parseJsonResults(jsonReports);
        Feature passingFeature = features.get(0);
        Feature failingFeature = features.get(1);

        passingStep = passingFeature.getElements()[0].getSteps()[0];
        failingStep = failingFeature.getElements()[0].getSteps()[5];
    }

    @Test
    public void shouldReturnRows() throws IOException {
        List<String> jsonReports = new ArrayList<String>();
        jsonReports.add(getAbsolutePathFromResource("net/masterthought/cucumber/cells.json"));
        List<Feature> features = new ReportParser(configuration).parseJsonResults(jsonReports);
        Feature feature = features.get(0);
        Step step = feature.getElements()[0].getSteps()[0];

        assertThat(step.getRows()[0], isA(Row.class));
    }

    @Test
    public void shouldKnowIfHasRows() {
        assertThat(passingStep.hasRows(), is(false));
    }

    @Test
    public void shouldReturnStatus() {
        assertThat(passingStep.getStatus(), is(Status.PASSED));
        assertThat(failingStep.getStatus(), is(Status.FAILED));
    }

    @Test
    public void shouldReturnDuration() {
        assertThat(passingStep.getDuration(), is(107447000L));
    }
}
