package net.masterthought.cucumber;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

public class ReportBuilderTest {

    private Configuration configuration;

    @Before
    public void setup() throws URISyntaxException {
        File rd = new File(ReportBuilderTest.class.getClassLoader().getResource("net/masterthought/cucumber").toURI());
        configuration = new Configuration(rd, "cucumber-reporting");
        configuration.setRunWithJenkins(true);
        configuration.setBuildNumber("1");
    }

    @Test
    public void shouldRenderTheFeatureOverviewPageCorrectlyWithJSCharts() throws Exception {
        List<String> jsonReports = new ArrayList<>();
        jsonReports.add(new File(ReportBuilderTest.class.getClassLoader().getResource("net/masterthought/cucumber/project3.json").toURI()).getAbsolutePath());
        ReportBuilder reportBuilder = new ReportBuilder(jsonReports, configuration);
        reportBuilder.generateReports();

        File input = new File(configuration.getReportDirectory(), "feature-overview.html");
        Document doc = Jsoup.parse(input, "UTF-8", "");
        assertThat(fromId("overview-title", doc).text(), is("Feature Overview for build: 1"));
        assertStatsHeader(doc);
        assertStatsFirstFeature(doc);
        assertStatsTotals(doc);
        assertNotNull(fromId("js-charts", doc));
    }

    @Test
    public void shouldRenderTheFeaturePageCorrectly() throws Exception {
        List<String> jsonReports = new ArrayList<>();
        jsonReports.add(new File(ReportBuilderTest.class.getClassLoader().getResource("net/masterthought/cucumber/project3.json").toURI()).getAbsolutePath());
        ReportBuilder reportBuilder = new ReportBuilder(jsonReports, configuration);
        reportBuilder.generateReports();

        File input = new File(configuration.getReportDirectory(), "net-masterthought-example-ATM-feature.html");
        Document doc = Jsoup.parse(input, "UTF-8", "");
        assertThat(fromId("feature-title", doc).text(), is("Result for Account Holder withdraws cash in build: 1"));
        assertStatsHeader(doc);
        assertStatsFirstFeature(doc);
        assertFeatureContent(doc);
    }

    @Test
    public void shouldRenderFeaturePageWithTableInStepsCorrectly() throws Exception {
        List<String> jsonReports = new ArrayList<>();
        jsonReports.add(new File(ReportBuilderTest.class.getClassLoader().getResource("net/masterthought/cucumber/tableErrorExample.json").toURI()).getAbsolutePath());
        ReportBuilder reportBuilder = new ReportBuilder(jsonReports, configuration);
        reportBuilder.generateReports();

        File input = new File(configuration.getReportDirectory(), "com-cme-falcon-acceptancetests-FrameworkTests-FIX_Inbound_Outbound-NewOrderOverrides-feature.html");
        Document doc = Jsoup.parse(input, "UTF-8", "");

        Elements rows = fromClass("data-table",doc).get(2).getElementsByTag("tr");
        Elements firstRow = rows.get(0).getElementsByTag("td");
        Elements secondRow = rows.get(1).getElementsByTag("td");

        assertThat(rows.size(),is(2));
        assertThat(firstRow.size(),is(6));
        assertThat(secondRow.size(),is(6));

        assertThat(firstRow.get(0).text(),is("ordType"));
        assertThat(firstRow.get(1).text(),is("securityDescription"));
        assertThat(firstRow.get(2).text(),is("price"));
        assertThat(firstRow.get(3).text(),is("side"));
        assertThat(firstRow.get(4).text(),is("orderQty"));
        assertThat(firstRow.get(5).text(),is("timeInForce"));

        assertThat(secondRow.get(0).text(),is("limit"));
        assertThat(secondRow.get(1).text(),is("GEZ0"));
        assertThat(secondRow.get(2).text(),is("175.0"));
        assertThat(secondRow.get(3).text(),is("bid"));
        assertThat(secondRow.get(4).text(),is("1"));
        assertThat(secondRow.get(5).text(),is("session"));

        assertThat(fromId("feature-title", doc).text(), is("Result for New Inbound Order Overrides in build: 1"));
        assertStatsHeader(doc);
    }

    @Test
    public void shouldRenderErrorPageOnParsingError() throws Exception {
        List<String> jsonReports = new ArrayList<>();
        jsonReports.add(new File(ReportBuilderTest.class.getClassLoader().getResource("net/masterthought/cucumber/invalid_format.json").toURI()).getAbsolutePath());
        ReportBuilder reportBuilder = new ReportBuilder(jsonReports, configuration);
        reportBuilder.generateReports();

        File input = new File(configuration.getReportDirectory(), "feature-overview.html");
        Document doc = Jsoup.parse(input, "UTF-8", "");
        assertThat(fromId("overview-title", doc).text(), is("Oops Something went wrong with cucumber-reporting build: 1"));
        assertTrue(fromId("error-message", doc).text().contains(
                "com.google.gson.JsonSyntaxException: com.google.gson.stream.MalformedJsonException: Unterminated object at line 19 column 18 path $[0].elements[0].keyword"));
    }

    @Test
    public void shouldRenderErrorPageOnEmptyJson() throws Exception {
        List<String> jsonReports = new ArrayList<>();
        jsonReports.add(new File(ReportBuilderTest.class.getClassLoader().getResource("net/masterthought/cucumber/empty.json").toURI()).getAbsolutePath());
        ReportBuilder reportBuilder = new ReportBuilder(jsonReports, configuration);
        reportBuilder.generateReports();

        File input = new File(configuration.getReportDirectory(), "feature-overview.html");
        Document doc = Jsoup.parse(input, "UTF-8", "");
        assertThat(fromId("overview-title", doc).text(), is("Oops Something went wrong with cucumber-reporting build: 1"));
        assertTrue(fromId("error-message", doc).text().contains("does not contan features!"));
    }

    @Test
    public void shouldRenderErrorPageOnNoCucumberJson() throws Exception {
        List<String> jsonReports = new ArrayList<>();
        jsonReports.add(new File(ReportBuilderTest.class.getClassLoader().getResource("net/masterthought/cucumber/somethingelse.json").toURI()).getAbsolutePath());
        ReportBuilder reportBuilder = new ReportBuilder(jsonReports, configuration);
        reportBuilder.generateReports();

        File input = new File(configuration.getReportDirectory(), "feature-overview.html");
        Document doc = Jsoup.parse(input, "UTF-8", "");
        assertThat(fromId("overview-title", doc).text(),
                is("Oops Something went wrong with cucumber-reporting build: 1"));
        assertTrue(fromId("error-message", doc).text().contains(
                "java.lang.IllegalStateException: Expected BEGIN_ARRAY but was BEGIN_OBJECT at line 1 column 2 path $"));
    }

    @Test
    public void shouldRenderDocStringInTagReport() throws Exception {
        List<String> jsonReports = new ArrayList<>();
        jsonReports.add(new File(ReportBuilderTest.class.getClassLoader().getResource("net/masterthought/cucumber/docstring.json").toURI()).getAbsolutePath());
        configuration.setJenkinsBasePath("/jenkins/");
        ReportBuilder reportBuilder = new ReportBuilder(jsonReports, configuration);
        reportBuilder.generateReports();

        File input = new File(configuration.getReportDirectory(), "tag-1.html");
        Document doc = Jsoup.parse(input, "UTF-8", "");
        assertThat(fromClass("doc-string", doc).get(0).text(), is("X _ X O X O _ O X"));
        Elements tableCells = doc.getElementsByClass("stats-table").get(0).getElementsByTag("tr").get(2).getElementsByTag("td");
        assertEquals("@tag:1", tableCells.get(0).text());
        assertEquals("1", tableCells.get(1).text());
        assertEquals("1", tableCells.get(2).text());
        assertEquals("0", tableCells.get(3).text());
        assertEquals("2", tableCells.get(4).text());
        assertEquals("2", tableCells.get(5).text());
        assertEquals("0", tableCells.get(6).text());
        assertEquals("0", tableCells.get(7).text());
        assertEquals("0", tableCells.get(8).text());
        assertEquals("0", tableCells.get(9).text());
        assertEquals("0", tableCells.get(10).text());
        assertEquals("106ms", tableCells.get(11).text());
        assertEquals("passed", tableCells.get(12).text());
    }
    
    @Test
    public void shouldRenderExceptionInFeatureReport() throws Exception {
        List<String> jsonReports = new ArrayList<>();
        jsonReports.add(new File(ReportBuilderTest.class.getClassLoader().getResource("net/masterthought/cucumber/project3.json").toURI()).getAbsolutePath());
        configuration.setJenkinsBasePath("/jenkins/");
        ReportBuilder reportBuilder = new ReportBuilder(jsonReports, configuration);
        reportBuilder.generateReports();

        File input = new File(configuration.getReportDirectory(), "net-masterthought-example-ATMKexception-feature.html");
        Document doc = Jsoup.parse(input, "UTF-8", "");
        
        assertThat(fromClass("output_message", doc).text(), containsString("java.lang.AssertionError:"));
    }
    

    private void assertStatsHeader(Document doc) {
        assertThat("stats-header", fromId("stats-header-scenarios", doc).text(), is("Scenarios"));
        assertThat("stats-header-key", fromId("stats-header-key", doc).text(), is("Feature"));
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
        assertThat("stats-duration", fromId("stats-duration-Account Holder withdraws cash", doc).text(), is("112ms"));
        assertNotNull(fromId("stats-duration-Account Holder withdraws cash", doc));
    }

    private void assertFeatureContent(Document doc) {
        Elements elements = doc.select("div.passed");

        assertThat("feature-keyword", elements.select("div.feature-line span.feature-keyword").first().text(), is("Feature:"));
        assertThat("feature-text", elements.select("div.feature-line").first().text(), is("Feature: Account Holder withdraws cash"));
        assertThat("scenario-background-keyword", doc.select("div.passed span.element-keyword").first().text(), is("Background:"));
        assertThat("scenario-background-name", doc.select("div.passed span.element-name").first().text(), is("Activate Credit Card"));

        elements = doc.select("div.passed span.step-keyword");
        List<String> backgroundStepKeywords = new ArrayList<>();
        List<String> firstScenarioStepKeywords = new ArrayList<>();
        for (int i = 0; i< elements.size(); i++) {
            if (i < 3) {
                backgroundStepKeywords.add(elements.get(i).text());
            } else if (i < 10) {
                firstScenarioStepKeywords.add(elements.get(i).text());
            } else {
                break;
            }
        }
        assertThat("Background step keywords must be same", backgroundStepKeywords, is(Arrays.asList(new String[] {"Given", "When", "Then"})));
        assertThat("First scenario step keywords must be same", firstScenarioStepKeywords,
                is(Arrays.asList(new String[] {"Given", "And", "And", "When", "Then", "And", "And"})));

        elements = doc.select("div.passed span.step-name");
        List<String> backgroundStepNames = new ArrayList<>();
        List<String> firstScenarioStepNames = new ArrayList<>();
        for (int i = 0; i < elements.size(); i++) {
            if (i < 3) {
                backgroundStepNames.add(elements.get(i).text());
            } else if (i < 10) {
                firstScenarioStepNames.add(elements.get(i).text());
            } else {
                break;
            }
        }
        assertThat("Background step names must be same", backgroundStepNames,
                is(Arrays.asList(new String[] {
                        "I have a new credit card",
                        "I confirm my pin number",
                        "the card should be activated"})));
        assertThat("First scenario step names must be same", firstScenarioStepNames,
                is(Arrays.asList(new String[] {
                        "the account balance is 100",
                        "the card is valid",
                        "the machine contains 100",
                        "the Account Holder requests 10",
                        "the ATM should dispense 10",
                        "the account balance should be 90",
                        "the card should be returned"})));

        elements = doc.select("div.passed span.step-duration");
//        assertFalse("step durations must not be empty", elements.isEmpty());
        List<String> stepDurations = new ArrayList<String>();
        for (int i = 0; i < elements.size(); i++) {
            if (i <= 10) {
                stepDurations.add(elements.get(i).text());
            } else {
                break;
            }
        }
        assertThat("Step durations must be same", stepDurations,
                is(Arrays.asList(new String [] {"107ms", "000ms", "000ms", "000ms", "000ms", "000ms", "000ms", "003ms", "000ms", "000ms", "000ms"})));
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
        assertThat("stats-total-steps-pending", fromId("stats-total-steps-pending", doc).text(), is("0"));
        assertNotNull(fromId("stats-total-duration", doc));
        assertThat(fromId("stats-total-totals", doc).text(), is("Totals"));
    }

    private Element fromId(String id, Element doc) {
        return doc.getElementById(id);
    }

    private Elements fromClass(String clazz, Element doc) {
        return doc.getElementsByClass(clazz);
    }
}
