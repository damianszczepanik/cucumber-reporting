package net.masterthought.cucumber;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static net.masterthought.cucumber.FileReaderUtil.getAbsolutePathFromResource;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class TagsTest {

    ReportParser reportParser;
    ReportInformation reportInformation;

    @Before
    public void setUpJsonReports() throws IOException {
        List<String> jsonReports = new ArrayList<String>();
        jsonReports.add(getAbsolutePathFromResource("net/masterthought/cucumber/tags.json"));
        reportParser = new ReportParser(jsonReports);
        reportInformation = new ReportInformation(reportParser.getFeatures());

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
        assertThat(reportInformation.getTotalTagDuration(), is("1 ms"));
    }

    @Test
    public void shouldGetTotalTagScenarios() {
        assertThat(reportInformation.getTotalTagScenarios(), is(4));
    }

    @Test
    public void shouldGetTotalPassingTagScenarios() {
        assertThat(reportInformation.getTotalPassingTagScenarios(), is(2));
    }

    @Test
    public void shouldGetTotalFailingTagScenarios() {
        assertThat(reportInformation.getTotalFailingTagScenarios(), is(2));
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
        List<ScenarioTag> scenarios = reportInformation.getTags().get(0).getScenarios();
        assertThat(scenarios.size(), is(2));
        ScenarioTag firstScenario = scenarios.get(0);
        ScenarioTag secondScenario = scenarios.get(1);
        assertThat(firstScenario.getScenario().getRawName(), is("scenario1 for tag1"));
        assertThat(secondScenario.getScenario().getRawName(), is("scenario2 for tag1"));
    }

    @Test
    public void shouldGetTagScenariosForTag2() {
        List<ScenarioTag> scenarios = reportInformation.getTags().get(1).getScenarios();
        assertThat(scenarios.size(), is(2));
        ScenarioTag firstScenario = scenarios.get(0);
        ScenarioTag secondScenario = scenarios.get(1);
        assertThat(firstScenario.getScenario().getRawName(), is("scenario1 for tag2"));
        assertThat(secondScenario.getScenario().getRawName(), is("scenario2 for tag2"));
    }


}


