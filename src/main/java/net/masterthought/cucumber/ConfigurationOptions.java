package net.masterthought.cucumber;

public class ConfigurationOptions {

    public static boolean skippedFailsBuildValue;
    public static boolean undefinedFailsBuildValue;

    private ConfigurationOptions() {
        throw new AssertionError();
    }

    public static void setSkippedFailsBuild(boolean skippedFailsBuild) {
        skippedFailsBuildValue = skippedFailsBuild;
    }

    public static void setUndefinedFailsBuild(boolean undefinedFailsBuild) {
        undefinedFailsBuildValue = undefinedFailsBuild;
    }

    public static boolean skippedFailsBuild() {
        return skippedFailsBuildValue;
    }

    public static boolean undefinedFailsBuild() {
        return undefinedFailsBuildValue;
    }

}
