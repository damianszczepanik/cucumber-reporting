package net.masterthought.cucumber.json.support;

import org.apache.commons.lang3.ArrayUtils;

import net.masterthought.cucumber.json.Scenario;

public class ScenarioTag {

    private final Scenario scenario;
    private final String featureId;

    public ScenarioTag(Scenario scenario, String featureId) {
        this.scenario = scenario;
        this.featureId = featureId;
    }

    public Scenario getScenario() {
        return scenario;
    }

    public String getFeatureId() {
        return featureId;
    }

    public boolean hasSteps() {
        return ArrayUtils.isNotEmpty(scenario.getSteps());
    }
    
}
