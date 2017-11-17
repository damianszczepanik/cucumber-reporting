package net.masterthought.cucumber.generators;

import net.masterthought.cucumber.ReportParser;
import net.masterthought.cucumber.ReportResult;
import net.masterthought.cucumber.json.Element;
import net.masterthought.cucumber.json.Feature;
import net.masterthought.cucumber.json.TrendFeatures;
import net.masterthought.cucumber.json.TrendScenarios;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.NotImplementedException;

import net.masterthought.cucumber.Reportable;
import net.masterthought.cucumber.json.support.Status;
import net.masterthought.cucumber.json.support.StatusCounter;
import net.masterthought.cucumber.util.Util;
import org.apache.velocity.texen.util.FileUtil;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class OverviewReport implements Reportable {

    private long duration;

    private final StatusCounter featuresCounter = new StatusCounter();
    private final StatusCounter scenariosCounter = new StatusCounter();
    private final StatusCounter stepsCounter = new StatusCounter();
    private final TrendFeatures features = new TrendFeatures();
    private ReportResult reportResult;

    public void incFeaturesFor(Status status) {
        this.featuresCounter.incrementFor(status);
    }

    @Override
    public int getFeatures() {
        return featuresCounter.size();
    }

    @Override
    public int getPassedFeatures() {
        return featuresCounter.getValueFor(Status.PASSED);
    }

    @Override
    public int getFailedFeatures() {
        return featuresCounter.getValueFor(Status.FAILED);
    }

    public void incScenarioFor(Status status) {
        this.scenariosCounter.incrementFor(status);
    }

    @Override
    public int getScenarios() {
        return scenariosCounter.size();
    }

    @Override
    public int getPassedScenarios() {
        return scenariosCounter.getValueFor(Status.PASSED);
    }

    @Override
    public int getFailedScenarios() {
        return scenariosCounter.getValueFor(Status.FAILED);
    }

    public void incStepsFor(Status status) {
        this.stepsCounter.incrementFor(status);
    }

    @Override
    public int getSteps() {
        return stepsCounter.size();
    }

    @Override
    public int getPassedSteps() {
        return stepsCounter.getValueFor(Status.PASSED);
    }

    @Override
    public int getFailedSteps() {
        return stepsCounter.getValueFor(Status.FAILED);
    }

    @Override
    public int getSkippedSteps() {
        return stepsCounter.getValueFor(Status.SKIPPED);
    }

    @Override
    public int getUndefinedSteps() {
        return stepsCounter.getValueFor(Status.UNDEFINED);
    }

    @Override
    public int getPendingSteps() {
        return stepsCounter.getValueFor(Status.PENDING);
    }

    public void incDurationBy(long duration) {
        this.duration += duration;
    }

    @Override
    public long getDuration() {
        return duration;
    }

    @Override
    public String getFormattedDuration() {
        return Util.formatDuration(getDuration());
    }

    @Override
    public String getDeviceName() {
        throw new NotImplementedException();
    }

    @Override
    public String getName() {
        throw new NotImplementedException();
    }

    @Override
    public Status getStatus() {
        throw new NotImplementedException();
    }

    @Override
    public String[] getFeatureDetails() {
        //get all feature & scenario name
        List<Feature> features = TrendFeatures.getFeatures();
        String[] TrendFeatureScenario = new String[0];

        for (Feature feature : features){
            String featureName = feature.getName();
            Element[] scenarios = feature.getElements();
            for (Element scenario : scenarios){
                if(scenario.isScenario()){
                    String scenarioName = scenario.getName();
                    String scenarioStatus = scenario.getStatus().toString();
                    TrendFeatureScenario = (String[]) ArrayUtils.add(TrendFeatureScenario,featureName+";"+scenarioName+";"+scenarioStatus);
                }
            }
        }

        //TrendFeatureScenario = (String[]) ArrayUtils.add(TrendFeatureScenario,"feature1;scenario2;failed");
        //TrendFeatureScenario = (String[]) ArrayUtils.add(TrendFeatureScenario,"feature2;scenario2.1;failed");




        /*File file = new File("resource/scenario.json");
        String features = null;
        try {
            features = FileUtils.readFileToString(file,"utf-8");
            JSONObject jsonObject = new JSONObject(features);
            JSONArray jsonArray = jsonObject.getJSONArray("featuresDetail");

            for (int i =0; i < jsonArray.length(); i++){
                String feature = jsonArray.getJSONObject(0).getString("feature");
                JSONArray scenario = jsonArray.getJSONObject(0).getJSONArray("scenarios");
                String a = scenario.getJSONObject(0).getString("scenario");
                TrendScenarios trendScenarios = new TrendScenarios(scenario.getJSONObject(0).getString("scenario"),scenario.getJSONObject(1).getString("scenario"));
                //String name = trendScenarios.getScenarioName();
                //String status = trendScenarios.getStatus();
            }


        } catch (IOException e) {
            e.printStackTrace();
        }


        return features;*/
        return TrendFeatureScenario;
    }
}
