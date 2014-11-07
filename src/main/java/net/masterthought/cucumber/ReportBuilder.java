package net.masterthought.cucumber;

import net.masterthought.cucumber.charts.FlashChartBuilder;
import net.masterthought.cucumber.charts.JsChartUtil;
import net.masterthought.cucumber.json.Feature;
import net.masterthought.cucumber.util.UnzipUtils;
import net.masterthought.cucumber.util.Util;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.tools.generic.EscapeTool;

import java.io.*;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ReportBuilder {

    ReportInformation ri;
    private File reportDirectory;
    private String buildNumber;
    private String buildProject;
    private String pluginUrlPath;
    private boolean flashCharts;
    private boolean runWithJenkins;
    private boolean artifactsEnabled;
    private boolean highCharts;
    private boolean parsingError;

    //Added to control parallel reports
    private static boolean parallel = false;

    public static boolean isParallel(){
        return parallel;
    }

    public static void setParallel(boolean p){
       parallel = p;
    }

    public Map<String, String> getCustomHeader() {
        return customHeader;
    }

    public void setCustomHeader(Map<String, String> customHeader) {
        this.customHeader = customHeader;
    }

    private Map<String, String> customHeader;

    private final String VERSION = "cucumber-reporting-0.0.23";

    public ReportBuilder(List<String> jsonReports, File reportDirectory, String pluginUrlPath, String buildNumber, String buildProject, boolean skippedFails, boolean undefinedFails, boolean flashCharts, boolean runWithJenkins, boolean artifactsEnabled, String artifactConfig, boolean highCharts) throws Exception {

        try {
            this.reportDirectory = reportDirectory;
            this.buildNumber = buildNumber;
            this.buildProject = buildProject;
            this.pluginUrlPath = getPluginUrlPath(pluginUrlPath);
            this.flashCharts = flashCharts;
            this.runWithJenkins = runWithJenkins;
            this.artifactsEnabled = artifactsEnabled;
            this.highCharts = highCharts;
            this.parallel = false;

            ConfigurationOptions.setSkippedFailsBuild(skippedFails);
            ConfigurationOptions.setUndefinedFailsBuild(undefinedFails);
            ConfigurationOptions.setArtifactsEnabled(artifactsEnabled);
            if (artifactsEnabled) {
                ArtifactProcessor artifactProcessor = new ArtifactProcessor(artifactConfig);
                ConfigurationOptions.setArtifactConfiguration(artifactProcessor.process());
            }

            ReportParser reportParser = new ReportParser(jsonReports);
            this.ri = new ReportInformation(reportParser.getFeatures());
        } catch (Exception exception) {
            parsingError = true;
            generateErrorPage(exception);
            System.out.println(exception);
        }
    }

    public ReportBuilder(List<String> jsonReports, File reportDirectory, String pluginUrlPath, String buildNumber, String buildProject, boolean skippedFails, boolean undefinedFails, boolean flashCharts, boolean runWithJenkins, boolean artifactsEnabled, String artifactConfig, boolean highCharts, boolean parallelTesting) throws Exception {

        try {
            this.reportDirectory = reportDirectory;
            this.buildNumber = buildNumber;
            this.buildProject = buildProject;
            this.pluginUrlPath = getPluginUrlPath(pluginUrlPath);
            this.flashCharts = flashCharts;
            this.runWithJenkins = runWithJenkins;
            this.artifactsEnabled = artifactsEnabled;
            this.highCharts = highCharts;
            this.parallel = parallelTesting;

            ConfigurationOptions.setSkippedFailsBuild(skippedFails);
            ConfigurationOptions.setUndefinedFailsBuild(undefinedFails);
            ConfigurationOptions.setArtifactsEnabled(artifactsEnabled);
            if (artifactsEnabled) {
                ArtifactProcessor artifactProcessor = new ArtifactProcessor(artifactConfig);
                ConfigurationOptions.setArtifactConfiguration(artifactProcessor.process());
            }

            ReportParser reportParser = new ReportParser(jsonReports);
            this.ri = new ReportInformation(reportParser.getFeatures());

        } catch (Exception exception) {
            parsingError = true;
            generateErrorPage(exception);
            System.out.println(exception);
        }
    }

    public boolean getBuildStatus() {
        return !(ri.getTotalNumberFailingSteps() > 0);
    }

    public void generateReports() throws Exception {
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

            generateFeatureOverview();
            generateFeatureReports();
            generateTagReports();
            generateTagOverview();
        } catch (Exception exception) {
            if (!parsingError) {
                generateErrorPage(exception);
                System.out.println(exception);
                exception.printStackTrace();
            }
        }
    }

    private void setJsonFilesInFeatures() throws Exception {
        Iterator it = ri.getProjectFeatureMap().entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry) it.next();
            List<Feature> featureList = (List<Feature>) pairs.getValue();

            for (Feature feature : featureList) {
                String jsonFile = ((String) pairs.getKey()).split("/")[((String) pairs.getKey()).split("/").length-1];
                feature.setJsonFile(jsonFile);               
            }
        }
    }


    public void generateFeatureReports() throws Exception {
        Iterator it = ri.getProjectFeatureMap().entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry) it.next();
            List<Feature> featureList = (List<Feature>) pairs.getValue();

            for (Feature feature : featureList) {
                VelocityEngine ve = new VelocityEngine();
                ve.init(getProperties());
                Template featureResult = ve.getTemplate("templates/featureReport.vm");
                VelocityContextMap contextMap = VelocityContextMap.of(new VelocityContext());
                contextMap.putAll(getGeneralParameters());
                contextMap.put("parallel", ReportBuilder.isParallel());
                contextMap.put("feature", feature);
                contextMap.put("report_status_colour", ri.getReportStatusColour(feature));
                contextMap.put("scenarios", feature.getElements().toList());
                contextMap.put("time_stamp", ri.timeStamp());
                contextMap.put("artifactsEnabled", ConfigurationOptions.artifactsEnabled());
                contextMap.put("esc", new EscapeTool());                        
                generateReport(feature.getFileName(), featureResult, contextMap.getVelocityContext());
            }
        }
    }

    private void generateFeatureOverview() throws Exception {
        VelocityEngine ve = new VelocityEngine();
        ve.init(getProperties());
        Template featureOverview = ve.getTemplate("templates/featureOverview.vm");
        VelocityContextMap contextMap = VelocityContextMap.of(new VelocityContext());
        contextMap.putAll(getGeneralParameters());
        contextMap.put("features", ri.getFeatures());
        contextMap.put("parallel", ReportBuilder.isParallel());
        contextMap.put("total_features", ri.getTotalNumberOfFeatures());
        contextMap.put("total_scenarios", ri.getTotalNumberOfScenarios());
        contextMap.put("total_steps", ri.getTotalNumberOfSteps());
        contextMap.put("total_passes", ri.getTotalNumberPassingSteps());
        contextMap.put("total_fails", ri.getTotalNumberFailingSteps());
        contextMap.put("total_skipped", ri.getTotalNumberSkippedSteps());
        contextMap.put("total_pending", ri.getTotalNumberPendingSteps());
        contextMap.put("scenarios_passed", ri.getTotalScenariosPassed());
        contextMap.put("scenarios_failed", ri.getTotalScenariosFailed());
        if (flashCharts) {
            contextMap.put("step_data", FlashChartBuilder.donutChart(ri.getTotalNumberPassingSteps(), ri.getTotalNumberFailingSteps(), ri.getTotalNumberSkippedSteps(), ri.getTotalNumberPendingSteps()));
            contextMap.put("scenario_data", FlashChartBuilder.pieChart(ri.getTotalScenariosPassed(), ri.getTotalScenariosFailed()));
        } else {
            JsChartUtil pie = new JsChartUtil();
            List<String> stepColours = pie.orderStepsByValue(ri.getTotalNumberPassingSteps(), ri.getTotalNumberFailingSteps(), ri.getTotalNumberSkippedSteps(), ri.getTotalNumberPendingSteps());
            contextMap.put("step_data", stepColours);
            List<String> scenarioColours = pie.orderScenariosByValue(ri.getTotalScenariosPassed(), ri.getTotalScenariosFailed());
            contextMap.put("scenario_data", scenarioColours);
        }
        contextMap.put("time_stamp", ri.timeStamp());
        contextMap.put("total_duration", ri.getTotalDurationAsString());
        contextMap.put("flashCharts", flashCharts);
        contextMap.put("highCharts", highCharts);
        generateReport("feature-overview.html", featureOverview, contextMap.getVelocityContext());
    }


    public void generateTagReports() throws Exception {
        for (TagObject tagObject : ri.getTags()) {
            VelocityEngine ve = new VelocityEngine();
            ve.init(getProperties());
            Template featureResult = ve.getTemplate("templates/tagReport.vm");
            VelocityContextMap contextMap = VelocityContextMap.of(new VelocityContext());
            contextMap.putAll(getGeneralParameters());
            contextMap.put("tag", tagObject);
            contextMap.put("time_stamp", ri.timeStamp());
            contextMap.put("report_status_colour", ri.getTagReportStatusColour(tagObject));
            generateReport(tagObject.getTagName().replace("@", "").trim() + ".html", featureResult, contextMap.getVelocityContext());
            contextMap.put("hasCustomHeader", false);
            if (customHeader != null && customHeader.get(tagObject.getTagName()) != null) {
                contextMap.put("hasCustomHeader", true);
                contextMap.put("customHeader", customHeader.get(tagObject.getTagName()));
            }
        }
    }

    public void generateTagOverview() throws Exception {
        VelocityEngine ve = new VelocityEngine();
        ve.init(getProperties());
        Template featureOverview = ve.getTemplate("templates/tagOverview.vm");
        VelocityContextMap contextMap = VelocityContextMap.of(new VelocityContext());
        contextMap.putAll(getGeneralParameters());
        contextMap.put("tags", ri.getTags());
        contextMap.put("total_tags", ri.getTotalTags());
        contextMap.put("total_scenarios", ri.getTotalTagScenarios());
        contextMap.put("total_passed_scenarios", ri.getTotalPassingTagScenarios());
        contextMap.put("total_failed_scenarios", ri.getTotalFailingTagScenarios());
        contextMap.put("total_steps", ri.getTotalTagSteps());
        contextMap.put("total_passes", ri.getTotalTagPasses());
        contextMap.put("total_fails", ri.getTotalTagFails());
        contextMap.put("total_skipped", ri.getTotalTagSkipped());
        contextMap.put("total_pending", ri.getTotalTagPending());
        contextMap.put("hasCustomHeaders", false);
        if (customHeader != null) {
            contextMap.put("hasCustomHeaders", true);
            contextMap.put("customHeaders", customHeader);
        }
        contextMap.put("backgrounds", ri.getBackgroundInfo());
        if (flashCharts) {
            contextMap.put("chart_data", FlashChartBuilder.StackedColumnChart(ri.tagMap));
        } else {
            if (highCharts) {
                contextMap.put("chart_categories", JsChartUtil.getTags(ri.tagMap));
                contextMap.put("chart_data", JsChartUtil.generateTagChartDataForHighCharts(ri.tagMap));
            } else {
                contextMap.put("chart_rows", JsChartUtil.generateTagChartData(ri.tagMap));
            }
        }
        contextMap.put("total_duration", ri.getTotalTagDuration());
        contextMap.put("time_stamp", ri.timeStamp());
        contextMap.put("flashCharts", flashCharts);
        contextMap.put("highCharts", highCharts);
        long durationl = ri.getBackgroundInfo().getTotalDuration() + ri.getLongTotalTagDuration();
        String duration = Util.formatDuration(durationl);
        contextMap.put("total_duration", duration);
        generateReport("tag-overview.html", featureOverview, contextMap.getVelocityContext());
    }

    public void generateErrorPage(Exception exception) throws Exception {
        VelocityEngine ve = new VelocityEngine();
        ve.init(getProperties());
        Template errorPage = ve.getTemplate("templates/errorPage.vm");
        VelocityContextMap contextMap = VelocityContextMap.of(new VelocityContext());
        contextMap.putAll(getGeneralParameters());
        contextMap.put("error_message", exception);
        contextMap.put("time_stamp", new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()));
        generateReport("feature-overview.html", errorPage, contextMap.getVelocityContext());
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

    private void generateReport(String fileName, Template featureResult, VelocityContext context) throws Exception {
        FileOutputStream fileStream = new FileOutputStream(new File(reportDirectory, fileName));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fileStream, "UTF-8"));
        featureResult.merge(context, writer);
        writer.flush();
        writer.close();
    }

    private Properties getProperties() {
        Properties props = new Properties();
        props.setProperty("resource.loader", "class");
        props.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        props.setProperty("runtime.log", new File(reportDirectory, "velocity.log").getPath());
        return props;
    }

    private HashMap<String, Object> getGeneralParameters() {
        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("version", VERSION);
        result.put("fromJenkins", runWithJenkins);
        result.put("jenkins_base", pluginUrlPath);
        result.put("build_project", buildProject);
        result.put("build_number", buildNumber);
        return result;
    }
}
