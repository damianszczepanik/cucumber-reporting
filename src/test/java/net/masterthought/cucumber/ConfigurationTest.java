package net.masterthought.cucumber;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class ConfigurationTest {

    private static final File outputDirectory = new File("abc");

    private final String projectName = "123";

    @Test
    public void setStatusFlagsSetsFlags() {

        // give
        Configuration configuration = new Configuration(outputDirectory, projectName);
        boolean failsIfMissingIn = true;
        boolean failsIFPendingIn = false;
        boolean failsIfSkippedIn = true;
        boolean failsIfUndefinedIn = false;
        configuration.setStatusFlags(failsIfSkippedIn, failsIFPendingIn, failsIfUndefinedIn, failsIfMissingIn);

        // when
        boolean failsIfSkippedOut = configuration.failsIfSkipped();
        boolean failsIFPendingOut = configuration.failsIFPending();
        boolean failsIfUndefinedOut = configuration.failsIfUndefined();
        boolean failsIfMissingOut = configuration.failsIfMissing();

        // then
        assertThat(failsIfSkippedOut).isEqualTo(failsIfSkippedIn);
        assertThat(failsIFPendingOut).isEqualTo(failsIFPendingIn);
        assertThat(failsIfUndefinedOut).isEqualTo(failsIfUndefinedIn);
        assertThat(failsIfMissingOut).isEqualTo(failsIfMissingIn);
    }

    @Test
    public void isParallelTesting_ReturnsParallelTesting() {

        // give
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

        // give
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

        // give
        Configuration configuration = new Configuration(outputDirectory, projectName);
        String basePath = StringUtils.EMPTY;
        configuration.setJenkinsBasePath(basePath);

        // when
        String path = configuration.getJenkinsBasePath();

        // then
        assertThat(path).isEqualTo("/");
    }

    @Test
    public void isRunWithJenkins_ReturnsRunWithJenkins() {

        // give
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

        // give
        Configuration configuration = new Configuration(outputDirectory, projectName);

        // when
        File dir = configuration.getReportDirectory();

        // then
        assertThat(dir).isEqualTo(outputDirectory);
    }
    
    @Test
    public void getBuildNumber_ReturnsBuildNumber() {

        // give
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

        // give
        Configuration configuration = new Configuration(outputDirectory, projectName);

        // when
        String name = configuration.getProjectName();

        // then
        assertThat(name).isEqualTo(projectName);
    }
}