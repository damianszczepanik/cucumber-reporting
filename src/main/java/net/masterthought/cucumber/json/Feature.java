package net.masterthought.cucumber.json;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ArrayUtils;

import net.masterthought.cucumber.ReportBuilder;
import net.masterthought.cucumber.json.support.Status;
import net.masterthought.cucumber.json.support.StatusCounter;
import net.masterthought.cucumber.json.support.StepResults;
import net.masterthought.cucumber.util.Util;

public class Feature {

    // Start: attributes from JSON file report
    private final String id = null;
    private final String name = null;
    private final String uri = null;
    private final String description = null;
    private final String keyword = null;
    private final Scenario[] elements = new Scenario[0];
    private final Tag[] tags = new Tag[0];
    // End: attributes from JSON file report

    private String jsonFile;
    private String reportFileName;
    private String deviceName;
    private StepResults stepResults;
    private final List<Scenario> passedScenarios = new ArrayList<>();
    private final List<Scenario> failedScenarios = new ArrayList<>();
    private Status featureStatus;
    private int scenariosCount;

    public String getDeviceName() {
        return deviceName;
    }

    public String getId() {
        return id;
    }

    public Scenario[] getScenarios() {
        return elements;
    }

    public String getReportFileName() {
        return reportFileName;
    }

    public boolean hasTags() {
        return ArrayUtils.isNotEmpty(tags);
    }

    public boolean hasScenarios() {
        return ArrayUtils.isNotEmpty(elements);
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
        return StringUtils.isNotEmpty(name) ? getStatus().toHtmlClass()
                + "<div class=\"feature-line\"><span class=\"feature-keyword\">" + keyword + ":</span> " + name
                + "</div></div>" : "";
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
        return scenariosCount;
    }

    public int getNumberOfSteps() {
        return stepResults.getNumberOfSteps();
    }

    public int getNumberOfPasses() {
        return stepResults.getNumberOfPasses();
    }

    public int getNumberOfFailures() {
        return stepResults.getNumberOfFailures();
    }

    public int getNumberOfPending() {
        return stepResults.getNumberOfPending();
    }

    public int getNumberOfSkipped() {
        return stepResults.getNumberOfSkipped();
    }

    public int getNumberOfMissing() {
        return stepResults.getNumberOfMissing();
    }

    public int getNumberOfUndefined() {
        return stepResults.getNumberOfUndefined();
    }

    public String getTotalDuration() {
        return stepResults.getTotalDurationAsString();
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

        for (Scenario scenario : elements) {
            scenario.setMedaData(this);
        }

        setDeviceName();
        setReportFileName();
        calculateFeatureStatus();
        calculateScenarioCount();

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
        for (Scenario element : elements) {
            if (element.getStatus() != Status.PASSED) {
                featureStatus = Status.FAILED;
                return;
            }
        }
        featureStatus = Status.PASSED;
    }

    private void calculateScenarioCount() {
        for (Scenario element : elements) {
            if (element.isScenario()) {
                scenariosCount++;
            }
        }
    }

    public void calculateSteps() {
        List<Step> allSteps = new ArrayList<>();
        StatusCounter stepsCounter = new StatusCounter();
        long totalDuration = 0L;

        for (Scenario scenario : elements) {
            if (scenario.isScenario()) {
                if (scenario.getStatus() == Status.PASSED) {
                    passedScenarios.add(scenario);
                } else if (scenario.getStatus() == Status.FAILED) {
                    failedScenarios.add(scenario);
                }
            }

            for (Step step : scenario.getSteps()) {
                allSteps.add(step);
                stepsCounter.incrementFor(step.getStatus());
                totalDuration += step.getDuration();
            }
        }

        stepResults = new StepResults(allSteps, stepsCounter, totalDuration);
    }
}
