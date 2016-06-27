package net.masterthought.cucumber.json;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ObjectUtils;

import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.json.support.Status;
import net.masterthought.cucumber.json.support.StatusCounter;
import net.masterthought.cucumber.reports.Reportable;
import net.masterthought.cucumber.util.Util;

public class Feature implements Reportable, Comparable<Feature> {

    // Start: attributes from JSON file report
    private final String id = null;
    private final String name = null;
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

    public String getUri() {
        return uri;
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
    public long getDurations() {
        return totalDuration;
    }

    @Override
    public String getFormattedDurations() {
        return Util.formatDuration(getDurations());
    }

    @Override
    public int getPassedScenarios() {
        return scenarioCounter.getValueFor(Status.PASSED);
    }

    @Override
    public int getFailedScenarios() {
        return scenarioCounter.getValueFor(Status.FAILED);
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
            // file name without path and extension (usually path/{jsonFile}.json)
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
            if (element.getElementStatus() != Status.PASSED) {
                featureStatus = Status.FAILED;
                return;
            }
        }
        featureStatus = Status.PASSED;
    }

    private void calculateSteps() {
        for (Element element : elements) {
            if (element.isScenario()) {
                scenarioCounter.incrementFor(element.getElementStatus());
            }

            totalSteps += element.getSteps().length;

            for (Step step : element.getSteps()) {
                statusCounter.incrementFor(step.getStatus());
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
