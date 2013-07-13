package net.masterthought.cucumber;

import net.masterthought.cucumber.json.Feature;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static net.masterthought.cucumber.FileReaderUtil.getAbsolutePathFromResource;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class StatusesTest {

    ReportParser reportParser;
    Feature statusesFeature;

    @Before
    public void setUpJsonReports() throws IOException {
        List<String> jsonReports = new ArrayList<String>();
        jsonReports.add(getAbsolutePathFromResource("net/masterthought/cucumber/statuses.json"));
        reportParser = new ReportParser(jsonReports);
        statusesFeature = reportParser.getFeatures().entrySet().iterator().next().getValue().get(0);
        statusesFeature.processSteps();
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


