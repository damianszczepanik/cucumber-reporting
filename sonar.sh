#!/bin/bash
# the to the limitation in Travis configuration file, this must be executed as shell

if [ "${TRAVIS_BRANCH}" == "master" ] && [ "${TRAVIS_PULL_REQUEST}" == "false" ];
then
  # for master branch -> upload results to the server
  SONAR_MODE="publish"
else
  # for other branches -> perform only incremental results
  SONAR_MODE="issues"
fi

echo Running Sonar with mode ${SONAR_MODE} for pull request ${TRAVIS_PULL_REQUEST}
mvn clean org.jacoco:jacoco-maven-plugin:prepare-agent package sonar:sonar \
  -Dsonar.host.url=https://sonarqube.com -Dsonar.analysis.mode=${SONAR_MODE} -Dsonar.login=${SONAR_TOKEN};
