package net.masterthought.cucumber;

import net.masterthought.cucumber.charts.FlashChartBuilder;
import net.masterthought.cucumber.charts.JsChartUtil;
import net.masterthought.cucumber.json.Feature;
import net.masterthought.cucumber.util.UnzipUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.*;
import java.net.URISyntaxException;
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

    public ReportBuilder(List<String> jsonReports, File reportDirectory, String pluginUrlPath, String buildNumber, String buildProject, boolean skippedFails, boolean undefinedFails, boolean flashCharts, boolean runWithJenkins, boolean artifactsEnabled, String artifactConfig) throws Exception {
        ConfigurationOptions.setSkippedFailsBuild(skippedFails);
        ConfigurationOptions.setUndefinedFailsBuild(undefinedFails);
        ConfigurationOptions.setArtifactsEnabled(artifactsEnabled);
        if(artifactsEnabled){
            ArtifactProcessor artifactProcessor = new ArtifactProcessor(artifactConfig);
            ConfigurationOptions.setArtifactConfiguration(artifactProcessor.process());
        }
        ReportParser reportParser = new ReportParser(jsonReports);
        this.ri = new ReportInformation(reportParser.getFeatures());
        this.reportDirectory = reportDirectory;
        this.buildNumber = buildNumber;
        this.buildProject = buildProject;
        this.pluginUrlPath = getPluginUrlPath(pluginUrlPath);
        this.flashCharts = flashCharts;
        this.runWithJenkins = runWithJenkins;
        this.artifactsEnabled = artifactsEnabled;
    }

    public boolean getBuildStatus() {
        return !(ri.getTotalNumberFailingSteps() > 0);
    }

    public void generateReports() throws Exception {
        copyResource("themes", "blue.zip");
        if (flashCharts) {
            copyResource("charts", "flash_charts.zip");
        } else {
            copyResource("charts", "js.zip");
        }
        if(artifactsEnabled){
            copyResource("charts", "codemirror.zip");
        }
        generateFeatureOverview();
        generateFeatureReports();
        generateTagReports();
        generateTagOverview();
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
                VelocityContext context = new VelocityContext();
                context.put("feature", feature);
                context.put("report_status_colour", ri.getReportStatusColour(feature));
                context.put("build_project", buildProject);
                context.put("build_number", buildNumber);
                context.put("scenarios", feature.getElements());
                context.put("time_stamp", ri.timeStamp());
                context.put("jenkins_base", pluginUrlPath);
                context.put("fromJenkins", runWithJenkins);
                context.put("artifactsEnabled", ConfigurationOptions.artifactsEnabled());
                generateReport(feature.getFileName(), featureResult, context);
            }
        }
    }

    private void generateFeatureOverview() throws Exception {
        int numberTotalPassed = ri.getTotalNumberPassingSteps();
        int numberTotalFailed = ri.getTotalNumberFailingSteps();
        int numberTotalSkipped = ri.getTotalNumberSkippedSteps();
        int numberTotalPending = ri.getTotalNumberPendingSteps();

        VelocityEngine ve = new VelocityEngine();
        ve.init(getProperties());
        Template featureOverview = ve.getTemplate("templates/featureOverview.vm");
        VelocityContext context = new VelocityContext();
        context.put("build_project", buildProject);
        context.put("build_number", buildNumber);
        context.put("features", ri.getFeatures());
        context.put("total_features", ri.getTotalNumberOfFeatures());
        context.put("total_scenarios", ri.getTotalNumberOfScenarios());
        context.put("total_steps", ri.getTotalNumberOfSteps());
        context.put("total_passes", numberTotalPassed);
        context.put("total_fails", numberTotalFailed);
        context.put("total_skipped", numberTotalSkipped);
        context.put("total_pending", numberTotalPending);
        context.put("scenarios_passed", ri.getTotalScenariosPassed());
        context.put("scenarios_failed", ri.getTotalScenariosFailed());
        if (flashCharts) {
            context.put("step_data", FlashChartBuilder.donutChart(numberTotalPassed, numberTotalFailed, numberTotalSkipped, numberTotalPending));
            context.put("scenario_data", FlashChartBuilder.pieChart(ri.getTotalScenariosPassed(), ri.getTotalScenariosFailed()));
        } else {
            JsChartUtil pie = new JsChartUtil();
            List<String> stepColours = pie.orderStepsByValue(numberTotalPassed, numberTotalFailed, numberTotalSkipped, numberTotalPending);
            context.put("step_data", stepColours);
            List<String> scenarioColours = pie.orderScenariosByValue(ri.getTotalScenariosPassed(), ri.getTotalScenariosFailed());
            context.put("scenario_data", scenarioColours);
        }
        context.put("time_stamp", ri.timeStamp());
        context.put("total_duration", ri.getTotalDurationAsString());
        context.put("jenkins_base", pluginUrlPath);
        context.put("fromJenkins", runWithJenkins);
        context.put("flashCharts", flashCharts);
        generateReport("feature-overview.html", featureOverview, context);
    }


    public void generateTagReports() throws Exception {
        for (TagObject tagObject : ri.getTags()) {
            VelocityEngine ve = new VelocityEngine();
            ve.init(getProperties());
            Template featureResult = ve.getTemplate("templates/tagReport.vm");
            VelocityContext context = new VelocityContext();
            context.put("tag", tagObject);
            context.put("time_stamp", ri.timeStamp());
            context.put("jenkins_base", pluginUrlPath);
            context.put("build_project", buildProject);
            context.put("build_number", buildNumber);
            context.put("fromJenkins", runWithJenkins);
            context.put("report_status_colour", ri.getTagReportStatusColour(tagObject));
            generateReport(tagObject.getTagName().replace("@", "").trim() + ".html", featureResult, context);
        }
    }

    public void generateTagOverview() throws Exception {
        VelocityEngine ve = new VelocityEngine();
        ve.init(getProperties());
        Template featureOverview = ve.getTemplate("templates/tagOverview.vm");
        VelocityContext context = new VelocityContext();
        context.put("build_project", buildProject);
        context.put("build_number", buildNumber);
        context.put("tags", ri.getTags());
        context.put("total_tags", ri.getTotalTags());
        context.put("total_scenarios", ri.getTotalTagScenarios());
        context.put("total_passed_scenarios", ri.getTotalPassingTagScenarios());
        context.put("total_failed_scenarios", ri.getTotalFailingTagScenarios());
        context.put("total_steps", ri.getTotalTagSteps());
        context.put("total_passes", ri.getTotalTagPasses());
        context.put("total_fails", ri.getTotalTagFails());
        context.put("total_skipped", ri.getTotalTagSkipped());
        context.put("total_pending", ri.getTotalTagPending());
        if (flashCharts) {
            context.put("chart_data", FlashChartBuilder.StackedColumnChart(ri.tagMap));
        } else {
            context.put("chart_rows", JsChartUtil.generateTagChartData(ri.tagMap));
        }
        context.put("total_duration", ri.getTotalTagDuration());
        context.put("time_stamp", ri.timeStamp());
        context.put("jenkins_base", pluginUrlPath);
        context.put("fromJenkins", runWithJenkins);
        context.put("flashCharts", flashCharts);
        generateReport("tag-overview.html", featureOverview, context);
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
        Writer writer = new FileWriter(new File(reportDirectory, fileName));
        featureResult.merge(context, writer);
        writer.flush();
        writer.close();
    }

    private Properties getProperties() {
        Properties props = new Properties();
        props.setProperty("resource.loader", "class");
        props.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        return props;
    }


}
