package net.masterthought.cucumber;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import net.masterthought.cucumber.json.support.Status;
import net.masterthought.cucumber.presentation.PresentationMode;
import net.masterthought.cucumber.reducers.ReducingMethod;
import net.masterthought.cucumber.sorting.SortingMethod;
import org.junit.Test;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class ConfigurationTest {

    private static final File outputDirectory = new File("abc");

    private final String projectName = "123";

    @Test
    public void getReportDirectory_ReturnsOutputDirectory() {

        // given
        Configuration configuration = new Configuration(outputDirectory, projectName);

        // when
        File dir = configuration.getReportDirectory();

        // then
        assertThat(dir).isEqualTo(outputDirectory);
    }

    @Test
    public void getTrendsStatsFile_ReturnsTrendsFile() {

        // given
        File file = new File("ble");
        Configuration configuration = new Configuration(outputDirectory, projectName);

        // when
        configuration.setTrendsStatsFile(file);

        // then
        assertThat(configuration.getTrendsStatsFile()).isEqualTo(file);
    }

    @Test
    public void isTrendsStatsFile_ChecksIfTrendsFileWasSet() {

        // given
        File file = new File("ble");
        Configuration configuration = new Configuration(outputDirectory, projectName);

        // when
        configuration.setTrendsStatsFile(file);

        // then
        assertThat(configuration.isTrendsStatsFile()).isTrue();
    }

    @Test
    public void getTrendsLimit_ReturnsLimitForTrends() {

        // given
        final int limit = 123;
        Configuration configuration = new Configuration(outputDirectory, projectName);

        // when
        configuration.setTrends(null, limit);

        // then
        assertThat(configuration.getTrendsLimit()).isEqualTo(limit);
    }

    @Test
    public void isTrendsAvailable_OnNoTrendsPage_ReturnsFalse() {

        // given
        final int limit = -1;
        File file = new File("ble");
        Configuration configuration = new Configuration(outputDirectory, projectName);

        // when
        configuration.setTrends(file, limit);

        // then
        assertThat(configuration.isTrendsAvailable()).isFalse();
    }

    @Test
    public void isTrendsAvailable_OnNoTrendsFile_ReturnsFalse() {

        // given
        final int limit = 10;
        Configuration configuration = new Configuration(outputDirectory, projectName);

        // when
        configuration.setTrends(null, limit);

        // then
        assertThat(configuration.isTrendsAvailable()).isFalse();
    }

    @Test
    public void getBuildNumber_ReturnsBuildNumber() {

        // given
        Configuration configuration = new Configuration(outputDirectory, projectName);
        String buildNumber = "123xyz";
        configuration.setBuildNumber(buildNumber);

        // when
        String build = configuration.getBuildNumber();

        // then
        assertThat(build).isEqualTo(buildNumber);
    }

    @Test
    public void getProjectName_ReturnsProjectName() {

        // given
        Configuration configuration = new Configuration(outputDirectory, projectName);

        // when
        String name = configuration.getProjectName();

        // then
        assertThat(name).isEqualTo(projectName);
    }

    @Test
    public void getTagsToExcludeFromChart_ReturnsEmptyList() {

        // given
        Configuration configuration = new Configuration(outputDirectory, projectName);

        // when
        Collection<Pattern> patterns = configuration.getTagsToExcludeFromChart();

        // then
        assertThat(patterns).isEmpty();
    }

    @Test
    public void getDirectorySuffix_ReturnsDirectorySuffix() {

        // given
        String directorySuffix = "test";
        Configuration configuration = new Configuration(outputDirectory, projectName);

        // when
        configuration.setDirectorySuffix(directorySuffix);

        // then
        assertThat(configuration.getDirectorySuffix()).isEqualTo(directorySuffix);
    }

    @Test
    public void getDirectorySuffixWithSeparator_ReturnsDirectorySuffixWithSeparator() {

        // given
        String directorySuffix = "test";
        Configuration configuration = new Configuration(outputDirectory, projectName);

        // when
        configuration.setDirectorySuffix(directorySuffix);

        // then
        assertThat(configuration.getDirectorySuffixWithSeparator()).isEqualTo(ReportBuilder.SUFFIX_SEPARATOR + directorySuffix);
    }

    @Test
    public void getDirectorySuffixWithSeparatorForEmptySuffix_ReturnsEmptyString() {

        // given
        Configuration configuration = new Configuration(outputDirectory, projectName);

        // then
        assertThat(configuration.getDirectorySuffixWithSeparator()).isEmpty();
    }

    @Test
    public void getQualifier_ReturnsQualifierWhenSet() {

        // given
        String jsonFile = "test";
        String qualifier = "test";
        Configuration configuration = new Configuration(outputDirectory, projectName);

        // when
        configuration.setQualifier(jsonFile, qualifier);

        // then
        assertThat(configuration.getQualifier(jsonFile)).isEqualTo(qualifier);
    }

    @Test
    public void getQualifier_ReturnsNullWhenNotSet() {

        // given
        String jsonFile = "test";
        Configuration configuration = new Configuration(outputDirectory, projectName);

        // then
        assertThat(configuration.getQualifier(jsonFile)).isNull();
    }

    @Test
    public void getQualifier_ReturnsNullWhenSetThenRemoved() {

        // given
        String jsonFile = "test";
        String qualifier = "test";
        Configuration configuration = new Configuration(outputDirectory, projectName);

        // when
        configuration.setQualifier(jsonFile, qualifier);
        configuration.removeQualifier(jsonFile);

        // then
        assertThat(configuration.getQualifier(jsonFile)).isNull();
    }

    @Test
    public void isQualifierSet_ReturnsTrueWhenSet() {

        // given
        String jsonFile = "test";
        String qualifier = "test";
        Configuration configuration = new Configuration(outputDirectory, projectName);

        // when
        configuration.setQualifier(jsonFile, qualifier);

        // then
        assertThat(configuration.containsQualifier(jsonFile)).isTrue();
    }

    @Test
    public void isQualifierSet_ReturnsTrueWhenNotSet() {

        // given
        String jsonFile = "test";
        Configuration configuration = new Configuration(outputDirectory, projectName);

        // then
        assertThat(configuration.containsQualifier(jsonFile)).isFalse();
    }

    @Test
    public void isQualifierSet_ReturnsTrueWhenSetThenRemoved() {

        // given
        String jsonFile = "test";
        String qualifier = "test";
        Configuration configuration = new Configuration(outputDirectory, projectName);

        // when
        configuration.setQualifier(jsonFile, qualifier);
        configuration.removeQualifier(jsonFile);

        // then
        assertThat(configuration.containsQualifier(jsonFile)).isFalse();
    }

    @Test
    public void getTagsToExcludeFromChart_addPatterns_ReturnsListWithAllPatterns() {

        // given
        Configuration configuration = new Configuration(outputDirectory, projectName);
        String somePattern = "@specificTagNameToExclude";
        String anotherPattern = "@some.Regex.Pattern";
        configuration.setTagsToExcludeFromChart(somePattern, anotherPattern);

        // when
        Collection<Pattern> patterns = configuration.getTagsToExcludeFromChart();

        // then
        assertThat(patterns).extractingResultOf("pattern").containsOnly(somePattern, anotherPattern);
    }

    @Test
    public void setTagsToExcludeFromChart_OnInvalidRegexPattern_ThrowsValidationException() {

        // given
        Configuration configuration = new Configuration(outputDirectory, projectName);

        // then & then
        assertThatThrownBy(() -> configuration.setTagsToExcludeFromChart("\\invalid.regex\\")).isInstanceOf(ValidationException.class);
    }

    @Test
    public void addClassifications_AddsClassification() {

        // given
        Configuration configuration = new Configuration(outputDirectory, projectName);
        final String classificationName = "Browser";
        final String classificationValue = "Firefox 1.0";

        // when
        configuration.addClassifications(classificationName, classificationValue);

        // then
        assertThat(configuration.getClassifications()).hasSize(1);
        Map.Entry<String, String> classification = configuration.getClassifications().get(0);
        assertThat(classification.getKey()).isEqualTo(classificationName);
        assertThat(classification.getValue()).isEqualTo(classificationValue);
    }

    @Test
    public void setSortingMethod_SetsSortingMethod() {

        // given
        Configuration configuration = new Configuration(outputDirectory, projectName);
        SortingMethod sortingMethod = SortingMethod.NATURAL;

        // then
        configuration.setSortingMethod(sortingMethod);

        // then
        assertThat(configuration.getSortingMethod()).isEqualTo(sortingMethod);
    }

    @Test
    public void addReducingMethod_AddsReducingMethod() {

        // given
        Configuration configuration = new Configuration(outputDirectory, projectName);
        ReducingMethod reducingMethod = ReducingMethod.MERGE_FEATURES_BY_ID;

        // when
        configuration.addReducingMethod(reducingMethod);

        // then
        assertThat(configuration.getReducingMethods()).containsOnly(reducingMethod);
    }

    @Test
    public void containsReducingMethod_ChecksExistenceOfReducingMethod() {

        // given
        Configuration configuration = new Configuration(outputDirectory, projectName);

        // when
        configuration.addReducingMethod(ReducingMethod.MERGE_FEATURES_BY_ID);

        // then
        assertThat(configuration.containsReducingMethod(ReducingMethod.MERGE_FEATURES_BY_ID)).isTrue();

    }

    @Test
    public void addPresentationMode_AddsPresentationMode() {

        // given
        Configuration configuration = new Configuration(outputDirectory, projectName);
        PresentationMode presentationMode = PresentationMode.EXPAND_ALL_STEPS;

        // when
        configuration.addPresentationModes(presentationMode);

        // then
        assertThat(configuration.containsPresentationMode(presentationMode)).isTrue();
    }

    @Test
    public void addClassificationFiles_addsPropertyFiles() {

        // given
        Configuration configuration = new Configuration(outputDirectory, projectName);
        List<String> propertiesFiles = new ArrayList<>();
        propertiesFiles.add("properties-1.properties");
        propertiesFiles.add("properties-2.properties");

        // when
        configuration.addClassificationFiles(propertiesFiles);

        // then
        List<String> returnedPropertiesFiles = configuration.getClassificationFiles();
        assertThat(returnedPropertiesFiles).hasSize(2);
        assertThat(returnedPropertiesFiles).containsExactly(
                ("properties-1.properties"),
                ("properties-2.properties")
        );
    }

    @Test
    public void getNotFailingStatuses_ReturnsNotFailingStatuses() {

        // given
        Configuration configuration = new Configuration(outputDirectory, projectName);
        Status notFailingStatus = Status.SKIPPED;
        configuration.setNotFailingStatuses(Collections.singleton(notFailingStatus));

        // when
        Set<Status> statuses = configuration.getNotFailingStatuses();

        // then
        assertThat(statuses).containsExactly(notFailingStatus);
    }

    @Test
    public void setNotFailingStatuses_SkipsNullValues() {

        // given
        Configuration configuration = new Configuration(outputDirectory, projectName);
        Status notFailingStatus = Status.SKIPPED;
        configuration.setNotFailingStatuses(Collections.singleton(notFailingStatus));

        // when
        configuration.setNotFailingStatuses(null);

        // then
        assertThat(configuration.getNotFailingStatuses()).containsExactly(notFailingStatus);
    }

    @Test
    public void addCustomCssFiles_addsPropertyFiles() {

        // given
        Configuration configuration = new Configuration(outputDirectory, projectName);
        List<String> cssFiles = new ArrayList<>();
        cssFiles.add("my-styling.css");
        cssFiles.add("my-other-styling.css");

        // when
        configuration.addCustomCssFiles(cssFiles);

        // then
        List<String> returnedCssFiles = configuration.getCustomCssFiles();
        assertThat(returnedCssFiles).containsExactly("my-styling.css", "my-other-styling.css");
    }

    @Test
    public void addCustomJsFiles_addsPropertyFiles() {

        // given
        Configuration configuration = new Configuration(outputDirectory, projectName);
        List<String> jsFiles = new ArrayList<>();
        jsFiles.add("custom-code.js");

        // when
        configuration.addCustomJsFiles(jsFiles);

        // then
        List<String> returnedJsFiles = configuration.getCustomJsFiles();
        assertThat(returnedJsFiles).containsExactly("custom-code.js");
    }
}
