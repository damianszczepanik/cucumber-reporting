package net.masterthought.cucumber.json;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;

import net.masterthought.cucumber.ReportBuilder;
import net.masterthought.cucumber.json.support.ScenarioResults;
import net.masterthought.cucumber.json.support.StepResults;
import net.masterthought.cucumber.util.Status;
import net.masterthought.cucumber.util.StatusCounter;
import net.masterthought.cucumber.util.Util;

public class Feature {

    private final String id = null;
    private final String name = null;
    private final String uri = null;
    private final String description = null;
    private final String keyword = null;
    private final Element[] elements = new Element[0];
    private final Tag[] tags = new Tag[0];

    private String fileName;
    private String deviceName;
    private StepResults stepResults;
    private ScenarioResults scenarioResults;

    private String jsonFile = "";

    public String getDeviceName() {
        if (deviceName == null) {
            String[] splitedJsonFile = jsonFile.split("[^\\d\\w]");
            if (splitedJsonFile.length > 1) {
                // file name without path and extension (usually *.json)
                deviceName = splitedJsonFile[splitedJsonFile.length - 2];
            } else {
                // path name without special characters
                deviceName = splitedJsonFile[0];
            }
        }
        return deviceName;
    }

    public void setJsonFile(String json){
        this.jsonFile = json;
    }

    public Element[] getElements() {
        return elements;
    }

    public String getFileName() {
        if (fileName == null) {
            // remove all characters that might not be valid file name
            fileName = uri.replaceAll("[^\\d\\w]", "-");

            // If we expect to have parallel executions, we add postfix to file name
            if (ReportBuilder.isParallel() && !jsonFile.isEmpty()) {
                String[] splitedJsonFile = jsonFile.split("_");
                if (splitedJsonFile.length > 1) {
                    fileName = fileName + "-" + getDeviceName();
                }
            }
            fileName = fileName + ".html";
        }
        return fileName;
    }

    public String getUri(){
        return this.uri;
    }

    public boolean hasTags() {
        return Util.arrayNotEmpty(tags);
    }

    public boolean hasScenarios() {
        return elements.length > 0;
    }

    public Tag[] getTags() {
        return tags;
    }

    public String getTagsList() {
        return Util.tagsToHtml(tags);
    }

    public Status getStatus() {
        for (Element element : elements) {
            if (element.getStatus() != Status.PASSED) {
                return Status.FAILED;
            }
        }
        return Status.PASSED;
    }

    public String getName() {
        return Util.itemExists(name) ? getStatus().toHtmlClass()
                + "<div class=\"feature-line\"><span class=\"feature-keyword\">" + keyword + ":</span> " + name
                + "</div></div>" : "";
    }

    public String getRawName() {
        return Util.itemExists(name) ? StringEscapeUtils.escapeHtml(name) : "";
    }

    public String getRawStatus() {
        return getStatus().toString().toLowerCase();
    }

    public String getDescription() {
        String result = "";
        if (Util.itemExists(description)) {
            String content = description.replaceFirst("As an", "<span class=\"feature-role\">As an</span>");
            content = content.replaceFirst("I want to", "<span class=\"feature-action\">I want to</span>");
            content = content.replaceFirst("So that", "<span class=\"feature-value\">So that</span>");
            content = content.replaceAll("\n", "<br/>");
            result = "<div class=\"feature-description\">" + content + "</div>";
        }
        return result;
    }

    public int getNumberOfScenarios() {
        int result = 0;
        if (elements != null) {
            List<Element> elementList = new ArrayList<Element>();
            for (Element element : elements) {
                if (!element.isBackground()) {
                    elementList.add(element);
                }
            }
            result = elementList.size();
        }
        return result;
    }

    public int getNumberOfSteps() {
        return stepResults.getNumberOfSteps();
    }

    public int getNumberOfPasses() {
        return stepResults.getNumberOfPasses();
    }

    public int getNumberOfFailures() {
        return stepResults.getNumberOfFailures();
    }

    public int getNumberOfPending() {
        return stepResults.getNumberOfPending();
    }

    public int getNumberOfSkipped() {
        return stepResults.getNumberOfSkipped();
    }

    public int getNumberOfMissing() {
        return stepResults.getNumberOfMissing();
    }

    public int getNumberOfUndefined() {
        return stepResults.getNumberOfUndefined();
    }

    public String getDurationOfSteps() {
        return stepResults.getTotalDurationAsString();
    }

    public int getNumberOfScenariosPassed() {
        return scenarioResults.getNumberOfScenariosPassed();
    }

    public int getNumberOfScenariosFailed() {
        return scenarioResults.getNumberOfScenariosFailed();
    }

    public void processSteps() {
        List<Step> allSteps = new ArrayList<Step>();
        StatusCounter stepsCounter = new StatusCounter();
        List<Element> passedScenarios = new ArrayList<>();
        List<Element> failedScenarios = new ArrayList<>();
        long totalDuration = 0L;

        if (elements != null) {
            for (Element element : elements) {
                calculateScenarioStats(passedScenarios, failedScenarios, element);
                if (element.hasSteps()) {
                    for (Step step : element.getSteps()) {
                        allSteps.add(step);
                        stepsCounter.incrementFor(step.getStatus());
                        totalDuration += step.getDuration();
                    }
                }
            }
        }
        scenarioResults = new ScenarioResults(passedScenarios, failedScenarios);
        stepResults = new StepResults(allSteps, stepsCounter, totalDuration);
    }

    private void calculateScenarioStats(List<Element> passedScenarios, List<Element> failedScenarios, Element element) {
        if (!element.isBackground()) {
            if (element.getStatus() == Status.PASSED) {
                passedScenarios.add(element);
            } else if (element.getStatus() == Status.FAILED) {
                failedScenarios.add(element);
            }
        }
    }


}
