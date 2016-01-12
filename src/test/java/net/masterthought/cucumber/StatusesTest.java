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

public class StatusesTest {

    private final Configuration configuration = new Configuration();

    private Feature statusesFeature;

    @Before
    public void setUpJsonReports() throws IOException {
        List<String> jsonReports = new ArrayList<String>();
        jsonReports.add(getAbsolutePathFromResource("net/masterthought/cucumber/statuses.json"));
        List<Feature> features = new ReportParser(configuration).parseJsonResults(jsonReports);
        statusesFeature = features.get(0);
    }

    @Test
    public void shouldGetNumberOfSkipped() {
        assertThat(statusesFeature.getNumberOfSkipped(), is(1));
    }

    @Test
    public void shouldGetNumberOfPassed() {
        assertThat(statusesFeature.getNumberOfPasses(), is(1));
    }

    @Test
    public void shouldGetNumberOfFailed() {
        assertThat(statusesFeature.getNumberOfFailures(), is(1));
    }

    @Test
    public void shouldGetNumberOfPending() {
        assertThat(statusesFeature.getNumberOfPending(), is(1));
    }

    @Test
    public void shouldGetNumberOfMissing() {
        assertThat(statusesFeature.getNumberOfMissing(), is(1));
    }

    @Test
    public void shouldGetNumberOfUndefined() {
        assertThat(statusesFeature.getNumberOfUndefined(), is(1));
    }
}
