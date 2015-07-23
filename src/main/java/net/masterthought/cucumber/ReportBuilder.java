package net.masterthought.cucumber;

import net.masterthought.cucumber.generators.*;
import net.masterthought.cucumber.json.Feature;
import net.masterthought.cucumber.util.UnzipUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.velocity.exception.VelocityException;

import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReportBuilder {

    private ReportInformation reportInformation;
    private File reportDirectory;
    private String buildNumber;
    private String buildProject;
    private String pluginUrlPath;
    private boolean flashCharts;
    private boolean runWithJenkins;
    private boolean artifactsEnabled;
    private boolean highCharts;
    private boolean parsingError;
    private boolean tagsReportingEnabled;

    private Map<String, String> customHeader;

    public static final String VERSION = "cucumber-reporting-0.1.0";

    //Added to control parallel reports
    private static boolean parallel = false;

    public ReportInformation getReportInformation() {
        return reportInformation;
    }

    public static boolean isParallel(){
        return parallel;
    }

    public File getReportDirectory() {
        return reportDirectory;
    }

    public String getBuildNumber() {
        return buildNumber;
    }

    public String getBuildProject() {
        return buildProject;
    }

    public String getPluginUrlPath() {
        return pluginUrlPath;
    }

    public boolean isRunWithJenkins() {
        return runWithJenkins;
    }

    public Map<String, String> getCustomHeader() {
        return customHeader;
    }

    public void setCustomHeader(Map<String, String> customHeader) {
        this.customHeader = customHeader;
    }

    public boolean isFlashCharts() {
        return flashCharts;
    }

    public boolean isHighCharts() {
        return highCharts;
    }

    public boolean hasTagsReportingEnabled(){
        return tagsReportingEnabled;
    }

    public static Builder newReportBuilder(){
        return new ReportBuilder.Builder();
    }

    private ReportBuilder(List<String> jsonReports, File reportDirectory, String pluginUrlPath, String buildNumber,
            String buildProject, boolean skippedFails, boolean pendingFails, boolean undefinedFails,
            boolean missingFails, boolean flashCharts, boolean runWithJenkins, boolean artifactsEnabled,
            String artifactConfig, boolean highCharts, boolean parallelTesting, boolean tagsReportingEnabled) throws IOException, VelocityException {
        try {
            this.reportDirectory = reportDirectory;
            this.buildNumber = buildNumber;
            this.buildProject = buildProject;
            this.pluginUrlPath = getPluginUrlPath(pluginUrlPath);
            this.flashCharts = flashCharts;
            this.runWithJenkins = runWithJenkins;
            this.artifactsEnabled = artifactsEnabled;
            this.highCharts = highCharts;
            this.tagsReportingEnabled = tagsReportingEnabled;
            ReportBuilder.parallel = parallelTesting;

            ConfigurationOptions configuration = ConfigurationOptions.instance();
            configuration.setSkippedFailsBuild(skippedFails);
            configuration.setPendingFailsBuild(pendingFails);
            configuration.setUndefinedFailsBuild(undefinedFails);
            configuration.setMissingFailsBuild(missingFails);
            configuration.setArtifactsEnabled(artifactsEnabled);
            if (artifactsEnabled) {
                ArtifactProcessor artifactProcessor = new ArtifactProcessor(artifactConfig);
                configuration.setArtifactConfiguration(artifactProcessor.process());
            }

            ReportParser reportParser = new ReportParser(jsonReports);
            this.reportInformation = new ReportInformation(reportParser.getFeatures(), tagsReportingEnabled);
            // whatever happens we want to provide at least error page instead of empty report
        } catch (Exception exception) {
            parsingError = true;
            generateErrorPage(exception);
        }
    }

    public boolean getBuildStatus() {
        return reportInformation.getTotalStepsFailed() == 0;
    }

    public void generateReports() throws IOException, VelocityException {
        try {
            copyResource("themes", "blue.zip");
            if (flashCharts) {
                copyResource("charts", "flash_charts.zip");
            } else {
                copyResource("charts", "js.zip");
            }
            if (artifactsEnabled) {
                copyResource("charts", "codemirror.zip");
            }

            //Added to correlate feature with each report
            setJsonFilesInFeatures();

            new FeatureOverviewPage(this).generatePage();
            new FeatureReportPage(this).generatePage();

            if(tagsReportingEnabled){
                new TagReportPage(this).generatePage();
                new TagOverviewPage(this).generatePage();
            }
            // whatever happens we want to provide at least error page instead of empty report
        } catch (Exception exception) {
            if (!parsingError) {
                generateErrorPage(exception);
            }
        }
    }

    private void setJsonFilesInFeatures() {
        for (Map.Entry<String, List<Feature>> pairs : reportInformation.getProjectFeatureMap().entrySet()) {
            List<Feature> featureList = pairs.getValue();

            for (Feature feature : featureList) {
                String jsonFile = pairs.getKey().split("/")[pairs.getKey().split("/").length - 1];
                feature.setJsonFile(jsonFile);               
            }
        }
    }

    private void copyResource(String resourceLocation, String resourceName) throws IOException, URISyntaxException {
        final File tmpResourcesArchive = File.createTempFile("temp", resourceName + ".zip");

        InputStream resourceArchiveInputStream = ReportBuilder.class.getResourceAsStream(resourceLocation + "/" + resourceName);
        if (resourceArchiveInputStream == null) {
            resourceArchiveInputStream = ReportBuilder.class.getResourceAsStream("/" + resourceLocation + "/" + resourceName);
        }
        OutputStream resourceArchiveOutputStream = new FileOutputStream(tmpResourcesArchive);
        try {
            IOUtils.copy(resourceArchiveInputStream, resourceArchiveOutputStream);
        } finally {
            IOUtils.closeQuietly(resourceArchiveInputStream);
            IOUtils.closeQuietly(resourceArchiveOutputStream);
        }
        UnzipUtils.unzipToFile(tmpResourcesArchive, reportDirectory);
        FileUtils.deleteQuietly(tmpResourcesArchive);
    }

    private String getPluginUrlPath(String path) {
        return path.isEmpty() ? "/" : path;
    }

    private void generateErrorPage(Exception exception) throws IOException {
        ErrorPage errorPage = new ErrorPage(this, exception);
        errorPage.generatePage();
        System.out.println(exception);
            }

    public static final class Builder {

        private List<String> jsonReports = new ArrayList<>();
        private File reportDirectory;
        private String pluginUrlPath = "";
        private String buildNumber = "";
        private String buildProject = "";
        private boolean skippedFails;
        private boolean pendingFails;
        private boolean undefinedFails;
        private boolean missingFails;
        private boolean flashCharts;
        private boolean runWithJenkins;
        private boolean artifactsEnabled;
        private String artifactConfig = "";
        private boolean highCharts;
        private boolean parallelTesting;
        private boolean tagsReportingEnabled = true;

        public Builder withJsonReports(List<String> jsonReports) {
            this.jsonReports = jsonReports;
            return this;
        }

        public Builder withReportOutputDirectory(File reportDirectory) {
            this.reportDirectory = reportDirectory;
            return this;
        }

        public Builder withPluginUrlPath(String pluginUrlPath) {
            this.pluginUrlPath = pluginUrlPath;
            return this;
        }

        public Builder withBuildNumber(String buildNumber) {
            this.buildNumber = buildNumber;
            return this;
        }

        public Builder withBuildProject(String buildProject) {
            this.buildProject = buildProject;
            return this;
        }

        public Builder withSkippedFails(boolean skippedFails) {
            this.skippedFails = skippedFails;
            return this;
        }

        public Builder withPendingFails(boolean pendingFails) {
            this.pendingFails = pendingFails;
            return this;
        }

        public Builder withUndefinedFails(boolean undefinedFails) {
            this.undefinedFails = undefinedFails;
            return this;
        }

        public Builder withMissingFails(boolean missingFails) {
            this.missingFails = missingFails;
            return this;
        }

        public Builder withFlashCharts(boolean flashCharts) {
            this.flashCharts = flashCharts;
            return this;
        }

        public Builder withJenkins(boolean runWithJenkins) {
            this.runWithJenkins = runWithJenkins;
            return this;
        }

        public Builder withArtifactsEnabled(boolean artifactsEnabled) {
            this.artifactsEnabled = artifactsEnabled;
            return this;
        }

        public Builder withArtifactConfig(String artifactConfig) {
            this.artifactConfig = artifactConfig;
            return this;
        }

        public Builder withHighCharts(boolean highCharts) {
            this.highCharts = highCharts;
            return this;
        }

        public Builder withParallelTesting(boolean parallelTesting) {
            this.parallelTesting = parallelTesting;
            return this;
        }

        public Builder withTagsReporting(boolean tagsReportingEnabled) {
            this.tagsReportingEnabled = tagsReportingEnabled;
            return this;
        }

        public ReportBuilder build() throws IOException {
            return new ReportBuilder(jsonReports, reportDirectory, pluginUrlPath, buildNumber,
                    buildProject, skippedFails, pendingFails, undefinedFails,
            missingFails, flashCharts, runWithJenkins, artifactsEnabled,
            artifactConfig, highCharts, parallelTesting, tagsReportingEnabled);
        }
    }
}
