package net.masterthought.cucumber.json.support;

import org.apache.commons.lang3.ArrayUtils;

import net.masterthought.cucumber.json.Scenario;

public class ScenarioTag {

    private final Scenario scenario;
    private final String fetureFileName;

    public ScenarioTag(Scenario scenario, String fetureFileName) {
        this.scenario = scenario;
        this.fetureFileName = fetureFileName;
    }

    public Scenario getScenario() {
        return scenario;
    }

    public String getFetureFileName() {
        return fetureFileName;
    }

    public boolean hasSteps() {
        return ArrayUtils.isNotEmpty(scenario.getSteps());
    }
    
}
