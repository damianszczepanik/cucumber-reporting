package net.masterthought.cucumber;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.velocity.exception.VelocityException;

import net.masterthought.cucumber.generators.ErrorPage;
import net.masterthought.cucumber.generators.FeatureOverviewPage;
import net.masterthought.cucumber.generators.FeatureReportPage;
import net.masterthought.cucumber.generators.StepOverviewPage;
import net.masterthought.cucumber.generators.TagOverviewPage;
import net.masterthought.cucumber.generators.TagReportPage;
import net.masterthought.cucumber.json.Feature;
import net.masterthought.cucumber.util.Util;

public class ReportBuilder {

    private static final Logger LOG = LogManager.getLogger(ReportBuilder.class);

    private ReportInformation reportInformation;

    private Configuration configuration;
    private List<String> jsonFiles;

    private File reportDirectory;
    private String buildNumber;
    private String buildProject;
    private String pluginUrlPath;
    private boolean runWithJenkins;

    public ReportInformation getReportInformation() {
        return reportInformation;
    }

    public File getReportDirectory() {
        return reportDirectory;
    }

    public void setReportDirectory(File reportDirectory) {
        this.reportDirectory = reportDirectory;
    }

    public String getBuildNumber() {
        return buildNumber;
    }

    public void setBuildNumber(String buildNumber) {
        this.buildNumber = buildNumber;
    }

    public String getBuildProject() {
        return buildProject;
    }

    public void setBuildProject(String buildProject) {
        this.buildProject = buildProject;
    }

    public String getPluginUrlPath() {
        return pluginUrlPath;
    }

    public void setPluginUrlPath(String pluginUrlPath) {
        this.pluginUrlPath = pluginUrlPath;
    }

    public boolean isRunWithJenkins() {
        return runWithJenkins;
    }

    public void setRunWithJenkins(boolean runWithJenkins) {
        this.runWithJenkins = runWithJenkins;
    }

    public ReportBuilder(List<String> jsonFiles, File reportDirectory, String pluginUrlPath, String buildNumber,
            String buildProject, Configuration configuration, boolean runWithJenkins)
                    throws IOException, VelocityException {

        try {
            this.jsonFiles = jsonFiles;
            this.reportDirectory = reportDirectory;
            this.pluginUrlPath = getPluginUrlPath(pluginUrlPath);
            this.buildNumber = buildNumber;
            this.buildProject = buildProject;
            this.configuration = configuration;
            this.runWithJenkins = runWithJenkins;

            // whatever happens we want to provide at least error page instead of empty report
        } catch (Exception e) {
            generateErrorPage(e);
        }
    }

    public boolean hasBuildPassed() {
        return reportInformation != null && reportInformation.getAllFailedSteps() == 0;
    }

    public void generateReports() throws IOException, VelocityException {
        try {
            ReportParser reportParser = new ReportParser(configuration);
            List<Feature> features = reportParser.parseJsonResults(jsonFiles);
            reportInformation = new ReportInformation(features);

            copyResource("theme", "blue.zip", true);
            copyResource("chart", "Highcharts-4.2.1.zip", true);
            copyResource("styles", "reporting.css", false);

            new FeatureOverviewPage(this, configuration).generatePage();
            new FeatureReportPage(this, configuration).generatePage();
            new TagReportPage(this, configuration).generatePage();
            new TagOverviewPage(this, configuration).generatePage();
            new StepOverviewPage(this, configuration).generatePage();
            // whatever happens we want to provide at least error page instead of empty report
        } catch (Exception e) {
            generateErrorPage(e);
        }
    }

    private void copyResource(String resourceLocation, String resourceName, boolean decompress)
            throws IOException, URISyntaxException {
        File tempFile = new File(reportDirectory.getAbsoluteFile(), resourceName);
        FileUtils.copyInputStreamToFile(
                this.getClass().getResourceAsStream("/" + resourceLocation + "/" + resourceName), tempFile);
        if (decompress) {
            Util.unzipToFile(tempFile, reportDirectory.getAbsolutePath());
            tempFile.delete();
        }
    }

    private String getPluginUrlPath(String path) {
        return path.isEmpty() ? "/" : path;
    }

    private void generateErrorPage(Exception exception) throws IOException {
        LOG.info(exception);
        ErrorPage errorPage = new ErrorPage(this, configuration, exception, jsonFiles);
        errorPage.generatePage();
    }
}
