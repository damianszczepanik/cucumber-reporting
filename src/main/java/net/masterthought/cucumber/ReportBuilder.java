package net.masterthought.cucumber;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.masterthought.cucumber.generators.ErrorPage;
import net.masterthought.cucumber.generators.FeatureOverviewPage;
import net.masterthought.cucumber.generators.FeatureReportPage;
import net.masterthought.cucumber.generators.StepOverviewPage;
import net.masterthought.cucumber.generators.TagOverviewPage;
import net.masterthought.cucumber.generators.TagReportPage;
import net.masterthought.cucumber.json.Feature;
import net.masterthought.cucumber.json.support.TagObject;

public class ReportBuilder {

    private static final Logger LOG = LogManager.getLogger(ReportBuilder.class);

    private ReportResult reportResult;

    private Configuration configuration;
    private List<String> jsonFiles;

    public ReportBuilder(List<String> jsonFiles, Configuration configuration) {
        try {
            this.jsonFiles = jsonFiles;
            this.configuration = configuration;

            // whatever happens we want to provide at least error page instead of empty report
        } catch (Exception e) {
            generateErrorPage(e);
        }
    }

    /**
     * Checks if all features pass.
     * 
     * @return true if all feature pass otherwise false
     */
    public boolean hasBuildPassed() {
        return reportResult != null && reportResult.getAllFailedFeatures() == 0;
    }

    public void generateReports() {
        try {
            ReportParser reportParser = new ReportParser(configuration);
            List<Feature> features = reportParser.parseJsonResults(jsonFiles);
            reportResult = new ReportResult(features);

            copyResources("css", "reporting.css", "bootstrap.min.css");
            copyResources("js", "jquery.min.js", "bootstrap.min.js", "jquery.tablesorter.min.js",
                    "highcharts.js", "highcharts-3d.js");

            new FeatureOverviewPage(reportResult, configuration).generatePage();
            for (Feature feature : reportResult.getAllFeatures()) {
                new FeatureReportPage(reportResult, configuration, feature).generatePage();
            }
            new TagOverviewPage(reportResult, configuration).generatePage();
            for (TagObject tagObject : reportResult.getAllTags()) {
                new TagReportPage(reportResult, configuration, tagObject).generatePage();
            }
            new StepOverviewPage(reportResult, configuration).generatePage();

            // whatever happens we want to provide at least error page instead of empty report
        } catch (Exception e) {
            generateErrorPage(e);
        }
    }

    private void copyResources(String resourceLocation, String... resources)
            throws IOException, URISyntaxException {
        for (String resource : resources) {
            File tempFile = new File(configuration.getReportDirectory().getAbsoluteFile(), resource);
            // don't change this implementation unless you verified it works on Jenkins
            FileUtils.copyInputStreamToFile(
                    this.getClass().getResourceAsStream("/" + resourceLocation + "/" + resource), tempFile);
        }
    }

    private void generateErrorPage(Exception exception) {
        LOG.info(exception);
        ErrorPage errorPage = new ErrorPage(reportResult, configuration, exception, jsonFiles);
        errorPage.generatePage();
    }
}
