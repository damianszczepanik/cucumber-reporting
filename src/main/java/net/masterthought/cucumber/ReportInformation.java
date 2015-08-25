package net.masterthought.cucumber;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.googlecode.totallylazy.Sequence;

import net.masterthought.cucumber.json.Element;
import net.masterthought.cucumber.json.Feature;
import net.masterthought.cucumber.json.Step;
import net.masterthought.cucumber.json.support.ResultsWithMatch;
import net.masterthought.cucumber.util.Status;
import net.masterthought.cucumber.util.StatusCounter;
import net.masterthought.cucumber.util.Util;

public class ReportInformation {

    private final Map<String, List<Feature>> featureMap;
    private final Map<String, StepObject> stepObjects = new HashMap<>();
    private List<Feature> features;

    private int numberOfScenarios;
    private int numberOfSteps;
    private final StatusCounter totalSteps = new StatusCounter();
    private final StatusCounter totalBackgroundSteps = new StatusCounter();

    private Long totalDuration = 0l;
    private List<TagObject> tagMap = new ArrayList<>();
    private int totalTagScenarios = 0;
    private int totalTagSteps = 0;
    private final StatusCounter totalTags = new StatusCounter();

    private long totalTagDuration = 0L;
    private int totalPassingTagScenarios = 0;
    private int totalFailingTagScenarios = 0;
    private Background backgroundInfo = new Background();

    public ReportInformation(Map<String, List<Feature>> featureMap) {
        this.featureMap = featureMap;
        this.features = listAllFeatures();

        processFeatures();
        processTags();
        processSteps();
    }

    private List<Feature> listAllFeatures() {
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
        return this.tagMap;
    }

    public Map<String, List<Feature>> getFeatureMap() {
        return this.featureMap;
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
        return totalSteps.getValueFor(Status.PASSED);
    }

    public int getTotalStepsFailed() {
        return totalSteps.getValueFor(Status.FAILED);
    }

    public int getTotalStepsSkipped() {
        return totalSteps.getValueFor(Status.SKIPPED);
    }

    public int getTotalStepsPending() {
        return totalSteps.getValueFor(Status.PENDING);
    }

    public int getTotalStepsMissing() {
        return totalSteps.getValueFor(Status.MISSING);
    }

    public int getTotalStepsUndefined() {
        return totalSteps.getValueFor(Status.UNDEFINED);
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

    public String getReportStatusColour(Feature feature) {
        return feature.getStatus() == Status.PASSED ? Status.PASSED.color : Status.FAILED.color;
    }

    public String getTagReportStatusColour(TagObject tag) {
        return tag.getStatus() == Status.PASSED ? Status.PASSED.color : Status.FAILED.color;
    }

    public int getTotalTags() {
        return tagMap.size();
    }

    public int getTotalTagScenarios() {
        return totalTagScenarios;
    }

    public List<TagObject> getTagMap() {
        return tagMap;
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
        return totalTags.getValueFor(Status.PASSED);
    }

    public int getTotalTagFails() {
        return totalTags.getValueFor(Status.FAILED);
    }

    public int getTotalTagSkipped() {
        return totalTags.getValueFor(Status.SKIPPED);
    }

    public int getTotalTagPending() {
        return totalTags.getValueFor(Status.PENDING);
    }

    public int getTotalTagUndefined() {
        return totalTags.getValueFor(Status.UNDEFINED);
    }

    public int getTotalTagMissing() {
        return totalTags.getValueFor(Status.MISSING);
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
        for (TagObject tag : tagMap) {
            totalTagScenarios = calculateTotalTagScenarios(tag);
            totalPassingTagScenarios = calculateTotalTagScenariosForStatus(totalPassingTagScenarios, tag, Status.PASSED);
            totalFailingTagScenarios = calculateTotalTagScenariosForStatus(totalFailingTagScenarios, tag, Status.FAILED);
            for (Status status : Status.values()) {
                this.totalTags.incrementFor(status, tag.getNumberOfStatus(status));
            }

            for (ScenarioTag scenarioTag : tag.getScenarios()) {
                if (scenarioTag.hasSteps()) {
                    Sequence<Step> steps = scenarioTag.getScenario().getSteps();
                    List<Step> stepList = new ArrayList<Step>();
                    for (Step step : steps) {
                        stepList.add(step);
                        totalTagDuration = totalTagDuration + step.getDuration();
                    }
                    totalTagSteps += stepList.size();
                }
            }
        }
    }

    private int calculateTotalTagScenariosForStatus(int totalScenarios, TagObject tag, Status status) {
        List<ScenarioTag> scenarioTagList = new ArrayList<>();
        for (ScenarioTag scenarioTag : tag.getScenarios()) {
            if (!scenarioTag.getScenario().isBackground()) {
                if (scenarioTag.getScenario().getStatus().equals(status)) {
                    scenarioTagList.add(scenarioTag);
                }
            }
        }
        return totalScenarios + scenarioTagList.size();
    }

    private int calculateTotalTagScenarios(TagObject tag) {
        List<ScenarioTag> scenarioTagList = new ArrayList<>();
        for (ScenarioTag scenarioTag : tag.getScenarios()) {
            if (!scenarioTag.getScenario().isBackground()) {
                scenarioTagList.add(scenarioTag);
            }
        }
        return totalTagScenarios + scenarioTagList.size();
    }

    private void processFeatures() {
        for (Feature feature : features) {
            List<ScenarioTag> scenarioList = new ArrayList<>();
            Sequence<Element> scenarios = feature.getElements();
            numberOfScenarios = getNumberOfScenarios(scenarios);
            // build map with tags
            if (feature.hasTags()) {
                for (Element e : feature.getElements()) {
                    if (!e.isBackground()) {
                        scenarioList.add(new ScenarioTag(e, feature.getFileName()));
                    }
                }
                tagMap = addToTagMapByFeature(tagMap, feature.getTagList(), scenarioList);

            }

            for (Element scenario : scenarios) {
                if (!scenario.isBackground()) {
                    totalBackgroundSteps.incrementFor(scenario.getStatus());
                } else {
                    setBackgroundInfo(scenario);
                }

                if (scenario.hasTags()) {
                    addScenarioUnlessExists(scenarioList, new ScenarioTag(scenario, feature.getFileName()));
                    tagMap = addToTagMap(tagMap, scenario.getTagList(), scenarioList);
                }

                adjustStepsForScenario(scenario);
            }
        }
    }

    private void processSteps() {
        for (Feature feature : features) {
            Sequence<Element> scenarios = feature.getElements();
            for (Element scenario : scenarios) {

                countSteps(scenario.getBefore());
                countSteps(scenario.getAfter());

                countSteps(scenario.getSteps().toArray(ResultsWithMatch.class));
            }
        }
    }

    private void countSteps(ResultsWithMatch[] steps) {
        // before and after are not mandatory
        if (steps != null) {
            for (ResultsWithMatch step : steps) {

                    String methodName = step.getMatch().getLocation();
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
    }

    private void setBackgroundInfo(Element e) {
        backgroundInfo.addTotalScenarios(1);
        if (e.getStatus() == Status.PASSED) {
            backgroundInfo.addTotalScenariosPassed(1);
        } else {
            backgroundInfo.addTotalScenariosFailed(1);
        }
        backgroundInfo.addTotalSteps(e.getSteps().size());
        for (Step step : e.getSteps().toList()) {
            backgroundInfo.incrTotalDurationBy(step.getDuration());
            backgroundInfo.incrStepCounterForStatus(step.getStatus());
        }

    }

    private void adjustStepsForScenario(Element element) {
        String scenarioName = element.getRawName();
        if (element.hasSteps()) {
            Sequence<Step> steps = element.getSteps();
            numberOfSteps = numberOfSteps + steps.size();
            for (Step step : steps) {
                String stepName = step.getRawName();

                //apply artifacts
                ConfigurationOptions configuration = ConfigurationOptions.instance();
                if (configuration.artifactsEnabled()) {
                    Map<String, Artifact> map = configuration.artifactConfig();
                    String mapKey = scenarioName + stepName;
                    if (map.containsKey(mapKey)) {
                        Artifact artifact = map.get(mapKey);
                        String keyword = artifact.getKeyword();
                        String contentType = artifact.getContentType();
                        step.setName(stepName.replaceFirst(keyword, getArtifactFile(mapKey, keyword, artifact.getArtifactFile(), contentType)));
                    }
                }

                this.totalSteps.incrementFor(step.getStatus());
                totalDuration = totalDuration + step.getDuration();
            }
        }
    }

    private int getNumberOfScenarios(Sequence<Element> scenarios) {
        List<Element> scenarioList = new ArrayList<>();
        for (Element scenario : scenarios) {
            if (!scenario.isBackground()) {
                scenarioList.add(scenario);
            }
        }
        return numberOfScenarios + scenarioList.size();
    }

    private String getArtifactFile(String mapKey, String keyword, String artifactFile, String contentType) {
        mapKey = mapKey.replaceAll(" ", "_");
        String link = "";
        if (contentType.equals("xml")) {
            link = "<div style=\"display:none;\"><textarea id=\"" + mapKey + "\" class=\"brush: xml;\"></textarea></div><a onclick=\"applyArtifact('" + mapKey + "','" + artifactFile + "')\" href=\"#\">" + keyword + "</a>";
        } else {
            link = "<div style=\"display:none;\"><textarea id=\"" + mapKey + "\"></textarea></div><script>\\$('#" + mapKey + "').load('" + artifactFile + "')</script><a onclick=\"\\$('#" + mapKey + "').dialog();\" href=\"#\">" + keyword + "</a>";
        }
        return link;
    }

    private void addScenarioUnlessExists(List<ScenarioTag> scenarioList, ScenarioTag scenarioTag) {
        for (ScenarioTag scenario : scenarioList) {
            if (scenario.getParentFeatureUri().equalsIgnoreCase(scenarioTag.getParentFeatureUri())
                    && scenario.getScenario().equals(scenarioTag.getScenario())) {
                return;
            }
        }
        scenarioList.add(scenarioTag);
    }

    private List<TagObject> addToTagMap(List<TagObject> tagMap, Sequence<String> tagList, List<ScenarioTag> scenarioList) {

        for (String tagName : tagList) {
            TagObject tagObj = findTagObjectByNameInList(tagName, tagMap);

            List<ScenarioTag> existingTagList = new ArrayList<>();
            if (tagObj == null) {
                tagObj = new TagObject(tagName, existingTagList);
            } else {
                existingTagList.addAll(tagObj.getScenarios());
                tagObj.setScenarios(existingTagList);
                tagMap.remove(tagObj);
            }

            for (ScenarioTag scenarioTag : scenarioList) {
                if (scenarioTag.getScenario().getTagList().contains(tagName)) {
                    addScenarioUnlessExists(existingTagList, scenarioTag);
                }
            }

            tagMap.add(tagObj);
        }
        return tagMap;
    }

    public List<TagObject> addToTagMapByFeature(List<TagObject> tagMap, Sequence<String> tagList, List<ScenarioTag> scenarioList) {

        for (String tagName : tagList) {
            TagObject tagObj = findTagObjectByNameInList(tagName, tagMap);

            if (tagObj != null) {
                List<ScenarioTag> allScenarios = new ArrayList<>();
                allScenarios.addAll(tagObj.getScenarios());
                allScenarios.addAll(scenarioList);

                tagMap.remove(tagObj);
                tagObj.setScenarios(allScenarios);
            } else {
                tagObj = new TagObject(tagName, scenarioList);
            }
            tagMap.add(tagObj);
        }
        return tagMap;

    }

    private TagObject findTagObjectByNameInList(String name, List<TagObject> list) {
        for (TagObject tagObject : list) {
            if (tagObject.getTagName().equalsIgnoreCase(name)) {
                return tagObject;
            }
        }
        return null;
    }

    public Background getBackgroundInfo() {
        return backgroundInfo;
    }
}
