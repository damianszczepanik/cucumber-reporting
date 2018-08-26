package net.masterthought.cucumber;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TrendTableRowTest {

    TrendTableRow trendTableRow;
    @Before
    public void setUp() throws Exception {
        trendTableRow = new TrendTableRow("device 1","feature 1", "scenario 1","id");
        trendTableRow.setStatus("PASSED");
    }

    @Test
    public void getScenarioName() throws Exception {
        assertEquals("scenario 1", trendTableRow.getScenarioName());
    }

    @Test
    public void getStatuses() throws Exception {
        assertThat(trendTableRow.getStatuses()).asList();
        assertEquals("PASSED", trendTableRow.getStatuses().get(0));
    }

    @Test
    public void setStatus() throws Exception {
        int sizeBefore = trendTableRow.getStatuses().size();
        trendTableRow.setStatus("FAILED");

        assertEquals(sizeBefore+1, trendTableRow.getStatuses().size());
        assertEquals("FAILED", trendTableRow.getStatuses().get(1));
    }

    @Test
    public void getFeatureName() throws Exception {
        assertEquals("feature 1", trendTableRow.getFeatureName());
    }

    @Test
    public void getDeviceName() throws Exception {
        assertEquals("device 1", trendTableRow.getDeviceName());
    }

    @Test
    public void getId() throws Exception {
        assertEquals("id", trendTableRow.getId());
    }

}