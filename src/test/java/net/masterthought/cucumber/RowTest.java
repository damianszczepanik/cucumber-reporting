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
import net.masterthought.cucumber.json.Row;

public class RowTest {

    ReportParser reportParser;
    Row row;

    @Before
    public void setUpJsonReports() throws IOException {
        List<String> jsonReports = new ArrayList<String>();
        jsonReports.add(getAbsolutePathFromResource("net/masterthought/cucumber/cells.json"));
        reportParser = new ReportParser(jsonReports);
        Feature feature = reportParser.getFeatures().entrySet().iterator().next().getValue().get(0);
        row = feature.getScenarios()[0].getSteps()[0].getRows()[0];
        feature.processSteps();
    }

    @Test
    public void shouldReturnRows() {
        assertThat(row, is(Row.class));
    }

    @Test
    public void shouldReturnCells() {
        assertThat(row.cells[0], is("title"));
        assertThat(row.cells[1], is("lord"));
    }

}
