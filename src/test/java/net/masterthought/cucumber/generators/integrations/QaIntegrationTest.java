package net.masterthought.cucumber.generators.integrations;

import net.masterthought.cucumber.*;
import net.masterthought.cucumber.json.Element;
import net.masterthought.cucumber.json.Feature;
import net.masterthought.cucumber.json.Step;
import net.masterthought.cucumber.json.Tag;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by suci on 11/16/17.
 */
public class QaIntegrationTest {

    private static ReportParser reportParser;
    private static ReportResult reportResult;

    public static String featureName;
    public static String featureDescription;
    public static Integer featureScenarios;
    public static String featureDeviceName;
    public static String featureStatus;

    public static String scenarioName;
    public static String scenarioStatus;

    private static String deviceName;

    private static final Element[] elements = new Element[0];
    private static final Tag[] tags = new Tag[0];
    private static final List<Element> scenarios = new ArrayList<>();

    public static void main(String[] args) {
        // Set report output directory
        File reportOutputDirectory = new File("target");
        List<String> jsonFiles = new ArrayList<>();
        jsonFiles.add("resource/cucumber.json");

        String buildNumber = "1";
        String projectName = "cucumberProject";
        boolean runWithJenkins = false;
        boolean parallelTesting = false;

        Configuration configuration = new Configuration(reportOutputDirectory, projectName);
        // optional configuration
        configuration.setParallelTesting(parallelTesting);
        configuration.setRunWithJenkins(runWithJenkins);
        configuration.setBuildNumber(buildNumber);
        // addidtional metadata presented on main page
        configuration.addClassifications("Platform", "Windows");
        configuration.addClassifications("Browser", "Firefox");
        configuration.addClassifications("Branch", "release/1.0");
        configuration.setTrends(new File("resource", "cucumber-trends_3.json"), 5);

// optionally add metadata presented on main page via properties file
/*      List<String> classificationFiles = new ArrayList<>();
        classificationFiles.add("properties-1.properties");
        classificationFiles.add("properties-2.properties");
        configuration.addClassificationFiles(classificationFiles);
*/


        ReportBuilder reportBuilder = new ReportBuilder(jsonFiles, configuration);
        Reportable result = reportBuilder.generateReports();
// and here validate 'result' to decide what to do
// if report has failed features, undefined steps etc

        //System.out.println(result);
        //System.out.println(result.getFeatures());



        // parse json files for results
        reportParser = new ReportParser(configuration);
        List<Feature> features = reportParser.parseJsonFiles(jsonFiles);
        reportResult = new ReportResult(features, configuration.getSortingMethod());
        Reportable reportable = reportResult.getFeatureReport();



        //System.out.println("Features: " + features.size());
        //System.out.println("Features: " + reportable.);

        // Process every feature
        for (Feature a : features) {

            featureName = a.getName();
            featureDescription = a.getDescription();
            featureScenarios = a.getScenarios();
            featureDeviceName = a.getDeviceName();
            featureStatus = a.getStatus().toString();

            //System.out.println("Feature: " + featureName);
            //System.out.println("DeviceName: " + featureDeviceName);
            //System.out.println("Description: " + featureDescription);
            //System.out.println("Scenarios: " + featureScenarios);
            //System.out.println("Status: " + featureStatus);
            //System.out.println("FailedFeatures: " + features.get(i).getFailedFeatures());
            //System.out.println("PassedFeatures: " + features.get(i).getPassedFeatures());
            //System.out.println("FailedScenarios: " + a.getFailedScenarios());
            //System.out.println("PassedScenarios: " + a.getPassedScenarios());
            //System.out.println("FailedSteps: " + a.getFailedSteps());
            //System.out.println("PassedSteps: " + a.getPassedSteps());
            //System.out.println("Elements: " + a.getElements().length);

            // Get feature scenarios (elements)
            Element[] scenarios = a.getElements();
            for (Element e : scenarios) {

                // Check that the element is a scenario
                //String elementKeyword = e.getKeyword();
                //if (elementKeyword.equals("Scenario")) {
                if (e.isScenario()) {

                    scenarioName = e.getName();
                    scenarioStatus = e.getStatus().toString();

                    System.out.println("-----------------------");
                    //System.out.println("Keyword: " + elementKeyword);
                    System.out.println("Scenario: " + scenarioName);
                    //System.out.println("DeviceName: " + featureDeviceName);
                    System.out.println("Feature: " + featureName);
                    System.out.println("ScenarioStatus: " + scenarioStatus);

                    // Get the steps for the scenario
                    Step[] steps = e.getSteps();
                    for (Step s : steps) {
                        // Only show the error information if the step status is failed
                        if (s.getResult().getStatus().toString().equals("FAILED")) {
                            System.out.println("Step: " + s.getName());
                            System.out.println("StepStatus: " + s.getResult().getStatus());
                            System.out.println("StepErrorMessageTitle: " + s.getResult().getErrorMessageTitle());
                            System.out.println("StepErrorMessage: " + s.getResult().getErrorMessage());
                        }
                    }
                }



            }

            //Integer elementAmmount = scenarios;
            //System.out.println("elementAmmount: " + elementAmmount);


            //System.out.println(scenarios.get(i));

/*
            for (int x = 0; x < features.get(i).getElements().length; x++) {
                //scenarios.add(features.get(i).getElements());
                System.out.println("Scenario: " + scenarios.get(x).getDescription());
            }
*/

            /*
            scenarios.add();
            for (int i = 0; i < scenarios.size(); i++) {
                System.out.println("Scenario: " + scenarios.get(i).getDescription());
            }
            */

/*
            featureName = features.get(i).getName();
            featureDescription = features.get(i).getDescription();
            featureScenarios = features.get(i).getScenarios();
            featureDeviceName = features.get(i).getDeviceName();
            featureStatus = features.get(i).getStatus().toString();

            System.out.println("Feature: " + featureName);
            //System.out.println("DeviceName: " + featureDeviceName);
            //System.out.println("Description: " + featureDescription);
            System.out.println("Scenarios: " + featureScenarios);
            System.out.println("Status: " + featureStatus);
            //System.out.println("FailedFeatures: " + features.get(i).getFailedFeatures());
            //System.out.println("PassedFeatures: " + features.get(i).getPassedFeatures());
            System.out.println("FailedScenarios: " + features.get(i).getFailedScenarios());
            System.out.println("PassedScenarios: " + features.get(i).getPassedScenarios());
            System.out.println("FailedSteps: " + features.get(i).getFailedSteps());
            System.out.println("PassedSteps: " + features.get(i).getPassedSteps());
            System.out.println("Elements: " + features.get(i).getElements().length);
            System.out.println("ReportFileName: " + features.get(i).getReportFileName());
            //for (int f = 0; f < features.get(i).; f++) {
            //    System.out.println(feature);
            //}
        }
*/


        /*
        for (Feature f: features) {
            List<Element> escenario = reportParser.
            for (Scenario f: features) {

            }
        }
        */
        /*
        System.out.println("......................");
        System.out.println(reportable.getFeatures());
        System.out.println(reportable.getPassedFeatures());
        System.out.println(reportable.getFailedFeatures());
        System.out.println(reportable.getScenarios());
        System.out.println(reportable.getPassedScenarios());
        System.out.println(reportable.getFailedScenarios());
        System.out.println(reportable.getSteps());
        System.out.println(reportable.getPassedSteps());
        System.out.println(reportable.getFailedSteps());
*/
        /*
        for (int i = 0; i < features.size(); i++) {
            featureName = features.get(i).getName();
            featureDescription = features.get(i).getDescription();
            featureScenarios = features.get(i).getScenarios();
            featureDeviceName = features.get(i).getDeviceName();
            featureStatus = features.get(i).getStatus().toString();
            System.out.println("-----------------------");
            System.out.println("Feature: " + featureName);
            //System.out.println("DeviceName: " + featureDeviceName);
            //System.out.println("Description: " + featureDescription);
            System.out.println("Scenarios: " + featureScenarios);
            System.out.println("Status: " + featureStatus);
            //System.out.println("FailedFeatures: " + features.get(i).getFailedFeatures());
            //System.out.println("PassedFeatures: " + features.get(i).getPassedFeatures());
            System.out.println("FailedScenarios: " + features.get(i).getFailedScenarios());
            System.out.println("PassedScenarios: " + features.get(i).getPassedScenarios());
            System.out.println("FailedSteps: " + features.get(i).getFailedSteps());
            System.out.println("PassedSteps: " + features.get(i).getPassedSteps());

            System.out.println(reportable.getFeatures());
            //for (int f = 0; f < features.get(i).; f++) {
            //    System.out.println(feature);
            //}*/

        }
    }

}
