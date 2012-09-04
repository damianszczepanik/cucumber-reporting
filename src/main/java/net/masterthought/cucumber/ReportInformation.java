package net.masterthought.cucumber;

import net.masterthought.cucumber.json.Artifact;
import net.masterthought.cucumber.json.Element;
import net.masterthought.cucumber.json.Feature;
import net.masterthought.cucumber.json.Step;
import net.masterthought.cucumber.util.Util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

public class ReportInformation {

    private Map<String, List<Feature>> projectFeatureMap;
    private List<Feature> features;
    private int numberOfScenarios;
    private int numberOfSteps;
    private List<Step> totalPassingSteps = new ArrayList<Step>();
    private List<Step> totalFailingSteps = new ArrayList<Step>();
    private List<Step> totalSkippedSteps = new ArrayList<Step>();
    private List<Step> totalUndefinedSteps = new ArrayList<Step>();
    private List<Step> totalMissingSteps = new ArrayList<Step>();
    private List<Element> numberPassingScenarios = new ArrayList<Element>();
    private List<Element> numberFailingScenarios = new ArrayList<Element>();
    private Long totalDuration = 0l;
    List<TagObject> tagMap = new ArrayList<TagObject>();
    private int totalTagScenarios = 0;
    private int totalTagSteps = 0;
    private int totalTagPasses = 0;
    private int totalTagFails = 0;
    private int totalTagSkipped = 0;
    private int totalTagPending = 0;
    private long totalTagDuration = 0l;

    public ReportInformation(Map<String, List<Feature>> projectFeatureMap) {
        this.projectFeatureMap = projectFeatureMap;
        this.features = listAllFeatures();
        processFeatures();
    }

    private List<Feature> listAllFeatures() {
        List<Feature> allFeatures = new ArrayList<Feature>();
        Iterator it = projectFeatureMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry) it.next();
            List<Feature> featureList = (List<Feature>) pairs.getValue();
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

    public Map<String, List<Feature>> getProjectFeatureMap() {
        return this.projectFeatureMap;
    }

    public int getTotalNumberOfScenarios() {
        return numberOfScenarios;
    }

    public int getTotalNumberOfFeatures() {
        return features.size();
    }

    public int getTotalNumberOfSteps() {
        return numberOfSteps;
    }

    public int getTotalNumberPassingSteps() {
        return totalPassingSteps.size();
    }

    public int getTotalNumberFailingSteps() {
        return totalFailingSteps.size();
    }

    public int getTotalNumberSkippedSteps() {
        return totalSkippedSteps.size();
    }

    public int getTotalNumberPendingSteps() {
        return totalUndefinedSteps.size();
    }

    public int getTotalNumberMissingSteps() {
        return totalMissingSteps.size();
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
        return feature.getStatus() == Util.Status.PASSED ? "#C5D88A" : "#D88A8A";
    }

    public String getTagReportStatusColour(TagObject tag) {
        return tag.getStatus() == Util.Status.PASSED ? "#C5D88A" : "#D88A8A";
    }

    public int getTotalTags() {
        return tagMap.size();
    }

    public int getTotalTagScenarios() {
        return totalTagScenarios;
    }

    public int getTotalTagSteps() {
        return totalTagSteps;
    }

    public int getTotalTagPasses() {
        return totalTagPasses;
    }

    public int getTotalTagFails() {
        return totalTagFails;
    }

    public int getTotalTagSkipped() {
        return totalTagSkipped;
    }

    public int getTotalTagPending() {
        return totalTagPending;
    }

    public String getTotalTagDuration() {
        return Util.formatDuration(totalTagDuration);
    }

    public int getTotalScenariosPassed() {
        return numberPassingScenarios.size();
    }

    public int getTotalScenariosFailed() {
        return numberFailingScenarios.size();
    }

    private void processTags() {
        for (TagObject tag : tagMap) {
            totalTagScenarios = totalTagScenarios + tag.getScenarios().size();
            totalTagPasses += tag.getNumberOfPasses();
            totalTagFails += tag.getNumberOfFailures();
            totalTagSkipped += tag.getNumberOfSkipped();
            totalTagPending += tag.getNumberOfPending();


            for (ScenarioTag scenarioTag : tag.getScenarios()) {
                if (Util.hasSteps(scenarioTag)) {
                    Step[] steps = scenarioTag.getScenario().getSteps();
                    for (Step step : steps) {
                        totalTagSteps += steps.length;
                        totalTagDuration = totalTagDuration + step.getDuration();
                    }
                }
            }
        }
    }

    private void processFeatures() {
        for (Feature feature : features) {
            List<ScenarioTag> scenarioList = new ArrayList<ScenarioTag>();
            Element[] scenarios = feature.getElements();
            if (Util.itemExists(scenarios)) {
                numberOfScenarios = numberOfScenarios + scenarios.length;
                for (Element scenario : scenarios) {
                    String scenarioName = scenario.getRawName();

                    numberPassingScenarios = Util.setScenarioStatus(numberPassingScenarios, scenario, scenario.getStatus(), Util.Status.PASSED);
                    numberFailingScenarios = Util.setScenarioStatus(numberFailingScenarios, scenario, scenario.getStatus(), Util.Status.FAILED);

                    //process tags
                    if (feature.hasTags()) {
                        scenarioList.add(new ScenarioTag(scenario, feature.getFileName()));
                        tagMap = createOrAppendToTagMap(tagMap, feature.getTagList(), scenarioList);
                    }

                    if (Util.hasScenarios(feature)) {
                        if (scenario.hasTags()) {
                            scenarioList = addScenarioUnlessExists(scenarioList, new ScenarioTag(scenario, feature.getFileName()));
                        }
                        tagMap = createOrAppendToTagMap(tagMap, scenario.getTagList(), scenarioList);
                    }

                    if (Util.hasSteps(scenario)) {
                        Step[] steps = scenario.getSteps();
                        numberOfSteps = numberOfSteps + steps.length;
                        for (Step step : steps) {
                            String stepName = step.getRawName();

                            //apply artifacts
                            if (ConfigurationOptions.artifactsEnabled()) {
                                Map<String, Artifact> map = ConfigurationOptions.artifactConfig();
                                String mapKey = scenarioName + stepName;
                                if(map.containsKey(mapKey)){
                                    Artifact artifact = map.get(mapKey);
                                    String keyword = artifact.getKeyword();
                                    String contentType = artifact.getContentType();
                                    step.setName(stepName.replaceFirst(keyword, getArtifactFile(mapKey,keyword,artifact.getArtifactFile(),contentType)));
                                }
                            }

                            Util.Status stepStatus = step.getStatus();
                            totalPassingSteps = Util.setStepStatus(totalPassingSteps, step, stepStatus, Util.Status.PASSED);
                            totalFailingSteps = Util.setStepStatus(totalFailingSteps, step, stepStatus, Util.Status.FAILED);
                            totalSkippedSteps = Util.setStepStatus(totalSkippedSteps, step, stepStatus, Util.Status.SKIPPED);
                            totalUndefinedSteps = Util.setStepStatus(totalUndefinedSteps, step, stepStatus, Util.Status.UNDEFINED);
                            totalMissingSteps = Util.setStepStatus(totalMissingSteps, step, stepStatus, Util.Status.MISSING);
                            totalDuration = totalDuration + step.getDuration();
                        }
                    }
                }
            }
        }
        processTags();
    }

    private String getArtifactFile(String mapKey, String keyword, String artifactFile, String contentType){
        mapKey = mapKey.replaceAll(" ","_");
        String link = "";
        if(contentType.equals("xml")){
          link = "<div style=\"display:none;\"><textarea id=\"" + mapKey + "\" class=\"brush: xml;\"></textarea></div><a onclick=\"applyArtifact('" + mapKey + "','" + artifactFile +"')\" href=\"#\">" + keyword + "</a>";
        } else {
            link = "<div style=\"display:none;\"><textarea id=\"" + mapKey + "\"></textarea></div><script>\\$('#" + mapKey + "').load('" + artifactFile + "')</script><a onclick=\"\\$('#" + mapKey + "').dialog();\" href=\"#\">" + keyword + "</a>";
        }
        return link;
    }

    private List<ScenarioTag> addScenarioUnlessExists(List<ScenarioTag> scenarioList, ScenarioTag scenarioTag) {
        boolean exists = false;
        for (ScenarioTag scenario : scenarioList) {
            if (scenario.getParentFeatureUri().equalsIgnoreCase(scenarioTag.getParentFeatureUri())
                    && scenario.getScenario().getName().equalsIgnoreCase(scenarioTag.getScenario().getName())) {
                exists = true;
                break;
            }
        }

        if (!exists) {
            scenarioList.add(scenarioTag);
        }
        return scenarioList;
    }

    private List<TagObject> createOrAppendToTagMap(List<TagObject> tagMap, List<String> tagList, List<ScenarioTag> scenarioList) {
        for (String tag : tagList) {
            boolean exists = false;
            TagObject tagObj = null;
            for (TagObject tagObject : tagMap) {
                if (tagObject.getTagName().equalsIgnoreCase(tag)) {
                    exists = true;
                    tagObj = tagObject;
                    break;
                }
            }
            if (exists) {
                List<ScenarioTag> existingTagList = tagObj.getScenarios();
                for (ScenarioTag scenarioTag : scenarioList) {
                    existingTagList = addScenarioUnlessExists(existingTagList, scenarioTag);
                }
                tagMap.remove(tagObj);
                tagObj.setScenarios(existingTagList);
                tagMap.add(tagObj);
            } else {
                tagObj = new TagObject(tag, scenarioList);
                tagMap.add(tagObj);
            }
        }
        return tagMap;
    }


}
