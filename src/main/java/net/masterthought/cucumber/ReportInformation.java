package net.masterthought.cucumber;

import net.masterthought.cucumber.json.Element;
import net.masterthought.cucumber.json.Feature;
import net.masterthought.cucumber.json.Step;
import net.masterthought.cucumber.util.Util;

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
    private Long totalDuration = 0l;

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

    public List<Feature> getFeatures(){
        return this.features;
    }

    public Map<String, List<Feature>> getProjectFeatureMap(){
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

    private void processFeatures() {
        for (Feature feature : features) {
            Element[] scenarios = feature.getElements();
            numberOfScenarios = numberOfScenarios + scenarios.length;
            for (Element scenario : scenarios) {
                Step[] steps = scenario.getSteps();
                numberOfSteps = numberOfSteps + steps.length;
                for (Step step : steps) {
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
