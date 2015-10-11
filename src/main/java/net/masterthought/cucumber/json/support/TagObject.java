package net.masterthought.cucumber.json.support;

import java.util.ArrayList;
import java.util.List;

import net.masterthought.cucumber.json.Scenario;
import net.masterthought.cucumber.json.Step;
import net.masterthought.cucumber.util.Util;

public class TagObject {

    private final String tagName;
    private final List<ScenarioTag> scenarios = new ArrayList<>();
    private final List<Scenario> elements = new ArrayList<>();

    private final String fileName;

    public String getTagName() {
        return tagName;
    }

    public String getFileName() {
        return fileName;
    }

    public List<ScenarioTag> getScenarios() {
        return scenarios;
    }

    public void addScenarios(List<ScenarioTag> scenarioTagList) {
        this.scenarios.addAll(scenarioTagList);
    }

    public TagObject(String tagName) {
        this.tagName = tagName;

        // eliminate characters that might be invalid as a file name
        fileName = tagName.replace("@", "").replaceAll(":", "-").trim() + ".html";
    }

    public Integer getNumberOfScenarios() {
        int scenarioCounter = 0;
        for (ScenarioTag scenarioTag : this.scenarios) {
            if (!scenarioTag.getScenario().isBackground()) {
                scenarioCounter++;
            }
        }
        return scenarioCounter;
    }

    public Integer getNumberOfPassingScenarios() {
        return getNumberOfScenariosForStatus(Status.PASSED);
    }

    public Integer getNumberOfFailingScenarios() {
        return getNumberOfScenariosForStatus(Status.FAILED);
    }


    private Integer getNumberOfScenariosForStatus(Status status) {
        int scenarioCounter = 0;
        for (ScenarioTag scenarioTag : this.scenarios) {
            if (!scenarioTag.getScenario().isBackground()) {
                if (scenarioTag.getScenario().getStatus().equals(status)) {
                    scenarioCounter++;
                }
            }
        }
        return scenarioCounter;
    }

    public String getDurationOfSteps() {
        long duration = 0;
        for (ScenarioTag scenarioTag : scenarios) {
            if (scenarioTag.hasSteps()) {
                for (Step step : scenarioTag.getScenario().getSteps()) {
                    duration += step.getDuration();
                }
            }
        }
        return Util.formatDuration(duration);
    }

    public int getNumberOfSteps() {
        int totalSteps = 0;
        for (ScenarioTag scenario : scenarios) {
            if (scenario.hasSteps()) {
                totalSteps += scenario.getScenario().getSteps().length;
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

    public List<Scenario> getElements() {
        for (ScenarioTag scenarioTag : scenarios) {
            elements.add(scenarioTag.getScenario());
        }
        return elements;
    }

    public Status getStatus() {
        for (Scenario element : elements) {
            if (element.getStatus() != Status.PASSED) {
                return Status.FAILED;
            }
        }
        return Status.PASSED;
    }

    public String getRawStatus() {
        return getStatus().toString().toLowerCase();
    }

}
