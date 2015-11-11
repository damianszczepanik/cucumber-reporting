package net.masterthought.cucumber.json.support;

import org.apache.commons.lang3.ArrayUtils;

import net.masterthought.cucumber.json.Scenario;

public class ScenarioTag {

    private final Scenario scenario;
    private final String featureFileName;

    public ScenarioTag(Scenario scenario, String featureFileName) {
        this.scenario = scenario;
        this.featureFileName = featureFileName;
    }

    public Scenario getScenario() {
        return scenario;
    }

    public String getFeatureFileName() {
        return featureFileName;
    }

    public boolean hasSteps() {
        return ArrayUtils.isNotEmpty(scenario.getSteps());
    }
    
}
