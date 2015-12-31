package net.masterthought.cucumber;

import static net.masterthought.cucumber.FileReaderUtil.getAbsolutePathFromResource;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.Is.isA;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import net.masterthought.cucumber.json.Feature;

public class ReportParserTest {

    @Test
    public void shouldReturnAListOfFeaturesFromAJsonReport() throws IOException {
        List<Feature> features = new ReportParser().parseJsonResults(validJsonReports());
        assertThat(features.size(), is(4));
        assertThat(features.get(0), isA(Feature.class));
        assertThat(features.get(2), isA(Feature.class));
    }

    @Test
    public void shouldContainFourFeatures() throws IOException {
        List<Feature> features = new ReportParser().parseJsonResults(validJsonReports());
        List<Feature> updatedFeatures = new ArrayList<Feature>();
        updatedFeatures.addAll(features);
        assertThat(updatedFeatures.size(), is(4));
    }

    @Test
    public void shouldProcessCucumberReportsWithNoSteps() throws IOException {
        List<Feature> features = new ReportParser().parseJsonResults(withNoStepsInJsonReport());
        assertThat(features.size(), is(4));
    }

    @Test
    public void shouldProcessCucumberReportsWithNoSteps2() throws IOException {
        List<Feature> features = new ReportParser().parseJsonResults(withNoSteps2InJsonReport());
        ReportInformation reportInformation = new ReportInformation(features);

        // Should not crash with NPE
        assertThat(reportInformation.getAllFeatures().get(0), isA(Feature.class));
        assertThat(features.size(), is(1));
    }

    @Test
    public void shouldProcessCucumberReportsWithNoScenarios() throws IOException {
        List<Feature> features = new ReportParser().parseJsonResults(withNoScenariosInJsonReport());
        assertThat(features.size(), is(4));
    }

    private List<String> validJsonReports() {
        List<String> jsonReports = new ArrayList<>();
        jsonReports.add(getAbsolutePathFromResource("net/masterthought/cucumber/project1.json"));
        jsonReports.add(getAbsolutePathFromResource("net/masterthought/cucumber/project2.json"));
        return jsonReports;
    }

    private List<String> withNoStepsInJsonReport() {
        List<String> jsonReports = new ArrayList<>();
        jsonReports.add(getAbsolutePathFromResource("net/masterthought/cucumber/project1.json"));
        jsonReports.add(getAbsolutePathFromResource("net/masterthought/cucumber/nosteps.json"));
        return jsonReports;
    }

    private List<String> withNoSteps2InJsonReport() {
        List<String> jsonReports = new ArrayList<>();
        jsonReports.add(getAbsolutePathFromResource("net/masterthought/cucumber/nosteps2.json"));
        return jsonReports;
    }

    private List<String> withNoScenariosInJsonReport() {
        List<String> jsonReports = new ArrayList<>();
        jsonReports.add(getAbsolutePathFromResource("net/masterthought/cucumber/project1.json"));
        jsonReports.add(getAbsolutePathFromResource("net/masterthought/cucumber/noscenario.json"));
        return jsonReports;
    }

}
