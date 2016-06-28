package net.masterthought.cucumber;

import java.io.File;

public final class Configuration {

    private static final String EMBEDDINGS_DIRECTORY = "embeddings";

    private boolean failsIfSkipped;
    private boolean failsIFPending;
    private boolean failsIfUndefined;
    private boolean failsIfMissing;

    private boolean parallelTesting;
    //private String jenkinsBasePath;
    private String jenkinsBuildURL;
    private String jenkinsPreviousBuildURL;
    private String jenkinsProjectURL;


    private boolean runWithJenkins;

    private File reportDirectory;
    private String buildNumber;
    private String projectName;

    public Configuration(File reportOutputDirectory, String projectName) {
        this.reportDirectory = reportOutputDirectory;
        this.projectName = projectName;
    }

    public void setStatusFlags(boolean failsIfSkipped, boolean failsIFPending, boolean failsIfUndefined,
            boolean failsIfMissing) {
        this.failsIfSkipped = failsIfSkipped;
        this.failsIFPending = failsIFPending;
        this.failsIfUndefined = failsIfUndefined;
        this.failsIfMissing = failsIfMissing;
    }

    public boolean failsIfSkipped() {
        return failsIfSkipped;
    }

    public boolean failsIFPending() {
        return failsIFPending;
    }

    public boolean failsIfUndefined() {
        return failsIfUndefined;
    }

    public boolean failsIfMissing() {
        return failsIfMissing;
    }

    public boolean isParallelTesting() {
        return parallelTesting;
    }

    public void setParallelTesting(boolean parallelTesting) {
        this.parallelTesting = parallelTesting;
    }

//    public String getJenkinsBasePath() { return "";
//        return StringUtils.isEmpty(jenkinsBasePath) ? "/" : jenkinsBasePath;
//    }
//
//    public void setJenkinsBasePath(String jenkinsBase) {
//        this.jenkinsBasePath = jenkinsBase;
//    }

    public String getJenkinsProjectURL() {
        return jenkinsProjectURL;
    }

    public void setJenkinsProjectURL(String jenkinsProjectURL) {
        this.jenkinsProjectURL = jenkinsProjectURL;
    }

    public String getJenkinsBuildURL() {
        return jenkinsBuildURL;
    }

    public void setJenkinsBuildURL(String jenkinsBuildURL) {
        this.jenkinsBuildURL = jenkinsBuildURL;
    }

    public String getJenkinsPreviousBuildURL() {
        return jenkinsPreviousBuildURL;
    }

    public void setJenkinsPreviousBuildURL(String jenkinsPreviousBuildURL) {
        this.jenkinsPreviousBuildURL = jenkinsPreviousBuildURL;
    }

    public boolean isRunWithJenkins() {
        return runWithJenkins;
    }

    public void setRunWithJenkins(boolean runWithJenkins) {
        this.runWithJenkins = runWithJenkins;
    }

    public File getReportDirectory() {
        return reportDirectory;
    }

    public String getBuildNumber() {
        return buildNumber;
    }

    public void setBuildNumber(String buildNumber) {
        this.buildNumber = buildNumber;
    }

    public String getProjectName() {
        return projectName;
    }

    public File getEmbeddingDirectory() {
        return new File(getReportDirectory().getAbsolutePath(), Configuration.EMBEDDINGS_DIRECTORY);

    }
}
