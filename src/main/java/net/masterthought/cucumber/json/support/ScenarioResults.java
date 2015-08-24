package net.masterthought.cucumber.json.support;

import java.util.List;

import net.masterthought.cucumber.json.Element;

public class ScenarioResults {

    private final Element[] passedScenarios;
    private final Element[] failedScenarios;

    public ScenarioResults(List<Element> passedScenarios, List<Element> failedScenarios) {
        this.passedScenarios = new Element[passedScenarios.size()];
        passedScenarios.toArray(this.passedScenarios);
        this.failedScenarios = new Element[failedScenarios.size()];
        failedScenarios.toArray(this.failedScenarios);
    }

    public int getNumberOfScenariosPassed() {
        return passedScenarios.length;
    }

    public int getNumberOfScenariosFailed() {
        return failedScenarios.length;
    }

}