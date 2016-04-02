package net.masterthought.cucumber.json;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ArrayUtils;

import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.json.support.Status;
import net.masterthought.cucumber.json.support.StatusCounter;
import net.masterthought.cucumber.reports.Reportable;
import net.masterthought.cucumber.util.Util;

public class Feature implements Reportable {

    // Start: attributes from JSON file report
    private final String id = null;
    private final String name = null;
    private final String uri = null;
    private final String description = null;
    private final String keyword = null;

    private final Element[] elements = new Element[0];
    private final Tag[] tags = new Tag[0];
    // End: attributes from JSON file report

    private static final String UNKNOWN_NAME = "unknown";

    private String jsonFile;
    private String reportFileName;
    private String deviceName;
    private final List<Element> scenarios = new ArrayList<>();
    private final StatusCounter scenarioCounter = new StatusCounter();
    private Status featureStatus;
    private final StatusCounter statusCounter = new StatusCounter();
    private long totalDuration;
    private int totalSteps;

    @Override
    public String getDeviceName() {
        return deviceName;
    }

    public String getId() {
        return id;
    }

    public Element[] getElements() {
        return elements;
    }

    public String getReportFileName() {
        return reportFileName;
    }

    public boolean hasTags() {
        return ArrayUtils.isNotEmpty(tags);
    }

    public Tag[] getTags() {
        return tags;
    }

    @Override
    public Status getStatus() {
        return featureStatus;
    }

    public String getName() {
        return StringUtils.defaultString(name);
    }

    public String getKeyword() {
        return StringUtils.defaultString(keyword);
    }

    @Override
    public String getRawName() {
        return StringUtils.isNotEmpty(name) ? StringEscapeUtils.escapeHtml(name) : UNKNOWN_NAME;
    }

    public String getRawStatus() {
        return getStatus().toString().toLowerCase();
    }

    public String getDescription() {
        return StringUtils.defaultString(description);
    }

    @Override
    public int getScenarios() {
        return scenarios.size();
    }

    @Override
    public int getSteps() {
        return totalSteps;
    }

    @Override
    public int getPassedSteps() {
        return statusCounter.getValueFor(Status.PASSED);
    }

    @Override
    public int getFailedSteps() {
        return statusCounter.getValueFor(Status.FAILED);
    }

    @Override
    public int getPendingSteps() {
        return statusCounter.getValueFor(Status.PENDING);
    }

    @Override
    public int getSkippedSteps() {
        return statusCounter.getValueFor(Status.SKIPPED);
    }

    @Override
    public int getMissingSteps() {
        return statusCounter.getValueFor(Status.MISSING);
    }

    @Override
    public int getUndefinedSteps() {
        return statusCounter.getValueFor(Status.UNDEFINED);
    }

    @Override
    public String getDurations() {
        return Util.formatDuration(totalDuration);
    }

    @Override
    public int getPassedScenarios() {
        return scenarioCounter.getValueFor(Status.PASSED);
    }

    @Override
    public int getFailedScenarios() {
        return scenarioCounter.getValueFor(Status.FAILED);
    }

    /** Sets additional information and calculates values which should be calculated during object creation. */
    public void setMetaData(String jsonFile, int jsonFileNo, Configuration configuration) {
        this.jsonFile = StringUtils.substringAfterLast(jsonFile, "/");

        for (Element element : elements) {
            element.setMedaData(this, configuration);

            if (element.isScenario()) {
                scenarios.add(element);
            }
        }

        setDeviceName();
        setReportFileName(jsonFileNo, configuration);
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

    private void setReportFileName(int jsonFileNo, Configuration configuration) {
        // remove all characters that might not be valid file name
        reportFileName = Util.toValidFileName(uri);

        // If we expect to have parallel executions, we add postfix to file name
        if (configuration.isParallelTesting()) {
            reportFileName += "_" + getDeviceName();
        }

        // if there is only one JSON file - skip unique prefix
        if (jsonFileNo > 0) {
            // add jsonFile index to the file name so if two the same features are reported
            // in two different JSON files then file name must be different
            reportFileName += "_" + jsonFileNo;
        }

        reportFileName += ".html";
    }

    private void calculateFeatureStatus() {
        for (Element element : elements) {
            if (element.getStatus() != Status.PASSED) {
                featureStatus = Status.FAILED;
                return;
            }
        }
        featureStatus = Status.PASSED;
    }

    private void calculateSteps() {
        for (Element element : elements) {
            if (element.isScenario()) {
                scenarioCounter.incrementFor(element.getStatus());
            }

            totalSteps += element.getSteps().length;

            for (Step step : element.getSteps()) {
                statusCounter.incrementFor(step.getStatus());
                totalDuration += step.getDuration();
            }
        }
    }
}
