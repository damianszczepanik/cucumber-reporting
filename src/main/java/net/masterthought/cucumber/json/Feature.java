package net.masterthought.cucumber.json;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ObjectUtils;

import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.Reportable;
import net.masterthought.cucumber.json.support.Status;
import net.masterthought.cucumber.json.support.StatusCounter;
import net.masterthought.cucumber.util.Util;

public class Feature implements Reportable, Comparable<Feature> {

    // Start: attributes from JSON file report
    private final String id = null;
    private final String name = null;
    // as long as this is private attribute without getter deserialization must be forced by annotation
    @JsonProperty("uri")
    private final String uri = null;
    private final String description = null;
    private final String keyword = null;

    private final Element[] elements = new Element[0];
    private final Tag[] tags = new Tag[0];
    // End: attributes from JSON file report

    private String jsonFile;
    private String reportFileName;
    private String deviceName;
    private final List<Element> scenarios = new ArrayList<>();
    private final StatusCounter elementsCounter = new StatusCounter();
    private final StatusCounter stepsCounter = new StatusCounter();

    private Status featureStatus;
    private long totalDuration;

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

    public Tag[] getTags() {
        return tags;
    }

    @Override
    public Status getStatus() {
        return featureStatus;
    }

    @Override
    public String getName() {
        return StringUtils.defaultString(name);
    }

    public String getKeyword() {
        return StringUtils.defaultString(keyword);
    }

    public String getDescription() {
        return StringUtils.defaultString(description);
    }

    @Override
    public int getFeatures() {
        return 1;
    }

    @Override
    public int getPassedFeatures() {
        return getStatus().isPassed() ? 1 : 0;
    }

    @Override
    public int getFailedFeatures() {
        return getStatus().isPassed() ? 0 : 1;
    }

    @Override
    public int getScenarios() {
        return scenarios.size();
    }

    @Override
    public int getSteps() {
        return stepsCounter.size();
    }

    @Override
    public int getPassedSteps() {
        return stepsCounter.getValueFor(Status.PASSED);
    }

    @Override
    public int getFailedSteps() {
        return stepsCounter.getValueFor(Status.FAILED);
    }

    @Override
    public int getPendingSteps() {
        return stepsCounter.getValueFor(Status.PENDING);
    }

    @Override
    public int getSkippedSteps() {
        return stepsCounter.getValueFor(Status.SKIPPED);
    }

    @Override
    public int getUndefinedSteps() {
        return stepsCounter.getValueFor(Status.UNDEFINED);
    }

    @Override
    public long getDurations() {
        return totalDuration;
    }

    @Override
    public String getFormattedDurations() {
        return Util.formatDuration(getDurations());
    }

    @Override
    public int getPassedScenarios() {
        return elementsCounter.getValueFor(Status.PASSED);
    }

    @Override
    public int getFailedScenarios() {
        return elementsCounter.getValueFor(Status.FAILED);
    }

    public String getJsonFile() {
        return jsonFile;
    }

    /**
     * Sets additional information and calculates values which should be calculated during object creation.
     */
    public void setMetaData(String jsonFile, int jsonFileNo, Configuration configuration) {
        this.jsonFile = jsonFile;

        for (Element element : elements) {
            element.setMetaData(this);

            if (element.isScenario()) {
                scenarios.add(element);
            }
        }

        deviceName = calculateDeviceName();
        calculateReportFileName(jsonFileNo, configuration);
        featureStatus = calculateFeatureStatus();

        calculateSteps();
    }

    private String calculateDeviceName() {
        String[] splitJsonFile = jsonFile.split("[^\\d\\w]");
        // it should have at least two parts: file name and its extension (.json)
        if (splitJsonFile.length > 1) {
            // file name without path and extension (usually path/jsonFile.json)
            return splitJsonFile[splitJsonFile.length - 2];
        } else {
            // path name without special characters
            return splitJsonFile[0];
        }
    }

    private void calculateReportFileName(int jsonFileNo, Configuration configuration) {
        // remove all characters that might not be valid file name
        reportFileName = "report-feature_" + Util.toValidFileName(uri);

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

    private Status calculateFeatureStatus() {
        StatusCounter statusCounter = new StatusCounter();
        for (Element element : elements) {
            statusCounter.incrementFor(element.getStatus());
        }
        return statusCounter.getFinalStatus();
    }

    private void calculateSteps() {
        for (Element element : elements) {
            if (element.isScenario()) {
                elementsCounter.incrementFor(element.getStatus());
            }

            for (Step step : element.getSteps()) {
                stepsCounter.incrementFor(step.getResult().getStatus());
                totalDuration += step.getDuration();
            }
        }
    }

    @Override
    public int compareTo(Feature feature) {
        // order by the name so first compare by the name
        int nameCompare = ObjectUtils.compare(name, feature.getName());
        if (nameCompare != 0) {
            return nameCompare;
        }

        // if names are the same, compare by the ID which should be unieque by JSON file
        int idCompare = ObjectUtils.compare(id, feature.getId());
        if (idCompare != 0) {
            return idCompare;
        }

        // if ids are the same it means that feature exists in more than one JSON file so compare by JSON report
        return ObjectUtils.compare(jsonFile, feature.getJsonFile());
    }
}
