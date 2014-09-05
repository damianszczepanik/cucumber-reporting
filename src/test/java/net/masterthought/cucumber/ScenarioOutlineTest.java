package net.masterthought.cucumber;

import static net.masterthought.cucumber.FileReaderUtil.getAbsolutePathFromResource;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import net.masterthought.cucumber.json.Feature;
import net.masterthought.cucumber.json.Step;

public class ScenarioOutlineTest {
	
	@org.junit.Test
	public void generate() throws Exception{
	    File reportOutputDirectory = new File(getAbsolutePathFromResource("net/masterthought/cucumber"));
	    List<String> jsonReportFiles = new ArrayList<String>();
	    jsonReportFiles.add(getAbsolutePathFromResource("net/masterthought/cucumber/scenario_outline.json"));

	    String buildNumber = "1";
	    String buildProjectName = "super_project";
	    Boolean skippedFails = false;
	    Boolean undefinedFails = false;
	    Boolean flashCharts = true;
	    Boolean runWithJenkins = true;
	    Boolean artifactsEnabled = false;
	    Boolean hightCharts = false;
	    ReportBuilder reportBuilder = 	new ReportBuilder(jsonReportFiles,reportOutputDirectory,"",buildNumber,buildProjectName,skippedFails,undefinedFails,flashCharts,runWithJenkins,artifactsEnabled,"", hightCharts);
	    reportBuilder.generateReports();
	    
	    File input = new File(reportOutputDirectory, "Accounts.feature.html");
        Document doc = Jsoup.parse(input, "UTF-8", "");
        assertThat(fromId("feature-title", doc).text(), is("Feature Result for Build: 1"));
        assertStatsFirstFeature(doc);
        
         input = new File(reportOutputDirectory, "feature-overview.html");
         doc = Jsoup.parse(input, "UTF-8", "");
        assertThat(fromId("overview-title", doc).text(), is("Feature Overview for Build: 1"));
        assertStatsFirstFeature(doc);
        assertStatsTotals(doc);
	    
	}
	
	 private void assertStatsFirstFeature(Document doc) {
	        assertThat("stats", fromId("stats-name", doc).text(), is("name"));
	        assertThat("stats-number-scenarios", fromId("stats-number-scenarios-name", doc).text(), is("3"));
	        assertThat("stats-number-scenarios-passed", fromId("stats-number-scenarios-passed-name", doc).text(), is("2"));
	        assertThat("stats-number-scenarios-failed", fromId("stats-number-scenarios-failed-name", doc).text(), is("1"));
	        assertThat("stats-number-steps", fromId("stats-number-steps-name", doc).text(), is("9"));
	        assertThat("stats-number-steps-passed", fromId("stats-number-steps-passed-name", doc).text(), is("8"));
	        assertThat("stats-number-steps-failed", fromId("stats-number-steps-failed-name", doc).text(), is("1"));
	        assertThat("stats-number-steps-skipped", fromId("stats-number-steps-skipped-name", doc).text(), is("0"));
	        assertThat("stats-number-steps-pending", fromId("stats-number-steps-pending-name", doc).text(), is("0"));
	        assertThat("stats-duration", fromId("stats-duration-name", doc).text(), is("33 secs and 686 ms"));
	    }
	    private void assertStatsTotals(Document doc) {
	        assertThat("stats-total-features", fromId("stats-total-features", doc).text(), is("1"));
	        assertThat("stats-total-scenarios", fromId("stats-total-scenarios", doc).text(), is("3"));
	        assertThat("stats-total-scenarios-passed", fromId("stats-total-scenarios-passed", doc).text(), is("2"));
	        assertThat("stats-total-scenarios-failed", fromId("stats-total-scenarios-failed", doc).text(), is("1"));
	        assertThat("stats-total-steps", fromId("stats-total-steps", doc).text(), is("9"));
	        assertThat("stats-total-steps-passed", fromId("stats-total-steps-passed", doc).text(), is("8"));
	        assertThat("stats-total-steps-failed", fromId("stats-total-steps-failed", doc).text(), is("1"));
	        assertThat("stats-total-steps-skipped", fromId("stats-total-steps-skipped", doc).text(), is("0"));
	        assertThat("stats-total-steps-pending", fromId("stats-total-steps-pending", doc).text(), is("0"));
	        assertNotNull(fromId("stats-total-duration", doc));
	        assertThat(fromId("stats-total-totals", doc).text(), is("Totals"));
	    }
	private Element fromId(String id, Element doc) {
        return doc.getElementById(id);
    }

}
