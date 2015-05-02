package net.masterthought.cucumber;

import java.util.ArrayList;
import java.util.List;

import net.masterthought.cucumber.json.Element;
import net.masterthought.cucumber.json.Step;
import net.masterthought.cucumber.util.Status;
import net.masterthought.cucumber.util.Util;

import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sequences;

public class TagObject {

    private String tagName;
    private List<ScenarioTag> scenarios = new ArrayList<ScenarioTag>();
    private List<Element> elements = new ArrayList<Element>();

    public String getTagName() {
        return tagName;
    }

    public String getFileName() {
        return tagName.replace("@", "").trim() + ".html";
    }

    public List<ScenarioTag> getScenarios() {
        return scenarios;
    }

    public void setScenarios(List<ScenarioTag> scenarioTagList) {
        this.scenarios = scenarioTagList;
    }

    public TagObject(String tagName, List<ScenarioTag> scenarios) {
        this.tagName = tagName;
        this.scenarios = scenarios;
    }

    private void populateElements() {
        for (ScenarioTag scenarioTag : scenarios) {
            elements.add(scenarioTag.getScenario());
        }
    }

    public Integer getNumberOfScenarios() {
        List<ScenarioTag> scenarioTagList = new ArrayList<ScenarioTag>();
        for (ScenarioTag scenarioTag : this.scenarios) {
            if (!scenarioTag.getScenario().getKeyword().equals("Background")) {
                scenarioTagList.add(scenarioTag);
            }
        }
        return scenarioTagList.size();
    }

    public Integer getNumberOfPassingScenarios() {
        return getNumberOfScenariosForStatus(Status.PASSED);
    }

    public Integer getNumberOfFailingScenarios() {
        return getNumberOfScenariosForStatus(Status.FAILED);
    }


    private Integer getNumberOfScenariosForStatus(Status status) {
        List<ScenarioTag> scenarioTagList = new ArrayList<ScenarioTag>();
        for (ScenarioTag scenarioTag : this.scenarios) {
            if (!scenarioTag.getScenario().getKeyword().equals("Background")) {
                if (scenarioTag.getScenario().getStatus().equals(status)) {
                    scenarioTagList.add(scenarioTag);
                }
            }
        }
        return scenarioTagList.size();
    }

    public String getDurationOfSteps() {
        Long duration = 0L;
        for (ScenarioTag scenarioTag : scenarios) {
            if (scenarioTag.hasSteps()) {
                for (Step step : scenarioTag.getScenario().getSteps()) {
                    duration = duration + step.getDuration();
                }
            }
        }
        return Util.formatDuration(duration);
    }

    public int getNumberOfSteps() {
        int totalSteps = 0;
        for (ScenarioTag scenario : scenarios) {
            if (scenario.hasSteps()) {
                totalSteps += scenario.getScenario().getSteps().size();
            }
        }
        return totalSteps;
    }

    public int getNumberOfStatus(Status status) {
        return Util.findStatusCount(getStatuses(), status);
    }

    /** No-parameters method required for velocity template. */
    public int getNumberOfPasses() {
        return getNumberOfStatus(Status.PASSED);
    }

    /** No-parameters method required for velocity template. */
    public int getNumberOfFailures() {
        return getNumberOfStatus(Status.FAILED);
    }

    /** No-parameters method required for velocity template. */
    public int getNumberOfSkipped() {
        return getNumberOfStatus(Status.SKIPPED);
    }

    /** No-parameters method required for velocity template. */
    public int getNumberOfUndefined() {
        return getNumberOfStatus(Status.UNDEFINED);
    }

    /** No-parameters method required for velocity template. */
    public int getNumberOfMissing() {
        return getNumberOfStatus(Status.MISSING);
    }

    /** No-parameters method required for velocity template. */
    public int getNumberOfPending() {
        return getNumberOfStatus(Status.UNDEFINED);
    }

    private List<Status> getStatuses() {
        List<Status> statuses = new ArrayList<Status>();
        for (ScenarioTag scenarioTag : scenarios) {
            if (scenarioTag.hasSteps()) {
                for (Step step : scenarioTag.getScenario().getSteps()) {
                    statuses.add(step.getStatus());
                }
            }
        }
        return statuses;
    }

    public Sequence<Element> getElements() {
        populateElements();
        return Sequences.sequence(elements);
    }

    public Status getStatus() {
        Sequence<Status> results = getElements().map(Element.Functions.status());
        return results.contains(Status.FAILED) ? Status.FAILED : Status.PASSED;
    }

    public String getRawStatus() {
        return getStatus().toString().toLowerCase();
    }
}
