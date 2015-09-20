package net.masterthought.cucumber.json.support;

import org.apache.commons.lang3.ArrayUtils;

import net.masterthought.cucumber.json.Element;

public class ScenarioTag {

    private final Element scenario;
    private final String parentFeatureUri;

    public ScenarioTag(Element scenario, String parentFeatureUri) {
        this.scenario = scenario;
        this.parentFeatureUri = parentFeatureUri;
    }

    public Element getScenario() {
        return scenario;
    }

    public String getParentFeatureUri() {
        return parentFeatureUri;
    }

    public boolean hasSteps() {
        return ArrayUtils.isNotEmpty(scenario.getSteps());
    }
    
}
