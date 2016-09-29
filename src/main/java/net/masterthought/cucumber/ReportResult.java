package net.masterthought.cucumber;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ArrayUtils;

import net.masterthought.cucumber.generators.OverviewReport;
import net.masterthought.cucumber.json.Element;
import net.masterthought.cucumber.json.Feature;
import net.masterthought.cucumber.json.Match;
import net.masterthought.cucumber.json.Result;
import net.masterthought.cucumber.json.Step;
import net.masterthought.cucumber.json.Tag;
import net.masterthought.cucumber.json.support.Resultsable;
import net.masterthought.cucumber.json.support.Status;
import net.masterthought.cucumber.json.support.StepObject;
import net.masterthought.cucumber.json.support.TagObject;

public class ReportResult {

    private final List<Feature> allFeatures = new ArrayList<>();
    private final Map<String, TagObject> allTags = new TreeMap<>();
    private final Map<String, StepObject> allSteps = new TreeMap<>();

    private final String buildTime;

    private final OverviewReport featuresReport = new OverviewReport();
    private final OverviewReport tagsReport = new OverviewReport();

    public ReportResult(List<Feature> features) {
        this.buildTime = getCurrentTime();

        for (Feature feature : features) {
            processFeature(feature);
        }
    }

    public List<Feature> getAllFeatures() {
        return toSortedList(allFeatures);
    }

    public List<TagObject> getAllTags() {
        return toSortedList(allTags.values());
    }

    public List<StepObject> getAllSteps() {
        return toSortedList(allSteps.values());
    }

    private static <T extends Comparable<? super T>> List<T> toSortedList(Collection<T> values) {
        List<T> list = new ArrayList<T>(values);
        Collections.sort(list);
        return list;
    }

    public Reportable getFeatureReport() {
        return featuresReport;
    }

    public Reportable getTagReport() {
        return tagsReport;
    }

    public int getAllPassedFeatures() {
        return featuresReport.getPassedFeatures();
    }

    public int getAllFailedFeatures() {
        return featuresReport.getFailedFeatures();
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
                // don't count tag for feature if was already counted for element
                if (!ArrayUtils.contains(feature.getTags(), tag)) {
                    processTag(tag, element, element.getStatus());
                }
            }

            Step[] steps = element.getSteps();
            for (Step step : steps) {
                featuresReport.incStepsFor(step.getResult().getStatus());
                featuresReport.incDurationBy(step.getDuration());
            }
            countSteps(steps);

            countSteps(element.getBefore());
            countSteps(element.getAfter());
        }
        featuresReport.incFeaturesFor(feature.getStatus());
    }

    private void processTag(Tag tag, Element element, Status status) {

        TagObject tagObject = addTagObject(tag.getName());

        // if this element was not added by feature tag, add it as element tag
        if (tagObject.addElement(element)) {
            tagsReport.incScenarioFor(status);

            Step[] steps = element.getSteps();
            for (Step step : steps) {
                tagsReport.incStepsFor(step.getResult().getStatus());
                tagsReport.incDurationBy(step.getDuration());
            }
        }
    }

    private void countSteps(Resultsable[] steps) {
        for (Resultsable step : steps) {

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
            addNewStep(step.getResult(), methodName);
        }
    }

    private void addNewStep(Result result, String methodName) {
        StepObject stepObject = allSteps.get(methodName);
        // if first occurrence of this location add element to the map
        if (stepObject == null) {
            stepObject = new StepObject(methodName);
        }

        // happens that report is not valid - does not contain information about result
        stepObject.addDuration(result.getDuration(), result.getStatus());
        allSteps.put(methodName, stepObject);
    }

    private TagObject addTagObject(String name) {
        TagObject tagObject = allTags.get(name);
        if (tagObject == null) {
            tagObject = new TagObject(name);
            allTags.put(tagObject.getName(), tagObject);
        }
        return tagObject;
    }

    public static String getCurrentTime() {
        return new SimpleDateFormat("dd MMM yyyy, HH:mm").format(new Date());
    }
}
