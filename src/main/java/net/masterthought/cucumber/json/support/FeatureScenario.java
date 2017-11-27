package net.masterthought.cucumber.json.support;

/**
 * Created by suci on 11/17/17.
 */
public class FeatureScenario {

    private String featureName;
    private String scenarioName;
    private String status;

    public FeatureScenario(){
        super();
    }

    public FeatureScenario(String featureName, String scenarioName, String status){
        this.featureName = featureName;
        this.scenarioName = scenarioName;
        this.status = status;
    }


    public String getFeatureName() {
        return featureName;
    }

    public String getScenarioName() {
        return scenarioName;
    }

    public String getStatus() {
        return status;
    }
}
