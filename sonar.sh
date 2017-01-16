#!/bin/bash
# the to the limitation in Travis configuration file, this must be executed as shell

if [ "${TRAVIS_BRANCH}" == "master" ] && [ "${TRAVIS_PULL_REQUEST}" == "false" ];
then
  echo for master branch -> upload results to the server
  mvn clean org.jacoco:jacoco-maven-plugin:prepare-agent package sonar:sonar \
    -Dsonar.host.url=https://sonarqube.com -Dsonar.analysis.mode=publish -Dsonar.login=${SONAR_TOKEN};
else
  echo for other branches -> perform only incremental results
  mvn org.jacoco:jacoco-maven-plugin:prepare-agent package sonar:sonar \
    -Dsonar.host.url=https://sonarqube.com -Dsonar.analysis.mode=issues -Dsonar.login=${SONAR_TOKEN}
fi
