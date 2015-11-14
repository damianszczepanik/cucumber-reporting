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
import net.masterthought.cucumber.json.Scenario;
import net.masterthought.cucumber.json.Step;
import net.masterthought.cucumber.json.Tag;
import net.masterthought.cucumber.json.support.ResultsWithMatch;
import net.masterthought.cucumber.json.support.ScenarioTag;
import net.masterthought.cucumber.json.support.Status;
import net.masterthought.cucumber.json.support.StatusCounter;
import net.masterthought.cucumber.json.support.StepObject;
import net.masterthought.cucumber.json.support.TagObject;
import net.masterthought.cucumber.util.Util;

public class ReportInformation {

    private final Map<String, StepObject> stepObjects = new HashMap<>();
    private List<Feature> features;

    private int numberOfScenarios;
    private int numberOfSteps;
    private final StatusCounter allStatuses = new StatusCounter();
    private final StatusCounter totalBackgroundSteps = new StatusCounter();

    private long totalDuration;
    private final Map<String, TagObject> allTags = new TreeMap<>();
    private int totalTagScenarios;
    private int totalTagSteps;
    private final StatusCounter tagStatusCounter = new StatusCounter();

    private long totalTagDuration;
    private int totalPassingTagScenarios;
    private int totalFailingTagScenarios;
    private Background backgroundInfo = new Background();

    public ReportInformation(Map<String, List<Feature>> featureMap) {
        this.features = listAllFeatures(featureMap);

        processFeatures();
        processTags();
        processSteps();
    }

    private List<Feature> listAllFeatures(Map<String, List<Feature>> featureMap) {
        List<Feature> allFeatures = new ArrayList<Feature>();
        for (Map.Entry<String, List<Feature>> pairs : featureMap.entrySet()) {
            List<Feature> featureList = pairs.getValue();
            allFeatures.addAll(featureList);
        }
        return allFeatures;
    }

    public List<Feature> getFeatures() {
        return this.features;
    }

    public List<TagObject> getTags() {
        return new ArrayList<TagObject>(this.allTags.values());
    }

    public Map<String, StepObject> getStepObject() {
        return this.stepObjects;
    }

    public int getTotalScenarios() {
        return numberOfScenarios;
    }

    public int getTotalFeatures() {
        return features.size();
    }

    public int getTotalSteps() {
        return numberOfSteps;
    }

    public int getTotalStepsPassed() {
        return allStatuses.getValueFor(Status.PASSED);
    }

    public int getTotalStepsFailed() {
        return allStatuses.getValueFor(Status.FAILED);
    }

    public int getTotalStepsSkipped() {
        return allStatuses.getValueFor(Status.SKIPPED);
    }

    public int getTotalStepsPending() {
        return allStatuses.getValueFor(Status.PENDING);
    }

    public int getTotalStepsMissing() {
        return allStatuses.getValueFor(Status.MISSING);
    }

    public int getTotalStepsUndefined() {
        return allStatuses.getValueFor(Status.UNDEFINED);
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
        return totalTagScenarios;
    }

    public int getTotalTagScenariosPassed() {
        return totalPassingTagScenarios;
    }

    public int getTotalTagScenariosFailed() {
        return totalFailingTagScenarios;
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

    public String getTotalTagDuration() {
        return Util.formatDuration(totalTagDuration);
    }

    public long getLongTotalTagDuration() {
        return totalTagDuration;
    }

    public int getTotalScenariosPassed() {
        return this.totalBackgroundSteps.getValueFor(Status.PASSED);
    }

    public int getTotalScenariosFailed() {
        return this.totalBackgroundSteps.getValueFor(Status.FAILED);
    }

    private void processTags() {
        for (TagObject tag : allTags.values()) {
            countTagScenarios(tag);

            for (Status status : Status.values()) {
                this.tagStatusCounter.incrementFor(status, tag.getNumberOfStatus(status));
            }

            for (ScenarioTag scenarioTag : tag.getScenarios()) {
                if (scenarioTag.hasSteps()) {
                    Step[] steps = scenarioTag.getScenario().getSteps();
                    for (Step step : steps) {
                        totalTagDuration += step.getDuration();
                    }
                    totalTagSteps += steps.length;
                }
            }
        }
    }

    private void countTagScenarios(TagObject tag) {
        for (ScenarioTag scenarioTag : tag.getScenarios()) {
            if (scenarioTag.getScenario().isScenario()) {
                this.totalTagScenarios++;
                if (scenarioTag.getScenario().getStatus() == Status.PASSED) {
                    this.totalPassingTagScenarios++;
                } else if (scenarioTag.getScenario().getStatus() == Status.FAILED) {
                    this.totalFailingTagScenarios++;
                }
            }
        }
    }

    private void processFeatures() {
        for (Feature feature : features) {
            List<ScenarioTag> scenarioTagList = new ArrayList<>();
            Scenario[] allFeatureScenarios = feature.getScenarios();
            this.numberOfScenarios += countNoBackgroundScenarios(allFeatureScenarios);

            for (Scenario scenario : allFeatureScenarios) {
                if (scenario.isScenario()) {
                    scenarioTagList.add(new ScenarioTag(scenario, feature.getFileName()));
                }
            }

            for (Scenario scenario : allFeatureScenarios) {
                if (scenario.isScenario()) {
                    totalBackgroundSteps.incrementFor(scenario.getStatus());
                } else {
                    updateBackgroundInfo(scenario);
                }
                addScenarioTagsToTagMap(scenario.getTags(), scenarioTagList);

                updateStepsForScenario(scenario);
            }
        }
    }

    private void processSteps() {
        for (Feature feature : features) {
            Scenario[] scenarios = feature.getScenarios();
            for (Scenario scenario : scenarios) {
                countSteps(scenario.getBefore());
                countSteps(scenario.getAfter());

                countSteps(scenario.getSteps());
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
            StepObject stepObject = this.stepObjects.get(methodName);
            // if first occurrence of this location add element to the map
            if (stepObject == null) {
                stepObject = new StepObject(methodName);
            }
            // happens that report is not valid - does not contain information about result
            if (step.getResult() != null) {
                stepObject.addDuration(step.getResult().getDuration(), step.getResult().getStatus());
            } else {
                // when result is not available it means that something really went wrong (report is incomplete)
                // and for this case FAILED status is used to avoid problems during parsing
                stepObject.addDuration(0, Status.FAILED.name());
            }
            this.stepObjects.put(methodName, stepObject);
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
            backgroundInfo.incrStepCounterForStatus(step.getStatus());
        }

    }

    private void updateStepsForScenario(Scenario scenario) {
        Step[] steps = scenario.getSteps();
        numberOfSteps += steps.length;
        for (Step step : steps) {
            allStatuses.incrementFor(step.getStatus());
            totalDuration += step.getDuration();
        }
    }

    private int countNoBackgroundScenarios(Scenario[] scenarios) {
        int counter = 0;
        for (Scenario scenario : scenarios) {
            if (scenario.isScenario()) {
                counter++;
            }
        }
        return counter;
    }

    private void addScenarioUnlessExists(TagObject tagObject, ScenarioTag scenarioToAdd) {
        for (ScenarioTag scenarioTag : tagObject.getScenarios()) {
            if (scenarioTag.getScenario().getId().equals(scenarioToAdd.getScenario().getId())
                    && scenarioTag.getScenario().equals(scenarioToAdd.getScenario())) {
                return;
            }
        }
        tagObject.addScenarios(scenarioToAdd);
    }

    private void addScenarioTagsToTagMap(Tag[] scenarioTagsAllScenarios, List<ScenarioTag> scenariosWithoutBackground) {
        for (Tag tag : scenarioTagsAllScenarios) {
            TagObject tagObject = addTagObject(tag.getName());

            for (ScenarioTag scenarioTag : scenariosWithoutBackground) {
                for (Tag tag2 : scenarioTag.getScenario().getTags()) {
                    if (tag2.getName().equals(tag.getName())) {
                        addScenarioUnlessExists(tagObject, scenarioTag);
                        break;
                    }
                }
            }

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
