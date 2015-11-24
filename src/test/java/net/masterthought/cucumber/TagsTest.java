package net.masterthought.cucumber;

import static net.masterthought.cucumber.FileReaderUtil.getAbsolutePathFromResource;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import net.masterthought.cucumber.json.Feature;
import net.masterthought.cucumber.json.Scenario;
import net.masterthought.cucumber.json.support.TagObject;

public class TagsTest {

    private ReportInformation reportInformation;

    @Before
    public void setUpJsonReports() throws IOException {
        List<String> jsonReports = new ArrayList<String>();
        jsonReports.add(getAbsolutePathFromResource("net/masterthought/cucumber/tags.json"));
        List<Feature> features = new ReportParser().parseJsonResults(jsonReports);
        reportInformation = new ReportInformation(features);
    }

    @Test
    public void shouldGetTotalTagSteps() {
        assertThat(reportInformation.getTotalTagSteps(), is(4));
    }

    @Test
    public void shouldGetTotalTagPasses() {
        assertThat(reportInformation.getTotalTagPasses(), is(2));
    }

    @Test
    public void shouldGetTotalTagFails() {
        assertThat(reportInformation.getTotalTagFails(), is(2));
    }

    @Test
    public void shouldGetTotalTagSkipped() {
        assertThat(reportInformation.getTotalTagSkipped(), is(0));
    }

    @Test
    public void shouldGetTotalTagPending() {
        assertThat(reportInformation.getTotalTagPending(), is(0));
    }

    @Test
    public void shouldGetTotalTagDuration() {
        assertThat(reportInformation.getTotalTagDuration(), is("001ms"));
    }

    @Test
    public void shouldGetTotalTagScenarios() {
        assertThat(reportInformation.getTotalTagScenarios(), is(4));
    }

    @Test
    public void shouldgetTotalTagScenariosPassed() {
        assertThat(reportInformation.getTotalTagScenariosPassed(), is(2));
    }

    @Test
    public void shouldgetTotalTagScenariosFailed() {
        assertThat(reportInformation.getTotalTagScenariosFailed(), is(2));
    }

    @Test
    public void shouldGetTagInfoForTag1() {
        TagObject tagObject = reportInformation.getTags().get(0);
        assertThat(tagObject.getTagName(), is("@tag1"));
    }

    @Test
    public void shouldGetTagInfoForTag2() {
        TagObject tagObject = reportInformation.getTags().get(1);
        assertThat(tagObject.getTagName(), is("@tag2"));
    }

    @Test
    public void shouldGetTagScenariosForTag1() {
        List<Scenario> scenarios = reportInformation.getTags().get(0).getScenarios();
        assertThat(scenarios.size(), is(2));
        Scenario firstScenario = scenarios.get(0);
        Scenario secondScenario = scenarios.get(1);
        assertThat(firstScenario.getRawName(), is("scenario1 for tag1"));
        assertThat(secondScenario.getRawName(), is("scenario2 for tag1"));
    }

    @Test
    public void shouldGetTagScenariosForTag2() {
        List<Scenario> scenarios = reportInformation.getTags().get(1).getScenarios();
        assertThat(scenarios.size(), is(2));
        Scenario firstScenario = scenarios.get(0);
        Scenario secondScenario = scenarios.get(1);
        assertThat(firstScenario.getRawName(), is("scenario1 for tag2"));
        assertThat(secondScenario.getRawName(), is("scenario2 for tag2"));
    }
}
