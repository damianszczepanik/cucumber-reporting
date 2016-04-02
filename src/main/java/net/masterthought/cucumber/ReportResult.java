package net.masterthought.cucumber;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;

import net.masterthought.cucumber.json.Element;
import net.masterthought.cucumber.json.Feature;
import net.masterthought.cucumber.json.Match;
import net.masterthought.cucumber.json.Result;
import net.masterthought.cucumber.json.Step;
import net.masterthought.cucumber.json.Tag;
import net.masterthought.cucumber.json.support.ResultsWithMatch;
import net.masterthought.cucumber.json.support.Status;
import net.masterthought.cucumber.json.support.StatusCounter;
import net.masterthought.cucumber.json.support.StepObject;
import net.masterthought.cucumber.json.support.TagObject;
import net.masterthought.cucumber.reports.OverviewReport;
import net.masterthought.cucumber.reports.Reportable;

public class ReportResult {

    private final List<Feature> allFeatures = new ArrayList<>();
    private final Map<String, TagObject> allTags = new TreeMap<>();
    private final Map<String, StepObject> allSteps = new HashMap<>();

    private final StatusCounter featureCounter = new StatusCounter();

    private final String buildTime;

    private final OverviewReport featuresReport = new OverviewReport("Features");
    private final OverviewReport tagsReport = new OverviewReport("Tags");

    public ReportResult(List<Feature> features) {
        this.buildTime = new SimpleDateFormat("dd MMM yyyy, HH:mm").format(new Date());

        for (Feature feature : features) {
            processFeature(feature);
        }
    }

    public List<Feature> getAllFeatures() {
        return allFeatures;
    }

    public List<TagObject> getAllTags() {
        return new ArrayList<>(allTags.values());
    }

    public List<StepObject> getAllSteps() {
        return new ArrayList<>(allSteps.values());
    }

    public Reportable getFeatureReport() {
        return featuresReport;
    }

    public Reportable getTagReport() {
        return tagsReport;
    }

    public int getAllPassedFeatures() {
        return featureCounter.getValueFor(Status.PASSED);
    }

    public int getAllFailedFeatures() {
        return featureCounter.getValueFor(Status.FAILED);
    }

    public String getBuildTime() {
        return buildTime;
    }

    private void processFeature(Feature feature) {
        allFeatures.add(feature);

        for (Element element : feature.getElements()) {
            if (element.isScenario()) {
                featuresReport.incScenarioFor(element.getStatus());

                // all feature tags should be linked with scenario
                for (Tag tag : feature.getTags()) {
                    processTag(tag, element, feature.getStatus());
                }
            }

            // all element tags should be linked with element
            for (Tag tag : element.getTags()) {
                processTag(tag, element, element.getStatus());
            }

            Step[] steps = element.getSteps();
            for (Step step : steps) {
                featuresReport.incStepsFor(step.getStatus());
                featuresReport.incDurationBy(step.getDuration());
            }
            countSteps(steps);

            countSteps(element.getBefore());
            countSteps(element.getAfter());
        }
        featureCounter.incrementFor(feature.getStatus());
    }

    private void processTag(Tag tag, Element element, Status status) {

        TagObject tagObject = addTagObject(tag.getName());

        // if this element was not added by feature tag, add it as element tag
        if (tagObject.addElement(element)) {
            tagsReport.incScenarioFor(status);

            Step[] steps = element.getSteps();
            for (Step step : steps) {
                tagsReport.incStepsFor(step.getStatus());
                tagsReport.incDurationBy(step.getDuration());
            }
        }
    }

    private void countSteps(ResultsWithMatch[] steps) {
        for (ResultsWithMatch step : steps) {

            Match match = step.getMatch();
            // no match = could not find method that was matched to this step -> status is missing
            if (match == null) {
                continue;
            }

            String methodName = match.getLocation();
            // location is missing so there is no way to identify step
            if (StringUtils.isEmpty(methodName)) {
                continue;
            }

            StepObject stepObject = allSteps.get(methodName);
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
            allSteps.put(methodName, stepObject);
        }
    }

    private TagObject addTagObject(String name) {
        TagObject tagObject = allTags.get(name);
        if (tagObject == null) {
            tagObject = new TagObject(name);
            allTags.put(tagObject.getRawName(), tagObject);
        }
        return tagObject;
    }
}
