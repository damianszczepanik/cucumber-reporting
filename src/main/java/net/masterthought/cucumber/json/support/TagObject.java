package net.masterthought.cucumber.json.support;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.NotImplementedException;

import net.masterthought.cucumber.json.Element;
import net.masterthought.cucumber.json.Step;
import net.masterthought.cucumber.reports.Reportable;
import net.masterthought.cucumber.util.Util;

public class TagObject implements Reportable {

    private final String tagName;
    private final List<Element> elements = new ArrayList<>();

    private final String reportFileName;
    private int scenarioCounter;
    private StatusCounter elementsStatusCounter = new StatusCounter();
    private StatusCounter stepsStatusCounter = new StatusCounter();
    private long totalDuration;
    private int totalSteps;

    /** Status for current tag: {@link Status#PASSED} if all elements pass {@link Status#FAILED} otherwise. */
    private Status status = Status.PASSED;

    public TagObject(String tagName) {
        this.tagName = tagName;

        // eliminate characters that might be invalid as a file name
        this.reportFileName = tagName.replace("@", "").replaceAll(":", "-").trim() + ".html";
    }

    @Override
    public String getName() {
        return tagName;
    }

    public String getReportFileName() {
        return reportFileName;
    }

    public List<Element> getElements() {
        return elements;
    }

    public boolean addElement(Element element) {
        // don't process if this element was already linked with this tag
        if (elements.contains(element)) {
            return false;
        }

        elements.add(element);

        if (status != Status.FAILED && element.getStatus() != Status.PASSED) {
            status = Status.FAILED;
        }

        if (element.isScenario()) {
            scenarioCounter++;
        }

        elementsStatusCounter.incrementFor(element.getStatus());

        for (Step step : element.getSteps()) {
            stepsStatusCounter.incrementFor(step.getStatus());
            totalDuration += step.getDuration();
            totalSteps++;
        }
        return true;
    }

    @Override
    public int getScenarios() {
        return scenarioCounter;
    }

    @Override
    public int getPassedScenarios() {
        return elementsStatusCounter.getValueFor(Status.PASSED);
    }

    @Override
    public int getFailedScenarios() {
        return elementsStatusCounter.getValueFor(Status.FAILED);
    }

    @Override
    public String getDurations() {
        return Util.formatDuration(totalDuration);
    }

    @Override
    public int getSteps() {
        return totalSteps;
    }

    public int getNumberOfStatus(Status status) {
        return stepsStatusCounter.getValueFor(status);
    }

    @Override
    public int getPassedSteps() {
        return getNumberOfStatus(Status.PASSED);
    }

    @Override
    public int getFailedSteps() {
        return getNumberOfStatus(Status.FAILED);
    }

    @Override
    public int getSkippedSteps() {
        return getNumberOfStatus(Status.SKIPPED);
    }

    @Override
    public int getUndefinedSteps() {
        return getNumberOfStatus(Status.UNDEFINED);
    }

    @Override
    public int getMissingSteps() {
        return getNumberOfStatus(Status.MISSING);
    }

    @Override
    public int getPendingSteps() {
        return getNumberOfStatus(Status.PENDING);
    }

    @Override
    public Status getStatus() {
        return status;
    }

    public String getRawStatus() {
        return status.name().toLowerCase();
    }

    @Override
    public String getDeviceName() {
        throw new NotImplementedException();
    }
}
