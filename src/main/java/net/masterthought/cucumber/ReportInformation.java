package net.masterthought.cucumber;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.masterthought.cucumber.json.Feature;
import net.masterthought.cucumber.json.Match;
import net.masterthought.cucumber.json.Result;
import net.masterthought.cucumber.json.Scenario;
import net.masterthought.cucumber.json.Step;
import net.masterthought.cucumber.json.Tag;
import net.masterthought.cucumber.json.support.ResultsWithMatch;
import net.masterthought.cucumber.json.support.Status;
import net.masterthought.cucumber.json.support.StatusCounter;
import net.masterthought.cucumber.json.support.StepObject;
import net.masterthought.cucumber.json.support.TagObject;
import net.masterthought.cucumber.util.Util;

public class ReportInformation {

    private final List<Feature> features;

    private long totalDuration;
    private long totalTagDuration;

    private int totalTagSteps;

    private final Map<String, TagObject> allTags = new TreeMap<>();
    private final Map<String, StepObject> stepObjects = new HashMap<>();

    private final StatusCounter tagStatusCounter = new StatusCounter();
    private final StatusCounter tagCounter = new StatusCounter();
    private final StatusCounter scenarioCounter = new StatusCounter();
    private final StatusCounter stepStatusCounter = new StatusCounter();

    private Background backgroundInfo = new Background();

    public ReportInformation(List<Feature> features) {
        this.features = features;

        processFeatures();
    }

    public List<Feature> getFeatures() {
        return this.features;
    }

    public List<TagObject> getTags() {
        return new ArrayList<>(allTags.values());
    }

    public Map<String, StepObject> getStepObject() {
        return stepObjects;
    }

    public int getTotalScenarios() {
        return scenarioCounter.size();
    }

    public int getTotalSteps() {
        return stepStatusCounter.size();
    }

    public int getTotalStepsPassed() {
        return stepStatusCounter.getValueFor(Status.PASSED);
    }

    public int getTotalStepsFailed() {
        return stepStatusCounter.getValueFor(Status.FAILED);
    }

    public int getTotalStepsSkipped() {
        return stepStatusCounter.getValueFor(Status.SKIPPED);
    }

    public int getTotalStepsPending() {
        return stepStatusCounter.getValueFor(Status.PENDING);
    }

    public int getTotalStepsMissing() {
        return stepStatusCounter.getValueFor(Status.MISSING);
    }

    public int getTotalStepsUndefined() {
        return stepStatusCounter.getValueFor(Status.UNDEFINED);
    }

    public String getTotalDurationAsString() {
        return Util.formatDuration(totalDuration);
    }

    public Long getTotalDuration() {
        return totalDuration;
    }

    public String timeStamp() {
        return new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date());
    }

    public int getTotalTagScenarios() {
        return tagStatusCounter.size();
    }

    public int getTotalTagScenariosPassed() {
        return tagCounter.getValueFor(Status.PASSED);
    }

    public int getTotalTagScenariosFailed() {
        return tagCounter.getValueFor(Status.FAILED);
    }

    public int getTotalTagSteps() {
        return totalTagSteps;
    }

    public int getTotalTagPasses() {
        return tagStatusCounter.getValueFor(Status.PASSED);
    }

    public int getTotalTagFails() {
        return tagStatusCounter.getValueFor(Status.FAILED);
    }

    public int getTotalTagSkipped() {
        return tagStatusCounter.getValueFor(Status.SKIPPED);
    }

    public int getTotalTagPending() {
        return tagStatusCounter.getValueFor(Status.PENDING);
    }

    public int getTotalTagUndefined() {
        return tagStatusCounter.getValueFor(Status.UNDEFINED);
    }

    public int getTotalTagMissing() {
        return tagStatusCounter.getValueFor(Status.MISSING);
    }

    public long getTotalTagDuration() {
        return totalTagDuration;
    }

    public int getTotalScenariosPassed() {
        return this.scenarioCounter.getValueFor(Status.PASSED);
    }

    public int getTotalScenariosFailed() {
        return this.scenarioCounter.getValueFor(Status.FAILED);
    }

    private void processTag(TagObject tag, Scenario scenario) {
        tag.addScenarios(scenario);
        tagStatusCounter.incrementFor(tag.getStatus());

        Step[] steps = scenario.getSteps();
        for (Step step : steps) {
            totalTagDuration += step.getDuration();
        }
        totalTagSteps += steps.length;
    }

    private void processFeatures() {
        for (Feature feature : features) {

            for (Scenario scenario : feature.getScenarios()) {
                scenarioCounter.incrementFor(scenario.getStatus());

                if (scenario.isBackground()) {
                    updateBackgroundInfo(scenario);
                }

                for (Tag tag : scenario.getTags()) {
                    tagCounter.incrementFor(scenario.getStatus());

                    TagObject tagObject = addTagObject(tag.getName());
                    processTag(tagObject, scenario);
                }

                Step[] steps = scenario.getSteps();
                for (Step step : steps) {
                    stepStatusCounter.incrementFor(step.getStatus());
                    totalDuration += step.getDuration();
                }
                countSteps(steps);

                countSteps(scenario.getBefore());
                countSteps(scenario.getAfter());
            }
        }
    }

    private void countSteps(ResultsWithMatch[] steps) {
        for (ResultsWithMatch step : steps) {

            String methodName = null;
            Match match = step.getMatch();
            // no match = could not find method that was matched to this step -> status is missing
            if (match != null) {
                methodName = match.getLocation();
            }
            StepObject stepObject = stepObjects.get(methodName);
            // if first occurrence of this location add element to the map
            if (stepObject == null) {
                stepObject = new StepObject(methodName);
            }
            // happens that report is not valid - does not contain information about result
            Result result = step.getResult();
            if (result != null) {
                stepObject.addDuration(result.getDuration(), result.getStatus());
            } else {
                // when result is not available it means that something really went wrong (report is incomplete)
                // and for this case FAILED status is used to avoid problems during parsing
                stepObject.addDuration(0, Status.FAILED.name());
            }
            stepObjects.put(methodName, stepObject);
        }
    }

    private void updateBackgroundInfo(Scenario scenario) {
        backgroundInfo.incrTotalScenarios();
        if (scenario.getStatus() == Status.PASSED) {
            backgroundInfo.incPassedScenarios();
        } else {
            backgroundInfo.incFailedScenarios();
        }

        backgroundInfo.addTotalSteps(scenario.getSteps().length);
        for (Step step : scenario.getSteps()) {
            backgroundInfo.incrTotalDurationBy(step.getDuration());
            backgroundInfo.incrStepCounterFor(step.getStatus());
        }
    }

    private TagObject addTagObject(String name) {
        TagObject tagObject = allTags.get(name);
        if (tagObject == null) {
            tagObject = new TagObject(name);
            allTags.put(tagObject.getTagName(), tagObject);
        }
        return tagObject;
    }

    public Background getBackgroundInfo() {
        return backgroundInfo;
    }
}
