package net.masterthought.cucumber;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static net.masterthought.cucumber.FileReaderUtil.getAbsolutePathFromResource;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

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
        List<String> tagNames = new ArrayList<>();
        for(TagObject tagObject : reportInformation.getTags()){
            tagNames.add(tagObject.getTagName());
        }

        assertTrue(tagNames.contains("@tag1"));
    }

    @Test
    public void shouldGetTagInfoForTag2() {
        List<String> tagNames = new ArrayList<>();
        for(TagObject tagObject : reportInformation.getTags()){
            tagNames.add(tagObject.getTagName());
        }

        assertTrue(tagNames.contains("@tag2"));
    }

    @Test
    public void shouldGetTagScenariosForTag1() {
        TagObject tagObject = null;
        for(TagObject temp : reportInformation.getTags()){
            if(temp.getTagName().equals("@tag1")){
                tagObject = temp;
            }
        }

        List<ScenarioTag> scenarios = tagObject.getScenarios();
        assertThat(scenarios.size(), is(2));
        ScenarioTag firstScenario = scenarios.get(0);
        ScenarioTag secondScenario = scenarios.get(1);
        assertThat(firstScenario.getScenario().getRawName(), is("scenario1 for tag1"));
        assertThat(secondScenario.getScenario().getRawName(), is("scenario2 for tag1"));
    }

    @Test
    public void shouldGetTagScenariosForTag2() {
        TagObject tagObject = null;
        for(TagObject temp : reportInformation.getTags()){
            if(temp.getTagName().equals("@tag2")){
                tagObject = temp;
            }
        }

        List<ScenarioTag> scenarios = tagObject.getScenarios();
        assertThat(scenarios.size(), is(2));
        ScenarioTag firstScenario = scenarios.get(0);
        ScenarioTag secondScenario = scenarios.get(1);
        assertThat(firstScenario.getScenario().getRawName(), is("scenario1 for tag2"));
        assertThat(secondScenario.getScenario().getRawName(), is("scenario2 for tag2"));
    }


}


