package net.masterthought.cucumber;


import net.masterthought.cucumber.json.Element;
import net.masterthought.cucumber.json.Feature;
import net.masterthought.cucumber.json.Step;
import net.masterthought.cucumber.util.Util;
import org.junit.Test;
import sun.jvm.hotspot.debugger.linux.amd64.LinuxAMD64ThreadContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ReportParserTest {

    @Test
    public void shouldReturnAListOfFeaturesFromAJsonReport() throws IOException {
        ReportParser reportParser = new ReportParser(validJsonReports());
        assertThat(reportParser.getFeatures().entrySet().size(), is(2));
        assertThat(reportParser.getFeatures().entrySet().iterator().next().getValue().get(0), is(Feature.class));
        assertThat(reportParser.getFeatures().entrySet().iterator().next().getValue().get(1), is(Feature.class));
    }

    @Test
    public void shouldContainFourFeatures() throws IOException {
        ReportParser reportParser = new ReportParser(validJsonReports());
        Iterator it = reportParser.getFeatures().entrySet().iterator();
        List<Feature> features = new ArrayList<Feature>();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry) it.next();
            List<Feature> featureList = (List<Feature>) pairs.getValue();
            features.addAll(featureList);
        }
        assertThat(features.size(), is(4));
    }

    @Test
    public void shouldIgnoreEmptyJsonFiles() throws IOException {
        ReportParser reportParser = new ReportParser(withEmptyJsonReport());
        assertThat(reportParser.getFeatures().entrySet().size(), is(1));
    }

    @Test
    public void shouldIgnoreJsonFilesThatAreNotCucumberReports() throws IOException {
        ReportParser reportParser = new ReportParser(withNonCucumberJson());
        assertThat(reportParser.getFeatures().entrySet().size(), is(1));
    }

    private List<String> validJsonReports() {
        List<String> jsonReports = new ArrayList<String>();
        jsonReports.add("src/test/resources/net/masterthought/cucumber/project1.json");
        jsonReports.add("src/test/resources/net/masterthought/cucumber/project2.json");
        return jsonReports;
    }

    private List<String> withEmptyJsonReport() {
        List<String> jsonReports = new ArrayList<String>();
        jsonReports.add("src/test/resources/net/masterthought/cucumber/project1.json");
        jsonReports.add("src/test/resources/net/masterthought/cucumber/empty.json");
        return jsonReports;
    }

    private List<String> withNonCucumberJson() {
        List<String> jsonReports = new ArrayList<String>();
        jsonReports.add("src/test/resources/net/masterthought/cucumber/project1.json");
        jsonReports.add("src/test/resources/net/masterthought/cucumber/somethingelse.json");
        return jsonReports;
    }

}
