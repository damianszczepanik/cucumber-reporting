package net.masterthought.cucumber.generators;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.util.Properties;

import org.apache.velocity.VelocityContext;
import org.junit.Before;
import org.junit.Test;

import mockit.Deencapsulation;
import net.masterthought.cucumber.generators.integrations.PageTest;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class AbstractPageTest extends PageTest {

    @Before
    public void setUp() {
        setUpWithJson(SAMPLE_JSON);
    }

    @Test
    public void generateReportCreatesReportFile() {

        // give
        page = new FeaturesOverviewPage(reportResult, configuration);

        // when
        page.generatePage();

        // then
        File reportFile = new File(configuration.getReportDirectory(), page.getWebPage());
        assertThat(reportFile).exists();
    }

    @Test
    public void buildPropertiesReturnsProperties() {

        // give
        page = new FeaturesOverviewPage(reportResult, configuration);

        // when
        Properties props = Deencapsulation.invoke(page, "buildProperties");

        // then
        assertThat(props).hasSize(3);
        assertThat(props.getProperty("resource.loader")).isNotNull();
        assertThat(props.getProperty("class.resource.loader.class")).isNotNull();
        assertThat(props.getProperty("runtime.log")).isNotNull();
    }

    @Test
    public void buildGeneralParametersAddsCommonProperties() {

        // give
        page = new TagsOverviewPage(reportResult, configuration);

        // when
        // buildGeneralParameters() already called by constructor

        // then
        VelocityContext context = Deencapsulation.getField(page, "context");
        assertThat(context.getKeys()).hasSize(5);
        assertThat(context.get("jenkins_source")).isEqualTo(configuration.isRunWithJenkins());
        assertThat(context.get("jenkins_base")).isEqualTo(configuration.getJenkinsBasePath());
        assertThat(context.get("build_project_name")).isEqualTo(configuration.getProjectName());
        assertThat(context.get("build_number")).isEqualTo(configuration.getBuildNumber());
        assertThat(context.get("jenkins_source")).isNotNull();
    }

    @Test
    public void buildGeneralParametersWithBuildNumberAddsBuildPreviousNumberProperty() {

        // give
        configuration.setBuildNumber("12");
        page = new ErrorPage(null, configuration, null, jsonReports);

        // when
        // buildGeneralParameters() already called by constructor

        // then
        VelocityContext context = Deencapsulation.getField(page, "context");
        assertThat(context.getKeys()).hasSize(6);
        assertThat(context.get("build_time")).isNotNull();
    }

    @Test
    public void buildGeneralParametersOnErrorPageAddsExtraProperties() {

        // give
        configuration.setBuildNumber("3@");
        page = new ErrorPage(null, configuration, null, jsonReports);

        // when
        // buildGeneralParameters() already called by constructor

        // then
        VelocityContext context = Deencapsulation.getField(page, "context");
        assertThat(context.getKeys()).hasSize(5);
        assertThat(context.get("build_previous_number")).isNull();
    }

    @Test
    public void buildGeneralParametersOnInvalidBuildNumberDoesNotAddPreviousBuildNumberProperty() {

        // give
        configuration.setBuildNumber("34");
        page = new TagsOverviewPage(reportResult, configuration);

        // when
        // buildGeneralParameters() already called by constructor

        // then
        VelocityContext context = Deencapsulation.getField(page, "context");
        assertThat(context.getKeys()).hasSize(6);
        assertThat(context.get("build_previous_number")).isEqualTo(33);
    }
}
