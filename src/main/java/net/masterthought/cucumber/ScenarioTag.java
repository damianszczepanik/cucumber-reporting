package net.masterthought.cucumber;

import net.masterthought.cucumber.json.Element;

public class ScenarioTag {

    private Element scenario;
    private String parentFeatureUri;

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
}
