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
        assertNotNull(fromId("steps_chart", doc));
        assertNotNull(fromId("scenarios_chart", doc));
    }

    @Test
    public void shouldRenderTheFeaturePageCorrectly() throws Exception {
        List<String> jsonReports = new ArrayList<>();
        jsonReports.add(new File(ReportBuilderTest.class.getClassLoader().getResource("net/masterthought/cucumber/project3.json").toURI()).getAbsolutePath());
        ReportBuilder reportBuilder = new ReportBuilder(jsonReports, configuration);
        reportBuilder.generateReports();

        File input = new File(configuration.getReportDirectory(), "net-masterthought-example-ATM-feature.html");
        Document doc = Jsoup.parse(input, "UTF-8", "");
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

        Elements rows = fromClass("arguments-table", doc).get(2).getElementsByTag("tr");
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
    }

    @Test
    public void shouldRenderErrorPageOnParsingError() throws Exception {
        List<String> jsonReports = new ArrayList<>();
        jsonReports.add(new File(ReportBuilderTest.class.getClassLoader().getResource("net/masterthought/cucumber/invalid_format.json").toURI()).getAbsolutePath());
        ReportBuilder reportBuilder = new ReportBuilder(jsonReports, configuration);
        reportBuilder.generateReports();

        File input = new File(configuration.getReportDirectory(), "feature-overview.html");
        Document doc = Jsoup.parse(input, "UTF-8", "");
        assertThat(fromId("title", doc).getElementsByTag("h2").text(), is("Error"));
        assertThat(fromId("title", doc).getElementsByTag("p").text(), is("Something went wrong with project cucumber-reporting, build 1"));
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
        assertThat(fromId("title", doc).getElementsByTag("h2").text(), is("Error"));
        assertThat(fromId("title", doc).getElementsByTag("p").text(), is("Something went wrong with project cucumber-reporting, build 1"));
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
        assertThat(fromId("title", doc).getElementsByTag("h2").text(), is("Error"));
        assertThat(fromId("title", doc).getElementsByTag("p").text(), is("Something went wrong with project cucumber-reporting, build 1"));
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
        assertEquals("Passed", tableCells.get(12).text());
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

    private Element fromId(String id, Element doc) {
        return doc.getElementById(id);
    }

    private Elements fromClass(String clazz, Element doc) {
        return doc.getElementsByClass(clazz);
    }
}
