package net.masterthought.cucumber;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class ReportBuilderTest {

    @Test
    public void shouldRenderTheFeatureOverviewPageCorrectlyWithFlashCharts() throws Exception {
        File rd = new File(ReportBuilderTest.class.getClassLoader().getResource("net/masterthought/cucumber").toURI());
        List<String> jsonReports = new ArrayList<String>();
        jsonReports.add(new File(ReportBuilderTest.class.getClassLoader().getResource("net/masterthought/cucumber/project3.json").toURI()).getAbsolutePath());
        ReportBuilder reportBuilder = new ReportBuilder(jsonReports, rd, "", "1", "cucumber-reporting", false, false, true, true, false, "", false);
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
        jsonReports.add(new File(ReportBuilderTest.class.getClassLoader().getResource("net/masterthought/cucumber/project3.json").toURI()).getAbsolutePath());
        ReportBuilder reportBuilder = new ReportBuilder(jsonReports, rd, "", "1", "cucumber-reporting", false, false, false, true, false, "", false);
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
        jsonReports.add(new File(ReportBuilderTest.class.getClassLoader().getResource("net/masterthought/cucumber/project3.json").toURI()).getAbsolutePath());
        ReportBuilder reportBuilder = new ReportBuilder(jsonReports, rd, "", "1", "cucumber-reporting", false, false, true, true, false, "", false);
        reportBuilder.generateReports();

        File input = new File(rd, "masterthought-example-ATM.feature.html");
        Document doc = Jsoup.parse(input, "UTF-8", "");
        assertThat(fromId("feature-title", doc).text(), is("Feature Result for Build: 1"));
        assertStatsHeader(doc);
        assertStatsFirstFeature(doc);
    }

    @Test
    public void shouldRenderErrorPageOnParsingError() throws Exception {
        File rd = new File(ReportBuilderTest.class.getClassLoader().getResource("net/masterthought/cucumber").toURI());
        List<String> jsonReports = new ArrayList<String>();
        jsonReports.add(new File(ReportBuilderTest.class.getClassLoader().getResource("net/masterthought/cucumber/invalid_format.json").toURI()).getAbsolutePath());
        ReportBuilder reportBuilder = new ReportBuilder(jsonReports, rd, "", "1", "cucumber-reporting", false, false, true, true, false, "", false);
        reportBuilder.generateReports();

        File input = new File(rd, "feature-overview.html");
        Document doc = Jsoup.parse(input, "UTF-8", "");
        assertThat(fromId("overview-title", doc).text(), is("Oops Something went wrong with cucumber-reporting build: 1"));
        assertThat(fromId("error-message", doc).text(), is("com.google.gson.JsonSyntaxException: com.google.gson.stream.MalformedJsonException: Unterminated object at line 19 column 18"));
    }

    @Test
    public void shouldRenderErrorPageOnReportGenerationError() throws Exception {
        File rd = new File(ReportBuilderTest.class.getClassLoader().getResource("net/masterthought/cucumber").toURI());
        List<String> jsonReports = new ArrayList<String>();
        jsonReports.add(new File(ReportBuilderTest.class.getClassLoader().getResource("net/masterthought/cucumber/missing_elements.json").toURI()).getAbsolutePath());
        ReportBuilder reportBuilder = new ReportBuilder(jsonReports, rd, "", "1", "cucumber-reporting", false, false, true, true, false, "", false);
        reportBuilder.generateReports();

        File input = new File(rd, "feature-overview.html");
        Document doc = Jsoup.parse(input, "UTF-8", "");
        assertThat(fromId("overview-title", doc).text(), is("Oops Something went wrong with cucumber-reporting build: 1"));
        assertThat(fromId("error-message", doc).text(), is("java.lang.NullPointerException"));
    }

    private void assertStatsHeader(Document doc) {
        assertThat("stats-header", fromId("stats-header-scenarios", doc).text(), is("Scenarios"));
        assertThat("stats-header-feature", fromId("stats-header-feature", doc).text(), is("Feature"));
        assertThat("stats-header-scenarios-total", fromId("stats-header-scenarios-total", doc).text(), is("Total"));
        assertThat("stats-header-scenarios-passed", fromId("stats-header-scenarios-passed", doc).text(), is("Passed"));
        assertThat("stats-header-scenarios-failed", fromId("stats-header-scenarios-failed", doc).text(), is("Failed"));
        assertThat("stats-header-steps-total", fromId("stats-header-steps-total", doc).text(), is("Total"));
        assertThat("stats-header-steps-passed", fromId("stats-header-steps-passed", doc).text(), is("Passed"));
        assertThat("stats-header-steps-failed", fromId("stats-header-steps-failed", doc).text(), is("Failed"));
        assertThat("stats-header-steps-skipped", fromId("stats-header-steps-skipped", doc).text(), is("Skipped"));
        assertThat("stats-header-steps-pending", fromId("stats-header-steps-pending", doc).text(), is("Pending"));
        assertThat("stats-header-duration", fromId("stats-header-duration", doc).text(), is("Duration"));
        assertThat("stats-header-status", fromId("stats-header-status", doc).text(), is("Status"));
    }

    private void assertStatsFirstFeature(Document doc) {
        assertThat("stats", fromId("stats-Account Holder withdraws cash", doc).text(), is("Account Holder withdraws cash"));
        assertThat("stats-number-scenarios", fromId("stats-number-scenarios-Account Holder withdraws cash", doc).text(), is("4"));
        assertThat("stats-number-scenarios-passed", fromId("stats-number-scenarios-passed-Account Holder withdraws cash", doc).text(), is("4"));
        assertThat("stats-number-scenarios-failed", fromId("stats-number-scenarios-failed-Account Holder withdraws cash", doc).text(), is("0"));
        assertThat("stats-number-steps", fromId("stats-number-steps-Account Holder withdraws cash", doc).text(), is("40"));
        assertThat("stats-number-steps-passed", fromId("stats-number-steps-passed-Account Holder withdraws cash", doc).text(), is("40"));
        assertThat("stats-number-steps-failed", fromId("stats-number-steps-failed-Account Holder withdraws cash", doc).text(), is("0"));
        assertThat("stats-number-steps-skipped", fromId("stats-number-steps-skipped-Account Holder withdraws cash", doc).text(), is("0"));
        assertThat("stats-number-steps-pending", fromId("stats-number-steps-pending-Account Holder withdraws cash", doc).text(), is("0"));
        assertNotNull(fromId("stats-duration-Account Holder withdraws cash", doc));
    }

    private void assertStatsTotals(Document doc) {
        assertThat("stats-total-features", fromId("stats-total-features", doc).text(), is("4"));
        assertThat("stats-total-scenarios", fromId("stats-total-scenarios", doc).text(), is("7"));
        assertThat("stats-total-scenarios-passed", fromId("stats-total-scenarios-passed", doc).text(), is("6"));
        assertThat("stats-total-scenarios-failed", fromId("stats-total-scenarios-failed", doc).text(), is("1"));
        assertThat("stats-total-steps", fromId("stats-total-steps", doc).text(), is("67"));
        assertThat("stats-total-steps-passed", fromId("stats-total-steps-passed", doc).text(), is("55"));
        assertThat("stats-total-steps-failed", fromId("stats-total-steps-failed", doc).text(), is("1"));
        assertThat("stats-total-steps-skipped", fromId("stats-total-steps-skipped", doc).text(), is("7"));
        assertThat("stats-total-steps-pending", fromId("stats-total-steps-pending", doc).text(), is("4"));
        assertNotNull(fromId("stats-total-duration", doc));
        assertThat(fromId("stats-total-totals", doc).text(), is("Totals"));
    }

    private Element fromId(String id, Document doc) {
        return doc.getElementById(id);
    }


}
