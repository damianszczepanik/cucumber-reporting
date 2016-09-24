package net.masterthought.cucumber;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.apache.commons.lang.StringUtils;

public class Configuration {

    private static final String EMBEDDINGS_DIRECTORY = "embeddings";

    private boolean parallelTesting;
    private String jenkinsBasePath;
    private boolean runWithJenkins;

    private File reportDirectory;

    private File trendsStatsFile;
    private String buildNumber;
    private String projectName;

    private Collection<Pattern> tagsToExcludeFromChart = new ArrayList<>();

    public Configuration(File reportOutputDirectory, String projectName) {
        this.reportDirectory = reportOutputDirectory;
        this.projectName = projectName;
    }

    public boolean isParallelTesting() {
        return parallelTesting;
    }

    public void setParallelTesting(boolean parallelTesting) {
        this.parallelTesting = parallelTesting;
    }

    public String getJenkinsBasePath() {
        return StringUtils.defaultString(jenkinsBasePath);
    }

    public void setJenkinsBasePath(String jenkinsBase) {
        this.jenkinsBasePath = jenkinsBase;
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

    public File getTrendsStatsFile() {
        return trendsStatsFile;
    }

    public void setTrendsStatsFile(File trendsStatsFile) {
        this.trendsStatsFile = trendsStatsFile;
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
        return new File(getReportDirectory().getAbsolutePath(), ReportBuilder.BASE_DIRECTORY
                + File.separatorChar + Configuration.EMBEDDINGS_DIRECTORY);
    }

    /**
     * @return Patterns to be used to filter out tags in the 'Tags Overview' chart. Returns an empty list by default.
     */
    public Collection<Pattern> getTagsToExcludeFromChart() {
        return tagsToExcludeFromChart;
    }

    /**
     * Stores the regex patterns to be used for filtering out tags from the 'Tags Overview' chart
     *
     * @param patterns Regex patterns to match against tags
     * @throws ValidationException when any of the given strings is not a valid regex pattern.
     */
    public void setTagsToExcludeFromChart(String... patterns) throws ValidationException {
        for (String pattern : patterns) {
            try {
                tagsToExcludeFromChart.add(Pattern.compile(pattern));
            } catch (PatternSyntaxException e) {
                throw new ValidationException(e);
            }
        }
    }
}
