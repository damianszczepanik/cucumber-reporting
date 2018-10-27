[![Travis Status](https://img.shields.io/travis/damianszczepanik/cucumber-reporting/master.svg?label=Travis%20bulid)](https://travis-ci.org/damianszczepanik/cucumber-reporting)
[![AppVeyor Status](https://img.shields.io/appveyor/ci/damianszczepanik/cucumber-reporting/master.svg?label=AppVeyor%20build)](https://ci.appveyor.com/project/damianszczepanik/cucumber-reporting/history)
[![Shippable Status](https://img.shields.io/shippable/5844689c9d1f3e0f0057631a/master.svg?label=Shippable%20build)](https://app.shippable.com/projects/5844689c9d1f3e0f0057631a)

[![Coverage Status](https://img.shields.io/codecov/c/github/damianszczepanik/cucumber-reporting/master.svg?label=Unit%20tests%20coverage)](https://codecov.io/github/damianszczepanik/cucumber-reporting)
[![Sonarqube coverage](https://sonarqube.com/api/badges/measure?key=com.github.dannil:scb-java-client&metric=coverage)](https://sonarcloud.io/dashboard/index/net.masterthought:cucumber-reporting)
[![Sonarqube tech debt](https://sonarqube.com/api/badges/measure?key=com.github.dannil:scb-java-client&metric=sqale_debt_ratio)](https://sonarcloud.io/dashboard/index/net.masterthought:cucumber-reporting)
[![Coverity](https://scan.coverity.com/projects/6166/badge.svg?label=Coverity%20analysis)](https://scan.coverity.com/projects/damianszczepanik-cucumber-reporting)
[![Codacy](https://api.codacy.com/project/badge/grade/7f206992ed364f0896490057fdbdaa2e)](https://www.codacy.com/app/damianszczepanik/cucumber-reporting)
[![Codebeat](https://codebeat.co/badges/cb097d5a-280a-4867-8120-d6f03a874861)](https://codebeat.co/projects/github-com-damianszczepanik-cucumber-reporting)
[![Vulnerabilities](https://snyk.io/test/github/damianszczepanik/cucumber-reporting/badge.svg)](https://snyk.io/org/damianszczepanik/project/6a2fe301-d56c-49e7-8c78-cd3ff09c3828)

[![Maven Central](https://img.shields.io/maven-central/v/net.masterthought/cucumber-reporting.svg)](http://search.maven.org/#search|gav|1|g%3A%22net.masterthought%22%20AND%20a%3A%22cucumber-reporting%22)
[![License](https://img.shields.io/badge/license-GNU%20LGPL%20v2.1-blue.svg)](https://raw.githubusercontent.com/damianszczepanik/cucumber-reporting/master/LICENCE)
[![Contributors](https://img.shields.io/github/contributors/damianszczepanik/cucumber-reporting.svg)](https://github.com/damianszczepanik/cucumber-reporting/graphs/contributors)

# Publish pretty [cucumber](http://cukes.info/) reports

This is a Java report publisher primarily created to publish cucumber reports on the Jenkins build server.
It publishes pretty html reports with charts showing the results of cucumber runs. It has been split out into a standalone package so it can be used for Jenkins and maven command line as well as any other packaging that might be useful. Generated report has no dependency so can be viewed offline.

## Background

Cucumber is a test automation tool following the principles of Behavioural Driven Design and living documentation. Specifications are written in a concise human readable form and executed in continuous integration.

This project allows you to publish the results of a cucumber run as pretty html reports. In order for this to work you must generate a cucumber json report. The project converts the json report into an overview html linking to separate feature files with stats and results.

## Install

Add a maven dependency to your pom
```xml
<dependency>
    <groupId>net.masterthought</groupId>
    <artifactId>cucumber-reporting</artifactId>
    <version>(check version above)</version>
</dependency>
```

Read this if you need further [detailed configuration](https://github.com/jenkinsci/cucumber-reports-plugin/wiki/Detailed-Configuration) instructions for using the Jenkins version of this project

## Usage
```Java
File reportOutputDirectory = new File("target");
List<String> jsonFiles = new ArrayList<>();
jsonFiles.add("cucumber-report-1.json");
jsonFiles.add("cucumber-report-2.json");

String buildNumber = "1";
String projectName = "cucumberProject";
boolean runWithJenkins = false;

Configuration configuration = new Configuration(reportOutputDirectory, projectName);
// optional configuration - check javadoc
configuration.setRunWithJenkins(runWithJenkins);
configuration.setBuildNumber(buildNumber);
// addidtional metadata presented on main page
configuration.addClassifications("Platform", "Windows");
configuration.addClassifications("Browser", "Firefox");
configuration.addClassifications("Branch", "release/1.0");

// optionally add metadata presented on main page via properties file
List<String> classificationFiles = new ArrayList<>();
classificationFiles.add("properties-1.properties");
classificationFiles.add("properties-2.properties");
configuration.addClassificationFiles(classificationFiles);

ReportBuilder reportBuilder = new ReportBuilder(jsonFiles, configuration);
Reportable result = reportBuilder.generateReports();
// and here validate 'result' to decide what to do if report has failed
```
There is a feature overview page:

![feature overview page](https://github.com/damianszczepanik/cucumber-reporting/raw/master/.README/feature-overview.png)

And there are also feature specific results pages:

![feature specific page passing](https://github.com/damianszczepanik/cucumber-reporting/raw/master/.README/feature-passed.png)

And useful information for failures:

![feature specific page passing](https://github.com/damianszczepanik/cucumber-reporting/raw/master/.README/feature-failed.png)

If you have tags in your cucumber features you can see a tag overview:

![Tag overview](https://github.com/damianszczepanik/cucumber-reporting/raw/master/.README/tag-overview.png)

And you can drill down into tag specific reports:

![Tag report](https://github.com/damianszczepanik/cucumber-reporting/raw/master/.README/tag-report.png)

![Trends report](https://github.com/damianszczepanik/cucumber-reporting/raw/master/.README/trends.png)


## Code quality

Once you developed your new feature or improvement you should test it by providing several unit or integration tests.

![codecov.io](https://codecov.io/gh/damianszczepanik/cucumber-reporting/branch/master/graphs/tree.svg)

## BrowserStack
We use [BrowserStack](https://www.browserstack.com) to examine HTML report against different browsers and platforms.
![BrowserStack](https://github.com/damianszczepanik/cucumber-reporting/raw/master/.README/Browserstack-logo.png)

## Contribution

Interested in contributing to the cucumber-reporting?  Great!  Start [here](https://github.com/damianszczepanik/cucumber-reporting).
