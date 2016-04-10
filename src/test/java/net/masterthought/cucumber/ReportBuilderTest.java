package net.masterthought.cucumber;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
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
    public void shouldRenderErrorPageOnNoCucumberJson() throws Exception {
        List<String> jsonReports = new ArrayList<>();
        jsonReports.add(new File(ReportBuilderTest.class.getClassLoader().getResource("net/masterthought/cucumber/somethingelse.json").toURI()).getAbsolutePath());
        ReportBuilder reportBuilder = new ReportBuilder(jsonReports, configuration);
        reportBuilder.generateReports();

        File input = new File(configuration.getReportDirectory(), ReportBuilder.HOME_PAGE);
        Document doc = Jsoup.parse(input, "UTF-8", "");
        assertThat(fromId("lead", doc).getElementsByTag("h2").text(), is("Error"));
        assertThat(fromId("lead", doc).getElementsByTag("p").text(), is("Something went wrong with project cucumber-reporting, build 1"));
        assertTrue(fromClass("error-message", doc).text().contains(
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
        assertEquals("106 ms", tableCells.get(11).html());
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
        
        assertThat(fromClass("message", doc).text(), containsString("java.lang.AssertionError:"));
    }

    private Element fromId(String id, Element doc) {
        return doc.getElementById(id);
    }

    private Elements fromClass(String clazz, Element doc) {
        return doc.getElementsByClass(clazz);
    }
}
