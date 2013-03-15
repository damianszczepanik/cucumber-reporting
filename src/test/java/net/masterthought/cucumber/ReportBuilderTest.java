package net.masterthought.cucumber;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class ReportBuilderTest {

    @Test
    public void shouldRenderTheFeatureOverviewPageCorrectlyWithFlashCharts() throws Exception {
        File rd = new File(ReportBuilderTest.class.getClassLoader().getResource("net/masterthought/cucumber").toURI());
        List<String> jsonReports = new ArrayList<String>();
        jsonReports.add(new File(ReportBuilderTest.class.getClassLoader().getResource("net/masterthought/cucumber/project1.json").toURI()).getAbsolutePath());
        ReportBuilder reportBuilder = new ReportBuilder(jsonReports, rd, "", "1", "cucumber-reporting", false, false, true, true, false, "");
        reportBuilder.generateReports();

        File input = new File(rd, "feature-overview.html");
        Document doc = Jsoup.parse(input, "UTF-8", "");
        assertThat(fromId("overview-title", doc).text(), is("Feature Overview for Build: 1"));
        assertStatsHeader(doc);
        assertStatsFirstFeature(doc);
        assertNotNull(fromId("flash-charts", doc));
    }

    @Test
    public void shouldRenderTheFeatureOverviewPageCorrectlyWithJSCharts() throws Exception {
        File rd = new File(ReportBuilderTest.class.getClassLoader().getResource("net/masterthought/cucumber").toURI());
        List<String> jsonReports = new ArrayList<String>();
        jsonReports.add(new File(ReportBuilderTest.class.getClassLoader().getResource("net/masterthought/cucumber/project1.json").toURI()).getAbsolutePath());
        ReportBuilder reportBuilder = new ReportBuilder(jsonReports, rd, "", "1", "cucumber-reporting", false, false, false, true, false, "");
        reportBuilder.generateReports();

        File input = new File(rd, "feature-overview.html");
        Document doc = Jsoup.parse(input, "UTF-8", "");
        assertThat(fromId("overview-title", doc).text(), is("Feature Overview for Build: 1"));
        assertStatsHeader(doc);
        assertStatsFirstFeature(doc);
        assertStatsTotals(doc);
        assertNotNull(fromId("js-charts", doc));
    }

    @Test
    public void shouldRenderTheFeaturePageCorrectly() throws Exception {
        File rd = new File(ReportBuilderTest.class.getClassLoader().getResource("net/masterthought/cucumber").toURI());
        List<String> jsonReports = new ArrayList<String>();
        jsonReports.add(new File(ReportBuilderTest.class.getClassLoader().getResource("net/masterthought/cucumber/project1.json").toURI()).getAbsolutePath());
        ReportBuilder reportBuilder = new ReportBuilder(jsonReports, rd, "", "1", "cucumber-reporting", false, false, true, true, false, "");
        reportBuilder.generateReports();

        File input = new File(rd, "masterthought-example-ATM.feature.html");
        Document doc = Jsoup.parse(input, "UTF-8", "");
        assertThat(fromId("feature-title", doc).text(), is("Feature Result for Build: 1"));
        assertStatsHeader(doc);
//        assertStatsFirstFeature(doc);
//        assertNotNull(fromId("flash-charts", doc));
    }

    private void assertStatsHeader(Document doc) {
        assertThat(fromId("stats-header-scenarios", doc).text(), is("Scenarios"));
        assertThat(fromId("stats-header-feature", doc).text(), is("Feature"));
        assertThat(fromId("stats-header-scenarios-total", doc).text(), is("Total"));
        assertThat(fromId("stats-header-scenarios-passed", doc).text(), is("Passed"));
        assertThat(fromId("stats-header-scenarios-failed", doc).text(), is("Failed"));
        assertThat(fromId("stats-header-steps-total", doc).text(), is("Total"));
        assertThat(fromId("stats-header-steps-passed", doc).text(), is("Passed"));
        assertThat(fromId("stats-header-steps-failed", doc).text(), is("Failed"));
        assertThat(fromId("stats-header-steps-skipped", doc).text(), is("Skipped"));
        assertThat(fromId("stats-header-steps-pending", doc).text(), is("Pending"));
        assertThat(fromId("stats-header-duration", doc).text(), is("Duration"));
        assertThat(fromId("stats-header-status", doc).text(), is("Status"));
    }

    private void assertStatsFirstFeature(Document doc) {
        assertThat(fromId("stats-Account Holder withdraws cash", doc).text(), is("Account Holder withdraws cash"));
        assertThat(fromId("stats-number-scenarios-Account Holder withdraws cash", doc).text(), is("4"));
        assertThat(fromId("stats-number-scenarios-passed-Account Holder withdraws cash", doc).text(), is("4"));
        assertThat(fromId("stats-number-scenarios-failed-Account Holder withdraws cash", doc).text(), is("0"));
        assertThat(fromId("stats-number-steps-Account Holder withdraws cash", doc).text(), is("40"));
        assertThat(fromId("stats-number-steps-passed-Account Holder withdraws cash", doc).text(), is("40"));
        assertThat(fromId("stats-number-steps-failed-Account Holder withdraws cash", doc).text(), is("0"));
        assertThat(fromId("stats-number-steps-skipped-Account Holder withdraws cash", doc).text(), is("0"));
        assertThat(fromId("stats-number-steps-pending-Account Holder withdraws cash", doc).text(), is("0"));
        assertNotNull(fromId("stats-duration-Account Holder withdraws cash", doc));
    }

    private void assertStatsTotals(Document doc){
        assertThat(fromId("stats-total-features", doc).text(), is("2"));
        assertThat(fromId("stats-total-scenarios", doc).text(), is("5"));
        assertThat(fromId("stats-total-scenarios-passed", doc).text(), is("4"));
        assertThat(fromId("stats-total-scenarios-failed", doc).text(), is("1"));
        assertThat(fromId("stats-total-steps", doc).text(), is("49"));
        assertThat(fromId("stats-total-steps-passed", doc).text(), is("45"));
        assertThat(fromId("stats-total-steps-failed", doc).text(), is("1"));
        assertThat(fromId("stats-total-steps-skipped", doc).text(), is("3"));
        assertThat(fromId("stats-total-steps-pending", doc).text(), is("0"));
        assertNotNull(fromId("stats-total-duration", doc));
        assertThat(fromId("stats-total-totals", doc).text(), is("Totals"));
    }

    private Element fromId(String id, Document doc) {
        return doc.getElementById(id);
    }


}
