[![Build Status](https://img.shields.io/travis/damianszczepanik/cucumber-reporting/master.svg?label=Travis%20bulid)](https://travis-ci.org/damianszczepanik/cucumber-reporting)
[![Build Status](https://img.shields.io/appveyor/ci/damianszczepanik/cucumber-reporting/master.svg?label=AppVeyor%20build)](https://ci.appveyor.com/project/damianszczepanik/cucumber-reporting/history)

[![Coverage Status](https://img.shields.io/codecov/c/github/damianszczepanik/cucumber-reporting/master.svg?label=Unit%20tests%20coverage)](https://codecov.io/github/damianszczepanik/cucumber-reporting)
[![SonarQube coverage](https://img.shields.io/sonar/http/nemo.sonarqube.org/net.masterthought:cucumber-reporting/coverage.svg?label=UI%20tests%20coverage)](http://nemo.sonarqube.org/overview/coverage?id=net.masterthought%3Acucumber-reporting)
[![SonarQube tech debt](https://img.shields.io/sonar/http/nemo.sonarqube.org/net.masterthought:cucumber-reporting/tech_debt.svg?label=Sonarqube%20tech%20debt)](http://nemo.sonarqube.org/overview/debt?id=net.masterthought%3Acucumber-reporting)
[![Coverity](https://scan.coverity.com/projects/6166/badge.svg?label=Coverity%20analysis)](https://scan.coverity.com/projects/damianszczepanik-cucumber-reporting)
[![Codacy](https://api.codacy.com/project/badge/grade/7f206992ed364f0896490057fdbdaa2e)](https://www.codacy.com/app/damianszczepanik/cucumber-reporting)
[![Maven Dependencies](https://www.versioneye.com/user/projects/55c5301d653762001a0035ed/badge.svg)](https://www.versioneye.com/user/projects/55c5301d653762001a0035ed?child=summary)

[![Maven Central](https://img.shields.io/maven-central/v/net.masterthought/cucumber-reporting.svg)](http://search.maven.org/#search|gav|1|g%3A%22net.masterthought%22%20AND%20a%3A%22cucumber-reporting%22)
[![License](https://img.shields.io/badge/license-GNU%20LGPL%20v2.1-blue.svg)](https://raw.githubusercontent.com/damianszczepanik/cucumber-reporting/master/LICENCE)

# Publish pretty [cucumber](http://cukes.info/) reports

This is a Java report publisher primarily created to publish cucumber reports on the Jenkins build server. It publishes pretty html reports showing the results of cucumber runs. It has been split out into a standalone package so it can be used for Jenkins and maven command line as well as any other packaging that might be useful.

## Background

Cucumber is a test automation tool following the principles of Behavioural Driven Design and living documentation. Specifications are written in a concise human readable form and executed in continuous integration.

This project allows you to publish the results of a cucumber run as pretty html reports. In order for this to work you must generate a cucumber json report. The project converts the json report into an overview html linking to separate feature file htmls with stats and results.

## Install

Add a maven dependency to your pom
```xml
<dependency>
    <groupId>net.masterthought</groupId>
    <artifactId>cucumber-reporting</artifactId>
    <version>(check version above)</version>
</dependency>
```

Read this if you need further [detailed install and configuration]
(https://github.com/jenkinsci/cucumber-reports-plugin/wiki/Detailed-Configuration) instructions for using the Jenkins version of this project

## Usage
```Java
File reportOutputDirectory = new File("target");
List<String> jsonFiles = new ArrayList<>();
jsonFiles.add("cucumber-report-1.json");
jsonFiles.add("cucumber-report-2.json");

String jenkinsBasePath = "";
String buildNumber = "1";
String projectName = "cucumber-jvm";
boolean skippedFails = true;
boolean pendingFails = false;
boolean undefinedFails = true;
boolean runWithJenkins = false;
boolean parallelTesting = false;

Configuration configuration = new Configuration(reportOutputDirectory, projectName);
// optionally only if you need
configuration.setStatusFlags(skippedFails, pendingFails, undefinedFails);
configuration.setParallelTesting(parallelTesting);
configuration.setJenkinsBasePath(jenkinsBasePath);
configuration.setRunWithJenkins(runWithJenkins);
configuration.setBuildNumber(buildNumber);

ReportBuilder reportBuilder = new ReportBuilder(jsonFiles, configuration);
reportBuilder.generateReports();
```
`skippedFails` means the build will be failed if any steps are in skipped status and similar happens for other flags. This only applies when running with Jenkins.
runWithJenkins means put in the links back to Jenkins in the report.

There is a feature overview page:

![feature overview page]
(https://github.com/damianszczepanik/cucumber-reporting/raw/master/.README/feature-overview.png)

And there are also feature specific results pages:

![feature specific page passing]
(https://github.com/damianszczepanik/cucumber-reporting/raw/master/.README/feature-passed.png)

And useful information for failures:

![feature specific page passing]
(https://github.com/damianszczepanik/cucumber-reporting/raw/master/.README/feature-failed.png)

If you have tags in your cucumber features you can see a tag overview:

![Tag overview]
(https://github.com/damianszczepanik/cucumber-reporting/raw/master/.README/tag-overview.png)

And you can drill down into tag specific reports:

![Tag report]
(https://github.com/damianszczepanik/cucumber-reporting/raw/master/.README/report-tag.png)


## Code quality

Once you developed your new feature or improvement you should test it by providing several unit or integration tests.

![codecov.io](https://codecov.io/gh/damianszczepanik/cucumber-reporting/branch/master/graphs/tree.svg)


## Contribution

Interested in contributing to the cucumber-reporting?  Great!  Start [here]
(https://github.com/damianszczepanik/cucumber-reporting).
