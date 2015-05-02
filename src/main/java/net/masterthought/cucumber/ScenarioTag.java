package net.masterthought.cucumber;

import net.masterthought.cucumber.json.Element;

import com.googlecode.totallylazy.predicates.LogicalPredicate;

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

    public boolean hasSteps() {
        return !scenario.getSteps().isEmpty();
    }
    
    public static class Predicates {

            public static LogicalPredicate<ScenarioTag> scenarioExists(final String fileUri, final String name) {
                return new LogicalPredicate<ScenarioTag>() {
                    @Override
                    public boolean matches(ScenarioTag scenarioTag) {
                        return scenarioTag.equals(fileUri) && scenarioTag.equals(name);
                    }
                };
            }
        }
}
