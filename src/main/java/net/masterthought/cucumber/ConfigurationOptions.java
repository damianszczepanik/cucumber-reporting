package net.masterthought.cucumber;

import java.util.Map;

public final class ConfigurationOptions {

    public boolean skippedFailsBuildValue;
    public boolean pendingFailsBuildValue;
    public boolean undefinedFailsBuildValue;
    public boolean missingFailsBuildValue;
    public boolean artifactsEnabledValue;
    public Map<String, Artifact> artifactConfiguration;

    private static final ConfigurationOptions configuration = new ConfigurationOptions();

    public static ConfigurationOptions instance() {
        return configuration;
    }

    private ConfigurationOptions() {
    }

    public void setSkippedFailsBuild(boolean skippedFailsBuild) {
        skippedFailsBuildValue = skippedFailsBuild;
    }

    public void setPendingFailsBuild(boolean pendingFailsBuild) {
        pendingFailsBuildValue = pendingFailsBuild;
    }

    public void setUndefinedFailsBuild(boolean undefinedFailsBuild) {
        undefinedFailsBuildValue = undefinedFailsBuild;
    }

    public void setMissingFailsBuild(boolean missngFailsBuild) {
        missingFailsBuildValue = missngFailsBuild;
    }

    public void setArtifactsEnabled(boolean artifactsEnabled) {
        artifactsEnabledValue = artifactsEnabled;
    }

    public void setArtifactConfiguration(Map<String, Artifact> configuration) {
        artifactConfiguration = configuration;
    }

    public boolean skippedFailsBuild() {
        return skippedFailsBuildValue;
    }

    public boolean pendingFailsBuild() {
        return pendingFailsBuildValue;
    }

    public boolean undefinedFailsBuild() {
        return undefinedFailsBuildValue;
    }

    public boolean missingFailsBuild() {
        return missingFailsBuildValue;
    }

    public boolean artifactsEnabled() {
        return artifactsEnabledValue;
    }

    public Map<String, Artifact> artifactConfig() {
        return artifactConfiguration;
    }

}
