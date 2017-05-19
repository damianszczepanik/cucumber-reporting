package net.masterthought.cucumber;

import java.io.File;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import net.masterthought.cucumber.sorting.SoringMethod;

public class Configuration {

    private static final String EMBEDDINGS_DIRECTORY = "embeddings";

    private boolean parallelTesting;
    private boolean runWithJenkins;

    private File reportDirectory;

    private File trendsFile;
    private int trendsLimit;
    private String buildNumber;
    private String projectName;

    private List<Map.Entry<String, String>> classifications = new ArrayList<>();

    private Collection<Pattern> tagsToExcludeFromChart = new ArrayList<>();
    private SoringMethod soringMethod = SoringMethod.NATURAL;

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
        return trendsFile;
    }

    /**
     * Checks if the file for the trends was set.
     *
     * @return <code>true</code> if the file location was provided, otherwise <code>false</code>
     */
    public boolean isTrendsStatsFile() {
        return trendsFile != null;
    }

    /**
     * Calls {@link #setTrends(File, int)} with zero limit.
     * @param trendsFile file with trends
     */
    public void setTrendsStatsFile(File trendsFile) {
        setTrends(trendsFile, 0);
    }

    public int getTrendsLimit() {
        return trendsLimit;
    }

    /**
     * Sets configuration for trends. When the limit is set to 0 then all items will be displayed.
     *
     * @param trendsFile  file where information about previous builds is stored
     * @param trendsLimit number of builds that should be presented (older builds are skipped)
     */
    public void setTrends(File trendsFile, int trendsLimit) {
        this.trendsFile = trendsFile;
        this.trendsLimit = trendsLimit;
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
    public void setTagsToExcludeFromChart(String... patterns) {
        for (String pattern : patterns) {
            try {
                tagsToExcludeFromChart.add(Pattern.compile(pattern));
            } catch (PatternSyntaxException e) {
                throw new ValidationException(e);
            }
        }
    }

    /**
     * Adds metadata that will be displayed at the main page of the report. It is useful when there is a few reports are
     * generated at the same time but with different parameters/configurations.
     *
     * @param name  name of the property
     * @param value value of the property
     */
    public void addClassifications(String name, String value) {
        classifications.add(new AbstractMap.SimpleEntry<>(name, value));
    }

    /**
     * Returns the classification for the report.
     */
    public List<Map.Entry<String, String>> getClassifications() {
        return classifications;
    }

    /**
     * Configure how items will be sorted in the report by default.
     *
     * @param soringMethod how the items should be sorted
     */
    public void setSortingMethod(SoringMethod soringMethod) {
        this.soringMethod = soringMethod;
    }

    /**
     * Returns the default sorting method.
     */
    public SoringMethod getSoringMethod() {
        return this.soringMethod;
    }
}
