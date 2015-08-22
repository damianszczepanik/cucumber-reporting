package net.masterthought.cucumber.json;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sequences;

import net.masterthought.cucumber.ReportBuilder;
import net.masterthought.cucumber.util.Status;
import net.masterthought.cucumber.util.StatusCounter;
import net.masterthought.cucumber.util.Util;

public class Feature {

    private String id;
    private String name;
    private String uri;
    private String description;
    private String keyword;
    private Element[] elements;
    private Tag[] tags;
    private StepResults stepResults;
    private ScenarioResults scenarioResults;
    private String jsonFile = "";

    public String getDeviceName() {
        String name = "";
        String[] splitedJsonFile = jsonFile.split("_");
        if (splitedJsonFile.length > 1)
            name = splitedJsonFile[0].substring(0, splitedJsonFile[0].length() - 1);
      return name;
    }

    public void setJsonFile(String json){
        this.jsonFile = json;
    }

    public Sequence<Element> getElements() {
        return Sequences.sequence(elements).realise();
    }

    public String getFileName() {
        List<String> matches = new ArrayList<String>();
        for (String line : Splitter.onPattern("/|\\\\").split(uri)) {
            String modified = line.replaceAll("\\)|\\(", "");
            modified = StringUtils.deleteWhitespace(modified).trim();
            matches.add(modified);
        }

        List<String> lastElement = matches.subList(1, matches.size());

        matches = lastElement.isEmpty() ? matches : lastElement;
        String fileName = Joiner.on("-").join(matches); 

        //If we spect to have parallel executions, we add 
        if (ReportBuilder.isParallel() && !jsonFile.isEmpty()) {
            String[] splitedJsonFile = jsonFile.split("_");
            if (splitedJsonFile.length > 1)
                fileName = fileName + "-" + getDeviceName();
        }
        fileName = fileName + ".html";
        return fileName;
    }

    public String getUri(){
        return this.uri;
    }

    public boolean hasTags() {
        return Util.arrayNotEmpty(tags);
    }

    public boolean hasScenarios() {
        return !getElements().isEmpty();
    }

    public Sequence<String> getTagList() {
        return getTags().map(Tag.functions.getName());
    }

    public Sequence<Tag> getTags() {
        return Sequences.sequence(tags).realise();
    }

    public String getTagsList() {
        String result = "<div class=\"feature-tags\"></div>";
        if (Util.arrayNotEmpty(tags)) {
            List<String> str = getTagList().toList();
            List<String> tagList = new ArrayList<String>();
            for(String s : str) {
                String link = s.replace("@", "").trim() + ".html";
                String ref = "<a href=\"" + link + "\">" + s + "</a>";
                tagList.add(ref);
            }
            result = "<div class=\"feature-tags\">" +   StringUtils.join(tagList.toArray(), ",")+ "</div>";
        }
        return result;
    }

    public Status getStatus() {
        Sequence<Status> results = getElements().map(Element.Functions.status());
        return results.contains(Status.FAILED) ? Status.FAILED : Status.PASSED;
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
        List<Element> passedScenarios = new ArrayList<Element>();
        List<Element> failedScenarios = new ArrayList<Element>();
        long totalDuration = 0L;

        if (elements != null) {
            for (Element element : elements) {
                calculateScenarioStats(passedScenarios, failedScenarios, element);
                if (element.hasSteps()) {
                    Sequence<Step> steps = element.getSteps();
                    for (Step step : steps) {
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


    private class StepResults {
        private final List<Step> allSteps;
        private final StatusCounter statusCounter;
        private final long totalDuration;

        public StepResults(List<Step> allSteps, StatusCounter statusCounter, long totalDuration) {
            this.allSteps = allSteps;
            this.statusCounter = statusCounter;
            this.totalDuration = totalDuration;
        }

        public int getNumberOfSteps() {
            return allSteps.size();
        }

        public int getNumberOfPasses() {
            return statusCounter.getValueFor(Status.PASSED);
        }

        public int getNumberOfFailures() {
            return statusCounter.getValueFor(Status.FAILED);
        }

        public int getNumberOfUndefined() {
            return statusCounter.getValueFor(Status.UNDEFINED);
        }

        public int getNumberOfPending() {
            return statusCounter.getValueFor(Status.PENDING);
        }

        public int getNumberOfSkipped() {
            return statusCounter.getValueFor(Status.SKIPPED);
        }

        public int getNumberOfMissing() {
            return statusCounter.getValueFor(Status.MISSING);
        }

        public long getTotalDuration() {
            return totalDuration;
        }

        public String getTotalDurationAsString() {
            return Util.formatDuration(totalDuration);
        }
    }

    private class ScenarioResults {
        private List<Element> passedScenarios;
        private List<Element> failedScenarios;

        ScenarioResults(List<Element> passedScenarios, List<Element> failedScenarios) {
            this.passedScenarios = passedScenarios;
            this.failedScenarios = failedScenarios;
        }

        public int getNumberOfScenariosPassed() {
            return passedScenarios.size();
        }

        public int getNumberOfScenariosFailed() {
            return failedScenarios.size();
        }

    }
}
