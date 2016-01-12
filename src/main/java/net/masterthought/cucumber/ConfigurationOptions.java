package net.masterthought.cucumber;

public final class ConfigurationOptions {

    public boolean skippedFailsBuildValue;
    public boolean pendingFailsBuildValue;
    public boolean undefinedFailsBuildValue;
    public boolean missingFailsBuildValue;

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
}
