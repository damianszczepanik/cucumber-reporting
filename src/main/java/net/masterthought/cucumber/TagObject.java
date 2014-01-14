package net.masterthought.cucumber;

import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sequences;
import net.masterthought.cucumber.json.Element;
import net.masterthought.cucumber.json.Step;
import net.masterthought.cucumber.util.Util;

import java.util.ArrayList;
import java.util.List;

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
        return getNumberOfScenariosForStatus(Util.Status.PASSED);
    }

    public Integer getNumberOfFailingScenarios() {
        return getNumberOfScenariosForStatus(Util.Status.FAILED);
    }


    private Integer getNumberOfScenariosForStatus(Util.Status status) {
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
            if (Util.hasSteps(scenarioTag)) {
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
            if (Util.hasSteps(scenario)) {
                totalSteps += scenario.getScenario().getSteps().size();
            }
        }
        return totalSteps;
    }

    public int getNumberOfPasses() {
        return Util.findStatusCount(getStatuses(), Util.Status.PASSED);
    }

    public int getNumberOfFailures() {
        return Util.findStatusCount(getStatuses(), Util.Status.FAILED);
    }

    public int getNumberOfSkipped() {
        return Util.findStatusCount(getStatuses(), Util.Status.SKIPPED);
    }

    public int getNumberOfPending() {
        return Util.findStatusCount(getStatuses(), Util.Status.UNDEFINED);
    }

    private List<Util.Status> getStatuses() {
        List<Util.Status> statuses = new ArrayList<Util.Status>();
        for (ScenarioTag scenarioTag : scenarios) {
            if (Util.hasSteps(scenarioTag)) {
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

    public Util.Status getStatus() {
        Sequence<Util.Status> results = getElements().map(Element.functions.status());
        return results.contains(Util.Status.FAILED) ? Util.Status.FAILED : Util.Status.PASSED;
    }

    public String getRawStatus() {
        return getStatus().toString().toLowerCase();
    }
}
