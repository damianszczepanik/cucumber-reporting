package net.masterthought.cucumber;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

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
        assertFeatureContent(doc);
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

    @Test
    public void shouldRenderDocStringInTagReport() throws Exception {
        File rd = new File(ReportBuilderTest.class.getClassLoader().getResource("net/masterthought/cucumber").toURI());
        List<String> jsonReports = new ArrayList<String>();
        jsonReports.add(new File(ReportBuilderTest.class.getClassLoader().getResource("net/masterthought/cucumber/docstring.json").toURI()).getAbsolutePath());
        ReportBuilder reportBuilder = new ReportBuilder(jsonReports, rd, "/jenkins/", "1", "cucumber-reporting", false, false, true, true, false, "", false);
        reportBuilder.generateReports();

        File input = new File(rd, "tag1.html");
        Document doc = Jsoup.parse(input, "UTF-8", "");
        assertThat(fromClass("doc-string",doc).get(0).text(),is("X _ X O X O _ O X"));
        Elements tableCells = doc.getElementsByClass("stats-table").get(0).getElementsByTag("tr").get(2).getElementsByTag("td");
        assertEquals("@tag1",tableCells.get(0).text());
        assertEquals("1",tableCells.get(1).text());
        assertEquals("1",tableCells.get(2).text());
        assertEquals("0",tableCells.get(3).text());
        assertEquals("2",tableCells.get(4).text());
        assertEquals("2",tableCells.get(5).text());
        assertEquals("0",tableCells.get(6).text());
        assertEquals("0",tableCells.get(7).text());
        assertEquals("0",tableCells.get(8).text());
        assertEquals("106 ms",tableCells.get(9).text());
        assertEquals("passed",tableCells.get(10).text());
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
        assertThat("stats-duration", fromId("stats-duration-Account Holder withdraws cash", doc).text(), is("112 ms"));
        assertNotNull(fromId("stats-duration-Account Holder withdraws cash", doc));
    }

    private void assertFeatureContent(Document doc) {
        Elements elements = doc.select("div.passed");

        assertThat("feature-keyword", elements.select("div.feature-line span.feature-keyword").first().text(), is("Feature:"));
        assertThat("feature-text", elements.select("div.feature-line").first().text(), is("Feature: Account Holder withdraws cash"));
        assertThat("feature-description", doc.select("div.feature-description").first().text(),
                is("As a Account Holder I want to withdraw cash from an ATM So that I can get money when the bank is closed"));
        assertThat("scenario-background-keyword", doc.select("div.passed span.scenario-keyword").first().text(), is("Background:"));
        assertThat("scenario-background-name", doc.select("div.passed span.scenario-name").first().text(), is("Activate Credit Card"));

        elements = doc.select("div.passed span.step-keyword");
        List<String> backgroundStepKeywords = new ArrayList<String>();
        List<String> firstScenarioStepKeywords = new ArrayList<String>();
        for (Element element : elements) {
            int index = elements.indexOf(element);
            if (index < 3) {
                backgroundStepKeywords.add(element.text());
            } else if (index >= 3 && index < 10) {
                firstScenarioStepKeywords.add(element.text());
            } else {
                break;
            }
        }
        assertThat("Background step keywords must be same", backgroundStepKeywords, is(Arrays.asList(new String[] {"Given", "When", "Then"})));
        assertThat("First scenario step keywords must be same", firstScenarioStepKeywords,
                is(Arrays.asList(new String[] {"Given", "And", "And", "When", "Then", "And", "And"})));

        elements = doc.select("div.passed span.step-name");
        List<String> backgroundStepNames = new ArrayList<String>();
        List<String> firstScenarioStepNames = new ArrayList<String>();
        for (Element element : elements) {
            int index = elements.indexOf(element);
            if (index < 3) {
                backgroundStepNames.add(element.text());
            } else if (index >= 3 && index < 10) {
                firstScenarioStepNames.add(element.text());
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
        for (Element element : elements) {
            stepDurations.add(element.text());
            int index = elements.indexOf(element);
            if (index >= 10) break;
        }
        assertThat("Step durations must be same", stepDurations,
                is(Arrays.asList(new String [] {"107 ms", "0 ms", "0 ms", "0 ms", "0 ms", "0 ms", "0 ms", "3 ms", "0 ms", "0 ms", "0 ms"})));
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

    private Element fromId(String id, Element doc) {
        return doc.getElementById(id);
    }

    private Elements fromClass(String clazz, Element doc) {
        return doc.getElementsByClass(clazz);
    }

    private Elements fromTag(String tag, Element element) {
        return element.getElementsByTag(tag);
    }
}
