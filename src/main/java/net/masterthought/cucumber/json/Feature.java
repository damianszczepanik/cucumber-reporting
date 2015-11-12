package net.masterthought.cucumber.json;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ArrayUtils;

import net.masterthought.cucumber.ReportBuilder;
import net.masterthought.cucumber.json.support.Status;
import net.masterthought.cucumber.json.support.StatusCounter;
import net.masterthought.cucumber.json.support.StepResults;
import net.masterthought.cucumber.util.Util;

public class Feature {

    private final String id = null;
    private final String name = null;
    private final String uri = null;
    private final String description = null;
    private final String keyword = null;
    private final Scenario[] elements = new Scenario[0];
    private final Tag[] tags = new Tag[0];

    private String fileName;
    private String deviceName;
    private StepResults stepResults;
    private List<Scenario> passedScenarios;
    private List<Scenario> failedScenarios;

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

    public String getId() {
        return id;
    }

    public void setJsonFile(String json){
        this.jsonFile = json;
    }

    public Scenario[] getScenarios() {
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
        return !ArrayUtils.isEmpty(tags);
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
        for (Scenario element : elements) {
            if (element.getStatus() != Status.PASSED) {
                return Status.FAILED;
            }
        }
        return Status.PASSED;
    }

    public String getName() {
        return StringUtils.isNotEmpty(name) ? getStatus().toHtmlClass()
                + "<div class=\"feature-line\"><span class=\"feature-keyword\">" + keyword + ":</span> " + name
                + "</div></div>" : "";
    }

    public String getRawName() {
        return StringUtils.isNotEmpty(name) ? StringEscapeUtils.escapeHtml(name) : "";
    }

    public String getRawStatus() {
        return getStatus().toString().toLowerCase();
    }

    public String getDescription() {
        String result = "";
        if (StringUtils.isNotEmpty(description)) {
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
            List<Scenario> elementList = new ArrayList<Scenario>();
            for (Scenario element : elements) {
                if (element.isScenario()) {
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

    public String getTotalDuration() {
        return stepResults.getTotalDurationAsString();
    }

    public int getNumberOfScenariosPassed() {
        return passedScenarios.size();
    }

    public int getNumberOfScenariosFailed() {
        return failedScenarios.size();
    }

    public void processSteps() {
        List<Step> allSteps = new ArrayList<Step>();
        StatusCounter stepsCounter = new StatusCounter();
        List<Scenario> passedScenarios = new ArrayList<>();
        List<Scenario> failedScenarios = new ArrayList<>();
        long totalDuration = 0L;

        if (elements != null) {
            for (Scenario element : elements) {
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
        this.passedScenarios = passedScenarios;
        this.failedScenarios = failedScenarios;
        stepResults = new StepResults(allSteps, stepsCounter, totalDuration);
    }

    private void calculateScenarioStats(List<Scenario> passedScenarios, List<Scenario> failedScenarios, Scenario element) {
        if (element.isScenario()) {
            if (element.getStatus() == Status.PASSED) {
                passedScenarios.add(element);
            } else if (element.getStatus() == Status.FAILED) {
                failedScenarios.add(element);
            }
        }
    }


}
