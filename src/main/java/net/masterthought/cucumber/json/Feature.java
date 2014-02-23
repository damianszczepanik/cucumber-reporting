package net.masterthought.cucumber.json;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.googlecode.totallylazy.Function1;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sequences;
import net.masterthought.cucumber.util.Util;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class Feature {

    private String name;
    private String uri;
    private String description;
    private String keyword;
    private Element[] elements;
    private Tag[] tags;
    private StepResults stepResults;
    private ScenarioResults scenarioResults;


    public Feature() {

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

        List<String> sublist = matches.subList(1, matches.size());

        matches = (sublist.size() == 0) ? matches : sublist;
        String fileName = Joiner.on("-").join(matches) + ".html";
        return fileName;
    }

    public boolean hasTags() {
        return Util.itemExists(tags);
    }

    public Sequence<String> getTagList() {
        return getTags().map(Tag.functions.getName());
    }

    public Sequence<Tag> getTags() {
        return Sequences.sequence(tags).realise();
    }

    public String getTagsList() {
        String result = "<div class=\"feature-tags\"></div>";
        if (Util.itemExists(tags)) {
            String tagList = StringUtils.join(getTagList().toList().toArray(), ",");
            result = "<div class=\"feature-tags\">" + tagList + "</div>";
        }
        return result;
    }

    public Util.Status getStatus() {
        Sequence<Util.Status> results = getElements().map(Element.functions.status());
        return results.contains(Util.Status.FAILED) ? Util.Status.FAILED : Util.Status.PASSED;
    }

    public String getName() {
        return Util.itemExists(name) ? Util.result(getStatus()) + "<div class=\"feature-line\"><span class=\"feature-keyword\">" + keyword + ":</span> " + name + "</div>" + Util.closeDiv() : "";
    }

    public String getRawName() {
        return Util.itemExists(name) ? name : "";
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
        if (Util.itemExists(elements)) {
            List<Element> elementList = new ArrayList<Element>();
            for (Element element : elements) {
                if (!element.getKeyword().equals("Background")) {
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
        List<Step> passedSteps = new ArrayList<Step>();
        List<Step> failedSteps = new ArrayList<Step>();
        List<Step> skippedSteps = new ArrayList<Step>();
        List<Step> undefinedSteps = new ArrayList<Step>();
        List<Step> pendingSteps = new ArrayList<Step>();
        List<Step> missingSteps = new ArrayList<Step>();
        List<Element> passedScenarios = new ArrayList<Element>();
        List<Element> failedScenarios = new ArrayList<Element>();
        Long totalDuration = 0l;
        if (Util.itemExists(elements)) {
            for (Element element : elements) {
                calculateScenarioStats(passedScenarios, failedScenarios, element);
                if (Util.hasSteps(element)) {
                    Sequence<Step> steps = element.getSteps();
                    for (Step step : steps) {
                        allSteps.add(step);
                        Util.Status stepStatus = step.getStatus();
                        passedSteps = Util.setStepStatus(passedSteps, step, stepStatus, Util.Status.PASSED);
                        failedSteps = Util.setStepStatus(failedSteps, step, stepStatus, Util.Status.FAILED);
                        skippedSteps = Util.setStepStatus(skippedSteps, step, stepStatus, Util.Status.SKIPPED);
                        undefinedSteps = Util.setStepStatus(undefinedSteps, step, stepStatus, Util.Status.UNDEFINED);
                        pendingSteps = Util.setStepStatus(pendingSteps, step, stepStatus, Util.Status.PENDING);
                        missingSteps = Util.setStepStatus(missingSteps, step, stepStatus, Util.Status.MISSING);
                        totalDuration = totalDuration + step.getDuration();
                    }
                }
            }
        }
        scenarioResults = new ScenarioResults(passedScenarios, failedScenarios);
        stepResults = new StepResults(allSteps, passedSteps, failedSteps, skippedSteps, pendingSteps, missingSteps, undefinedSteps, totalDuration);
    }

    private void calculateScenarioStats(List<Element> passedScenarios, List<Element> failedScenarios, Element element) {
        if (!element.getKeyword().equals("Background")) {
            if (element.getStatus() == Util.Status.PASSED) {
                passedScenarios.add(element);
            } else if (element.getStatus() == Util.Status.FAILED) {
                failedScenarios.add(element);
            }
        }
    }

    class StepResults {

        List<Step> allSteps;
        List<Step> passedSteps;
        List<Step> failedSteps;
        List<Step> skippedSteps;
        List<Step> undefinedSteps;
        List<Step> pendingSteps;
        List<Step> missingSteps;
        Long totalDuration;

        public StepResults(List<Step> allSteps, List<Step> passedSteps, List<Step> failedSteps, List<Step> skippedSteps, List<Step> pendingSteps, List<Step> missingSteps, List<Step> undefinedSteps, Long totalDuration) {
            this.allSteps = allSteps;
            this.passedSteps = passedSteps;
            this.failedSteps = failedSteps;
            this.skippedSteps = skippedSteps;
            this.undefinedSteps = undefinedSteps;
            this.pendingSteps = pendingSteps;
            this.missingSteps = missingSteps;
            this.totalDuration = totalDuration;
        }

        public int getNumberOfSteps() {
            return allSteps.size();
        }

        public int getNumberOfPasses() {
            return passedSteps.size();
        }

        public int getNumberOfFailures() {
            return failedSteps.size();
        }

        public int getNumberOfUndefined() {
            return undefinedSteps.size();
        }

        public int getNumberOfPending() {
            return pendingSteps.size();
        }

        public int getNumberOfSkipped() {
            return skippedSteps.size();
        }

        public int getNumberOfMissing() {
            return missingSteps.size();
        }

        public long getTotalDuration() {
            return totalDuration;
        }

        public String getTotalDurationAsString() {
            return Util.formatDuration(totalDuration);
        }
    }

    class ScenarioResults {
        List<Element> passedScenarios;
        List<Element> failedScenarios;

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
