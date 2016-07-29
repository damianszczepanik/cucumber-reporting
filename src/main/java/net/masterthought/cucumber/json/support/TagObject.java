package net.masterthought.cucumber.json.support;

import net.masterthought.cucumber.Reportable;
import net.masterthought.cucumber.ValidationException;
import net.masterthought.cucumber.json.Element;
import net.masterthought.cucumber.json.Step;
import net.masterthought.cucumber.util.Util;
import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class TagObject implements Reportable, Comparable<TagObject> {

    private final String tagName;
    private final List<Element> elements = new ArrayList<>();

    private final String reportFileName;
    private int scenarioCounter;
    private StatusCounter elementsStatusCounter = new StatusCounter();
    private StatusCounter stepsStatusCounter = new StatusCounter();
    private long totalDuration;
    private int totalSteps;

    /** Default status for current tag: {@link Status#PASSED} if all elements pass {@link Status#FAILED} otherwise. */
    private Status status = Status.PASSED;

    public TagObject(String tagName) {
        if (StringUtils.isEmpty(tagName)) {
            throw new ValidationException("TagName cannnot be null!");
        }
        this.tagName = tagName;

        // eliminate characters that might be invalid as a file tagName
        this.reportFileName = tagName.replace("@", "").replaceAll(":", "-").trim() + ".html";
    }

    @Override
    public String getName() {
        return tagName;
    }

    public String getReportFileName() {
        return reportFileName;
    }

    public boolean addElement(Element element) {
        // don't process if this element was already linked with this tag
        if (elements.contains(element)) {
            return false;
        }

        elements.add(element);

        if (status != Status.FAILED && element.getElementStatus() != Status.PASSED) {
            status = Status.FAILED;
        }

        if (element.isScenario()) {
            scenarioCounter++;
        }

        elementsStatusCounter.incrementFor(element.getElementStatus());

        for (Step step : element.getSteps()) {
            stepsStatusCounter.incrementFor(step.getResult().getStatus());
            totalDuration += step.getDuration();
            totalSteps++;
        }
        return true;
    }

    public List<Element> getElements() {
        return elements;
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
    public long getDurations() {
        return totalDuration;
    }

    @Override
    public String getFormattedDurations() {
        return Util.formatDuration(getDurations());
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

    @Override
    public int compareTo(TagObject o) {
        // since there might be the only one TagObject with given tagName, compare by location only
        return Integer.signum(tagName.compareTo(o.getName()));
    }
}
