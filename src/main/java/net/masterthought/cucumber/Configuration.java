package net.masterthought.cucumber;

import java.io.File;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import net.masterthought.cucumber.json.support.Status;
import net.masterthought.cucumber.presentation.PresentationMode;
import net.masterthought.cucumber.reducers.ReducingMethod;
import net.masterthought.cucumber.sorting.SortingMethod;
import org.apache.commons.lang.StringUtils;
import org.checkerframework.checker.nullness.qual.NonNull;

public class Configuration {

    private static final String EMBEDDINGS_DIRECTORY = "embeddings";

    private File reportDirectory;

    private File trendsFile;
    private int trendsLimit;
    private String buildNumber;
    private String projectName;
    private String directorySuffix;

    private List<Map.Entry<String, String>> classifications = new ArrayList<>();

    private Collection<Pattern> tagsToExcludeFromChart = new ArrayList<>();
    private SortingMethod sortingMethod = SortingMethod.NATURAL;
    private List<ReducingMethod> reducingMethods = new ArrayList<>();

    private List<PresentationMode> presentationModes = new ArrayList<>();
    private List<String> classificationFiles;

    private Set<Status> notFailingStatuses = Collections.emptySet();

    private Map<String, String> qualifiers = new HashMap<>();

    private List<String> customCssFiles = new ArrayList<>();
    private List<String> customJsFiles = new ArrayList<>();

    public Configuration(File reportDirectory, String projectName) {
        this.reportDirectory = reportDirectory;
        this.projectName = projectName;
    }

    /**
     * Returns directory where the report should be stored.
     *
     * @return directory for the report
     */
    public File getReportDirectory() {
        return reportDirectory;
    }

    /**
     * Returns file with history with trends.
     *
     * @return file with trends
     */
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
     *
     * @param trendsFile file with trends
     */
    public void setTrendsStatsFile(File trendsFile) {
        setTrends(trendsFile, 0);
    }

    /**
     * Returns number of historical reports presented by trends.
     *
     * @return number of reports in trends
     */
    public int getTrendsLimit() {
        return trendsLimit;
    }

    /**
     * Checks if the trends page should be generated and displayed.
     *
     * @return <code>true</code> if the page with trends should be displayed
     */
    public boolean isTrendsAvailable() {
        return getTrendsLimit() > -1 && isTrendsStatsFile();
    }

    /**
     * Sets configuration limit for trends.
     * When the limit is set to 0 then all items will be stored and displayed.
     * To disable saving and displaying trends page set to -1.
     * Otherwise number of previous builds is equal to provided limit.
     *
     * @param trendsFile file where information about previous builds is stored
     * @param limit      number of builds that should be presented (older builds are skipped)
     */
    public void setTrends(File trendsFile, int limit) {
        this.trendsFile = trendsFile;
        this.trendsLimit = limit;
    }

    /**
     * Gets the build number for this report.
     *
     * @return build number
     */
    public String getBuildNumber() {
        return buildNumber;
    }

    /**
     * Sets number of the build.
     *
     * @param buildNumber number of the build
     */
    public void setBuildNumber(String buildNumber) {
        this.buildNumber = buildNumber;
    }

    /**
     * Returns the project name.
     *
     * @return name of the project
     */
    public String getProjectName() {
        return projectName;
    }

    /**
     * Sets directory suffix.
     *
     * @param directorySuffix directory suffix
     */
    public void setDirectorySuffix(String directorySuffix) {
        this.directorySuffix = directorySuffix;
    }

    /**
     * Returns directory suffix
     *
     * @return directory suffix
     */
    public String getDirectorySuffix() {
        return StringUtils.defaultString(directorySuffix);
    }

    /**
     * Returns directory suffix with separator prepended if necessary
     *
     * @return directory suffix with prepended separator
     */
    public String getDirectorySuffixWithSeparator() {
        return StringUtils.isEmpty(directorySuffix) ? "" : ReportBuilder.SUFFIX_SEPARATOR + directorySuffix;
    }

    /**
     * Gets directory where the attachments are stored.
     *
     * @return directory for attachment
     */
    public File getEmbeddingDirectory() {
        return new File(getReportDirectory().getAbsolutePath(), ReportBuilder.BASE_DIRECTORY +
                this.getDirectorySuffixWithSeparator() + File.separatorChar + Configuration.EMBEDDINGS_DIRECTORY);
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
     * @param sortingMethod how the items should be sorted
     */
    public void setSortingMethod(SortingMethod sortingMethod) {
        this.sortingMethod = sortingMethod;
    }

    /**
     * Returns the default sorting method.
     */
    public SortingMethod getSortingMethod() {
        return this.sortingMethod;
    }

    /**
     * Sets how the report should be reduced, merged or modified.
     *
     * @param reducingMethod type of reduction
     */
    public void addReducingMethod(ReducingMethod reducingMethod) {
        this.reducingMethods.add(reducingMethod);
    }

    /**
     * Gets how the report should be reduced, merged or modified.
     *
     * @return type of reduction
     */
    public List<ReducingMethod> getReducingMethods() {
        return reducingMethods;
    }

    /**
     * Checks if the configuration has given {@link ReducingMethod} set.
     *
     * @param reducingMethod method to validate
     * @return <code>true</code> if method was set, otherwise <code>false</code>
     */
    public boolean containsReducingMethod(ReducingMethod reducingMethod) {
        return reducingMethods.contains(reducingMethod);
    }

    /**
     * Sets how the report should be presented.
     *
     * @param presentationMode method used for presentation
     */
    public void addPresentationModes(PresentationMode presentationMode) {
        this.presentationModes.add(presentationMode);
    }

    /**
     * Checks if the configuration has given {@link PresentationMode} set.
     *
     * @param presentationMode method used for presentation
     * @return <code>true</code> if mode was set, otherwise <code>false</code>
     */
    public boolean containsPresentationMode(PresentationMode presentationMode) {
        return presentationModes.contains(presentationMode);
    }

    /**
     * Adds properties files which house classifications in key value pairings. When these properties files get
     * processed these classifications get displayed on the main page of the report as metadata in the order in which
     * they appear within the file.
     */
    public void addClassificationFiles(List<String> classificationFiles) {
        this.classificationFiles = classificationFiles;
    }

    /**
     * Returns the list of properties files.
     */
    public List<String> getClassificationFiles() {
        return this.classificationFiles;
    }

    /**
     * Gets statuses which do not fail scenario.
     */
    public Set<Status> getNotFailingStatuses() {
        return notFailingStatuses;
    }

    /**
     * Sets {@link net.masterthought.cucumber.json.support.Status statuses}
     * of {@link net.masterthought.cucumber.json.Step steps} which should not fail the scenario.
     */
    public void setNotFailingStatuses(Set<Status> notFailingStatuses) {
        if (notFailingStatuses != null) {
            this.notFailingStatuses = notFailingStatuses;
        }
    }

    /**
     * Sets explicit qualifier to use for the given json file.
     *
     * @param jsonFileName JSON file name - without the extension
     * @param qualifier    Qualifier to use
     */
    public void setQualifier(@NonNull String jsonFileName, @NonNull String qualifier) {
        qualifiers.put(jsonFileName, qualifier);
    }

    /**
     * Retrieves explicit qualifier to use for a given json file.
     *
     * @param jsonFileName JSON file name - without the extension
     * @return Qualifier specified for this file or <code>null</code> if none specified
     */
    public String getQualifier(@NonNull String jsonFileName) {
        return qualifiers.get(jsonFileName);
    }

    /**
     * Checks whether an explicit qualifier was specified for a given json file.
     *
     * @param jsonFileName JSON file name - without the extension
     * @return <code>true</code> if the qualifier was specified, <code>false</code> otherwise
     */
    public boolean containsQualifier(@NonNull String jsonFileName) {
        return qualifiers.containsKey(jsonFileName);
    }

    /**
     * Removes explicit qualifier for a given json file.
     *
     * @param jsonFileName JSON file name - without the extension
     */
    public void removeQualifier(@NonNull String jsonFileName) {
        qualifiers.remove(jsonFileName);
    }

    /**
     * Adds custom static css files to each html page.
     */
    public void addCustomCssFiles(List<String> customCssFiles) {
        this.customCssFiles = customCssFiles;
    }

    /**
     * Returns the list of custom css files.
     */
    public List<String> getCustomCssFiles() {
        return this.customCssFiles;
    }

    /**
     * Adds custom static js files to each html page.
     */
    public void addCustomJsFiles(List<String> customJsFiles) {
        this.customJsFiles = customJsFiles;
    }

    /**
     * Returns the list of custom js files.
     */
    public List<String> getCustomJsFiles() {
        return this.customJsFiles;
    }
}
