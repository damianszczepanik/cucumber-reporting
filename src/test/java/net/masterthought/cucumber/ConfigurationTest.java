package net.masterthought.cucumber;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.util.Collection;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class ConfigurationTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    private static final File outputDirectory = new File("abc");

    private final String projectName = "123";


    @Test
    public void isParallelTesting_ReturnsParallelTesting() {

        // given
        Configuration configuration = new Configuration(outputDirectory, projectName);
        boolean parallelTesting = true;
        configuration.setParallelTesting(parallelTesting);

        // when
        boolean parallel = configuration.isParallelTesting();

        // then
        assertThat(parallel).isEqualTo(parallelTesting);
    }

    @Test
    public void getJenkinsBasePath_OnSampleBath_ReturnsJenkinsPath() {

        // given
        Configuration configuration = new Configuration(outputDirectory, projectName);
        String basePath = "abc321";
        configuration.setJenkinsBasePath(basePath);

        // when
        String path = configuration.getJenkinsBasePath();

        // then
        assertThat(path).isEqualTo(basePath);
    }

    @Test
    public void getJenkinsBasePath_OnEmptyPath_ReturnsJenkinsPath() {

        // given
        Configuration configuration = new Configuration(outputDirectory, projectName);
        String basePath = StringUtils.EMPTY;
        configuration.setJenkinsBasePath(basePath);

        // when
        String path = configuration.getJenkinsBasePath();

        // then
        assertThat(path).isEmpty();
    }

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
}
