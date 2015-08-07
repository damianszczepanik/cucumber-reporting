[![Build Status](https://img.shields.io/travis/damianszczepanik/cucumber-reporting/master.svg?style=flat)](https://travis-ci.org/damianszczepanik/cucumber-reporting)
[![Maven Central](https://img.shields.io/maven-central/v/net.masterthought/cucumber-reporting.svg)](http://search.maven.org/#search|gav|1|g%3A%22net.masterthought%22%20AND%20a%3A%22cucumber-reporting%22)

# Publish pretty [cucumber](http://cukes.info/) reports

This is a Java report publisher primarily created to publish cucumber reports on the Jenkins build server. It publishes pretty html reports showing the results of cucumber runs. It has been split out into a standalone package so it can be used for Jenkins and maven command line as well as any other packaging that might be useful.

[![Build Status](https://secure.travis-ci.org/masterthought/cucumber-reporting.png)](http://travis-ci.org/masterthought/cucumber-reporting)

## Background

Cucumber is a test automation tool following the principles of Behavioural Driven Design and living documentation. Specifications are written in a concise human readable form and executed in continuous integration.

This project allows you to publish the results of a cucumber run as pretty html reports. In order for this to work you must generate a cucumber json report. The project converts the json report into an overview html linking to separate feature file htmls with stats and results.

## Install

1. Add a maven dependency to your pom
```xml
<dependency>
    <groupId>net.masterthought</groupId>
    <artifactId>cucumber-reporting</artifactId>
    <version>0.1.0</version>
</dependency>
```
2. Or grab the jar file from the [downloads](http://www.masterthought.net/section/cucumber-reporting) page

Read this if you need further  [detailed install and configuration]
(https://github.com/masterthought/jenkins-cucumber-jvm-reports-plugin-java/wiki/Detailed-Configuration) instructions for using the Jenkins version of this project

## Release Notes

Release notes are [here](https://github.com/masterthought/cucumber-reporting/wiki/Release-Notes)

## Usage

    File reportOutputDirectory = new File("target");
    List<String> list = new ArrayList<String>();
    list.add("cucumber-report1.json");
    list.add("cucumber-report2.json");

    String pluginUrlPath = "";
    String buildNumber = "1";
    String buildProject = "cucumber-jvm";
    boolean skippedFails = true;
    boolean pendingFails = true;
    boolean undefinedFails = true;
    boolean missingFails = true;
    boolean flashCharts = true;
    boolean runWithJenkins = false;
    boolean artifactsEnabled = false;
    String artifactConfig = "";
    boolean highCharts = false;

    ReportBuilder reportBuilder = new ReportBuilder(list, reportOutputDirectory, pluginUrlPath, buildNumber,
        buildProject, skippedFails, pendingFails, undefinedFails, missingFails, flashCharts, runWithJenkins, artifactsEnabled,
        artifactConfig, highCharts);
    reportBuilder.generateReports();

skippedFails means the build will be failed if any steps are in skipped status and undefinedFails means the build will be failed if any steps are in undefined status. This only applies when running with Jenkins.
flashCharts means either use the default flashcharts or use the D3 javascript charts. runWithJenkins means put in the links back to Jenkins in the report.

There is a feature overview page:

![feature overview page]
(https://github.com/masterthought/cucumber-reporting/raw/master/.README/feature-overview.png)

And there are also feature specific results pages:

![feature specific page passing]
(https://github.com/masterthought/cucumber-reporting/raw/master/.README/feature-passed.png)

And useful information for failures:

![feature specific page passing]
(https://github.com/masterthought/cucumber-reporting/raw/master/.README/feature-failed.png)

If you have tags in your cucumber features you can see a tag overview:

![Tag overview]
(https://github.com/masterthought/cucumber-reporting/raw/master/.README/tag-overview.png)

And you can drill down into tag specific reports:

![Tag report]
(https://github.com/masterthought/cucumber-reporting/raw/master/.README/tag-report.png)

## Develop

Interested in contributing to the cucumber-reporting?  Great!  Start [here]
(https://github.com/masterthought/cucumber-reporting).
