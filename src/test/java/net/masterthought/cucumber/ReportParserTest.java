package net.masterthought.cucumber;

import static net.masterthought.cucumber.FileReaderUtil.getAbsolutePathFromResource;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.Is.isA;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import net.masterthought.cucumber.json.Feature;

public class ReportParserTest {

    @Test
    public void shouldReturnAListOfFeaturesFromAJsonReport() throws IOException {
        Map<String, List<Feature>> features = new ReportParser().parseJsonResults(validJsonReports());
        assertThat(features.entrySet().size(), is(2));
        assertThat(features.entrySet().iterator().next().getValue().get(0), isA(Feature.class));
        assertThat(features.entrySet().iterator().next().getValue().get(1), isA(Feature.class));
    }

    @Test
    public void shouldContainFourFeatures() throws IOException {
        Map<String, List<Feature>> features = new ReportParser().parseJsonResults(validJsonReports());
        List<Feature> updatedFeatures = new ArrayList<Feature>();
        for (Map.Entry<String, List<Feature>> pairs : features.entrySet()) {
            List<Feature> featureList = pairs.getValue();
            updatedFeatures.addAll(featureList);
        }
        assertThat(updatedFeatures.size(), is(4));
    }

    @Test
    public void shouldIgnoreEmptyJsonFiles() throws IOException {
        Map<String, List<Feature>> features = new ReportParser().parseJsonResults(withEmptyJsonReport());
        assertThat(features.entrySet().size(), is(1));
    }

    @Test
    public void shouldIgnoreJsonFilesThatAreNotCucumberReports() throws IOException {
        Map<String, List<Feature>> features = new ReportParser().parseJsonResults(withNonCucumberJson());
        assertThat(features.entrySet().size(), is(1));
    }

    @Test
    public void shouldProcessCucumberReportsWithNoSteps() throws IOException {
        Map<String, List<Feature>> features = new ReportParser().parseJsonResults(withNoStepsInJsonReport());
        assertThat(features.entrySet().size(), is(2));
    }

    @Test
    public void shouldProcessCucumberReportsWithNoSteps2() throws IOException {
        Map<String, List<Feature>> features = new ReportParser().parseJsonResults(withNoSteps2InJsonReport());
        ReportInformation reportInformation = new ReportInformation(features);

        // Should not crash with NPE
        assertThat(reportInformation.getFeatures().get(0), isA(Feature.class));
        assertThat(features.entrySet().size(), is(1));
    }

    @Test
    public void shouldProcessCucumberReportsWithNoScenarios() throws IOException {
        Map<String, List<Feature>> features = new ReportParser().parseJsonResults(withNoScenariosInJsonReport());
        assertThat(features.entrySet().size(), is(2));
    }
    
    private List<String> validJsonReports() {
        List<String> jsonReports = new ArrayList<String>();
        jsonReports.add(getAbsolutePathFromResource("net/masterthought/cucumber/project1.json"));
        jsonReports.add(getAbsolutePathFromResource("net/masterthought/cucumber/project2.json"));
        return jsonReports;
    }

    private List<String> withEmptyJsonReport() {
        List<String> jsonReports = new ArrayList<String>();
        jsonReports.add(getAbsolutePathFromResource("net/masterthought/cucumber/project1.json"));
        jsonReports.add(getAbsolutePathFromResource("net/masterthought/cucumber/empty.json"));
        return jsonReports;
    }

    private List<String> withNonCucumberJson() {
        List<String> jsonReports = new ArrayList<String>();
        jsonReports.add(getAbsolutePathFromResource("net/masterthought/cucumber/project1.json"));
        jsonReports.add(getAbsolutePathFromResource("net/masterthought/cucumber/somethingelse.json"));
        return jsonReports;
    }

    private List<String> withNoStepsInJsonReport() {
        List<String> jsonReports = new ArrayList<String>();
        jsonReports.add(getAbsolutePathFromResource("net/masterthought/cucumber/project1.json"));
        jsonReports.add(getAbsolutePathFromResource("net/masterthought/cucumber/nosteps.json"));
        return jsonReports;
    }

    private List<String> withNoSteps2InJsonReport() {
        List<String> jsonReports = new ArrayList<String>();
        jsonReports.add(getAbsolutePathFromResource("net/masterthought/cucumber/nosteps2.json"));
        return jsonReports;
    }

    private List<String> withNoScenariosInJsonReport() {
        List<String> jsonReports = new ArrayList<String>();
        jsonReports.add(getAbsolutePathFromResource("net/masterthought/cucumber/project1.json"));
        jsonReports.add(getAbsolutePathFromResource("net/masterthought/cucumber/noscenario.json"));
        return jsonReports;
    }

}
