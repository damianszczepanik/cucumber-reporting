package net.masterthought.cucumber.json;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ArrayUtils;

import com.google.gson.annotations.SerializedName;

import net.masterthought.cucumber.ReportBuilder;
import net.masterthought.cucumber.json.support.Status;
import net.masterthought.cucumber.json.support.StatusCounter;
import net.masterthought.cucumber.util.Util;

public class Feature {

    // Start: attributes from JSON file report
    private final String id = null;
    private final String name = null;
    private final String uri = null;
    private final String description = null;
    private final String keyword = null;

    @SerializedName("elements")
    private final Scenario[] scenarios = new Scenario[0];
    private final Tag[] tags = new Tag[0];
    // End: attributes from JSON file report

    private String jsonFile;
    private String reportFileName;
    private String deviceName;
    private final List<Scenario> passedScenarios = new ArrayList<>();
    private final List<Scenario> failedScenarios = new ArrayList<>();
    private Status featureStatus;
    private final StatusCounter statusCounter = new StatusCounter();
    private long totalDuration;
    private int totalSteps;

    public String getDeviceName() {
        return deviceName;
    }

    public String getId() {
        return id;
    }

    public Scenario[] getScenarios() {
        return scenarios;
    }

    public String getReportFileName() {
        return reportFileName;
    }

    public boolean hasTags() {
        return ArrayUtils.isNotEmpty(tags);
    }

    public boolean hasScenarios() {
        return ArrayUtils.isNotEmpty(scenarios);
    }

    public Tag[] getTags() {
        return tags;
    }

    public String getTagsList() {
        return Util.tagsToHtml(tags);
    }

    public Status getStatus() {
        return featureStatus;
    }

    public String getName() {
        if (StringUtils.isNotEmpty(name)) {
            return String.format(
                    "<div class=\"%s\"><div class=\"feature-line\"><span class=\"feature-keyword\">%s: </span>%s</div></div>",
                    getStatus().getName().toLowerCase(), keyword, name);
        } else {
            return "";
        }
    }

    public String getRawName() {
        return StringUtils.isNotEmpty(name) ? StringEscapeUtils.escapeHtml(name) : "";
    }

    public String getRawStatus() {
        return getStatus().toString().toLowerCase();
    }

    public String getDescription() {
        return description;
    }

    public int getNumberOfScenarios() {
        return scenarios.length;
    }

    public int getNumberOfSteps() {
        return totalSteps;
    }

    public int getNumberOfPasses() {
        return statusCounter.getValueFor(Status.PASSED);
    }

    public int getNumberOfFailures() {
        return statusCounter.getValueFor(Status.FAILED);
    }

    public int getNumberOfPending() {
        return statusCounter.getValueFor(Status.PENDING);
    }

    public int getNumberOfSkipped() {
        return statusCounter.getValueFor(Status.SKIPPED);
    }

    public int getNumberOfMissing() {
        return statusCounter.getValueFor(Status.MISSING);
    }

    public int getNumberOfUndefined() {
        return statusCounter.getValueFor(Status.UNDEFINED);
    }

    public String getTotalDuration() {
        return Util.formatDuration(totalDuration);
    }

    public int getNumberOfScenariosPassed() {
        return passedScenarios.size();
    }

    public int getNumberOfScenariosFailed() {
        return failedScenarios.size();
    }

    /** Sets additional information and calculates values which should be calculated during object creation. */
    public void setMetaData(String jsonFile) {
        this.jsonFile = StringUtils.substringAfterLast(jsonFile, "/");

        for (Scenario scenario : scenarios) {
            scenario.setMedaData(this);
        }

        setDeviceName();
        setReportFileName();
        calculateFeatureStatus();

        calculateSteps();
    }

    private void setDeviceName() {
        String[] splitedJsonFile = jsonFile.split("[^\\d\\w]");
        if (splitedJsonFile.length > 1) {
            // file name without path and extension (usually path/{jsonfIle}.json)
            deviceName = splitedJsonFile[splitedJsonFile.length - 2];
        } else {
            // path name without special characters
            deviceName = splitedJsonFile[0];
        }
    }

    private void setReportFileName() {
        // remove all characters that might not be valid file name
        reportFileName = uri.replaceAll("[^\\d\\w]", "-");

        // If we expect to have parallel executions, we add postfix to file name
        if (ReportBuilder.isParallel()) {
            reportFileName = reportFileName + "-" + getDeviceName();
        }
        reportFileName = reportFileName + ".html";
    }

    private void calculateFeatureStatus() {
        for (Scenario element : scenarios) {
            if (element.getStatus() != Status.PASSED) {
                featureStatus = Status.FAILED;
                return;
            }
        }
        featureStatus = Status.PASSED;
    }

    private void calculateSteps() {
        for (Scenario scenario : scenarios) {
            if (scenario.getStatus() == Status.PASSED) {
                passedScenarios.add(scenario);
            } else if (scenario.getStatus() == Status.FAILED) {
                failedScenarios.add(scenario);
            }

            totalSteps += scenario.getSteps().length;

            for (Step step : scenario.getSteps()) {
                statusCounter.incrementFor(step.getStatus());
                totalDuration += step.getDuration();
            }
        }
    }

}
