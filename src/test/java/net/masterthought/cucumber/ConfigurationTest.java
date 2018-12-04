package net.masterthought.cucumber;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import net.masterthought.cucumber.reducers.ReducingMethod;
import net.masterthought.cucumber.sorting.SortingMethod;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class ConfigurationTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    private static final File outputDirectory = new File("abc");

    private final String projectName = "123";


    @Test
    public void isRunWithJenkins_ReturnsRunWithJenkins() {

        // given
        Configuration configuration = new Configuration(outputDirectory, projectName);
        boolean runWithJenkins = true;
        configuration.setRunWithJenkins(runWithJenkins);

        // when
        boolean run = configuration.isRunWithJenkins();

        // then
        assertThat(run).isEqualTo(runWithJenkins);
    }

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

        // then
        thrown.expect(ValidationException.class);
        configuration.setTagsToExcludeFromChart("\\invalid.regex\\");
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
    public void containsReducingMethod_ChecksReducingMethod() {

        // given
        Configuration configuration = new Configuration(outputDirectory, projectName);

        // when
        configuration.addReducingMethod(ReducingMethod.MERGE_FEATURES_BY_ID);

        // then
        assertThat(configuration.containsReducingMethod(ReducingMethod.MERGE_FEATURES_BY_ID)).isTrue();

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
}
