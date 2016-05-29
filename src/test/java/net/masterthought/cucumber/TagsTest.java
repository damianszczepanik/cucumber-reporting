package net.masterthought.cucumber;

import static net.masterthought.cucumber.FileReaderUtil.getAbsolutePathFromResource;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import net.masterthought.cucumber.json.Element;
import net.masterthought.cucumber.json.Feature;
import net.masterthought.cucumber.json.support.TagObject;

public class TagsTest {

    private final Configuration configuration = new Configuration(new File(""), "testProject");

    private ReportResult reportResult;

    @Before
    public void setUpJsonReports() throws IOException {
        List<String> jsonReports = new ArrayList<String>();
        jsonReports.add(getAbsolutePathFromResource("net/masterthought/cucumber/tags.json"));
        List<Feature> features = new ReportParser(configuration).parseJsonResults(jsonReports);
        reportResult = new ReportResult(features);
    }

    @Test
    public void shouldGetTotalTagSteps() {
        assertThat(reportResult.getTagReport().getSteps()).isEqualTo(4);
    }

    @Test
    public void shouldGetTotalTagPasses() {
        assertThat(reportResult.getTagReport().getPassedSteps()).isEqualTo(2);
    }

    @Test
    public void shouldGetTotalTagFails() {
        assertThat(reportResult.getTagReport().getFailedSteps()).isEqualTo(2);
    }

    @Test
    public void shouldGetTotalTagSkipped() {
        assertThat(reportResult.getTagReport().getSkippedSteps()).isEqualTo(0);
    }

    @Test
    public void shouldGetTotalTagPending() {
        assertThat(reportResult.getTagReport().getPendingSteps()).isEqualTo(0);
    }

    @Test
    public void shouldGetTotalTagScenarios() {
        assertThat(reportResult.getTagReport().getScenarios()).isEqualTo(4);
    }

    @Test
    public void shouldgetTotalTagScenariosPassed() {
        assertThat(reportResult.getTagReport().getPassedScenarios()).isEqualTo(2);
    }

    @Test
    public void shouldgetTotalTagScenariosFailed() {
        assertThat(reportResult.getTagReport().getFailedScenarios()).isEqualTo(2);
    }

    @Test
    public void shouldGetTagInfoForTag1() {
        TagObject tagObject = reportResult.getAllTags().get(0);
        assertThat(tagObject.getName()).isEqualTo("@tag1");
    }

    @Test
    public void shouldGetTagInfoForTag2() {
        TagObject tagObject = reportResult.getAllTags().get(1);
        assertThat(tagObject.getName()).isEqualTo("@tag2");
    }

    @Test
    public void shouldGetTagScenariosForTag1() {
        List<Element> elements = reportResult.getAllTags().get(0).getElements();
        assertThat(elements.size()).isEqualTo(2);
        Element firstElement = elements.get(0);
        Element secondElement = elements.get(1);
        assertThat(firstElement.getRawName()).isEqualTo("scenario1 for tag1");
        assertThat(secondElement.getRawName()).isEqualTo("scenario2 for tag1");
    }

    @Test
    public void shouldGetTagScenariosForTag2() {
        List<Element> elements = reportResult.getAllTags().get(1).getElements();
        assertThat(elements.size()).isEqualTo(2);
        Element firstElement = elements.get(0);
        Element secondElement = elements.get(1);
        assertThat(firstElement.getRawName()).isEqualTo("scenario1 for tag2");
        assertThat(secondElement.getRawName()).isEqualTo("scenario2 for tag2");
    }

    @Test
    public void shouldGetDurations() {
        TagObject tag = reportResult.getAllTags().get(0);
        assertThat(tag.getDurations()).isEqualTo(1106277L);
    }

    @Test
    public void shouldGetFormattedDurations() {
        TagObject tag = reportResult.getAllTags().get(0);
        assertThat(tag.getFormattedDurations()).contains("ms");
    }
}
