package net.masterthought.cucumber;

import com.googlecode.totallylazy.Function1;
import com.googlecode.totallylazy.predicates.LogicalPredicate;
import net.masterthought.cucumber.json.Element;
import net.masterthought.cucumber.util.Util;

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

    public static class predicates {

            public static LogicalPredicate<ScenarioTag> scenarioExists(final String fileUri, final String name) {
                return new LogicalPredicate<ScenarioTag>() {
                    @Override
                    public boolean matches(ScenarioTag scenarioTag) {
                        return scenarioTag.equals(fileUri) && scenarioTag.equals(name);
                    }
                };
            }


//            public static Function1<ScenarioTag, Util.Status> status() {
//                return new Function1<ScenarioTag, Util.Status>() {
//                    @Override
//                    public Util.Status call(ScenarioTag scenarioTag) throws Exception {
//                        return step.getStatus();
//                    }
//                };
//            }
        }

}
