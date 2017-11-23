package net.masterthought.cucumber;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by suci on 11/22/17.
 */
public class TrendTableRow {

    private String featureName;
    private String scenarioName;
    private ArrayList<String> statuses;

    public TrendTableRow(String featureName, String scenarioName){
        this.featureName = featureName;
        this.scenarioName = scenarioName;
        this.statuses = new ArrayList<>();
    }

    public String getScenarioName() {
        return scenarioName;
    }

    public List<String> getStatuses() {
        return statuses;
    }

    public void setStatus(String name){
        statuses.add(name);
    }

    public String getFeatureName(){
        return featureName;
    }

}
