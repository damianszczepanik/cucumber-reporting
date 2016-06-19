package net.masterthought.cucumber.generators;

import mockit.Deencapsulation;
import net.masterthought.cucumber.generators.integrations.PageTest;
import net.masterthought.cucumber.util.Counter;
import org.apache.velocity.VelocityContext;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

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

        // given
        page = new FeaturesOverviewPage(reportResult, configuration);

        // when
        page.generatePage();

        // then
        File reportFile = new File(configuration.getReportDirectory(), page.getWebPage());
        assertThat(reportFile).exists();
    }

    @Test
    public void buildProperties_ReturnsProperties() {

        // given
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

        // given
        page = new TagsOverviewPage(reportResult, configuration);

        // when
        // buildGeneralParameters() already called by constructor

        // then
        VelocityContext context = Deencapsulation.getField(page, "context");
        assertThat(context.getKeys()).hasSize(6);
        assertThat(context.get("jenkins_source")).isEqualTo(configuration.isRunWithJenkins());
        assertThat(context.get("jenkins_base")).isEqualTo(configuration.getJenkinsBasePath());
        assertThat(context.get("build_project_name")).isEqualTo(configuration.getProjectName());
        assertThat(context.get("build_number")).isEqualTo(configuration.getBuildNumber());
        assertThat(context.get("jenkins_source")).isNotNull();

        Object obj = context.get("counter");
        assertThat(obj).isInstanceOf(Counter.class);
        Counter counter = (Counter) obj;
        assertThat(counter.next()).isEqualTo(1);
    }

    @Test
    public void buildGeneralParametersWithBuildNumberAddsBuildPreviousNumberProperty() {

        // given
        configuration.setBuildNumber("12");
        page = new ErrorPage(null, configuration, null, jsonReports);

        // when
        // buildGeneralParameters() already called by constructor

        // then
        VelocityContext context = Deencapsulation.getField(page, "context");
        assertThat(context.getKeys()).hasSize(7);
        assertThat(context.get("build_time")).isNotNull();
    }

    @Test
    public void buildGeneralParameters_OnErrorPageAddsExtraProperties() {

        // given
        configuration.setBuildNumber("3@");
        page = new ErrorPage(null, configuration, null, jsonReports);

        // when
        // buildGeneralParameters() already called by constructor

        // then
        VelocityContext context = Deencapsulation.getField(page, "context");
        assertThat(context.getKeys()).hasSize(6);
        assertThat(context.get("build_previous_number")).isNull();
    }

    @Test
    public void buildGeneralParameters_OnInvalidBuildNumberDoesNotAddPreviousBuildNumberProperty() {

        // given
        configuration.setBuildNumber("34");
        page = new TagsOverviewPage(reportResult, configuration);

        // when
        // buildGeneralParameters() already called by constructor

        // then
        VelocityContext context = Deencapsulation.getField(page, "context");
        assertThat(context.getKeys()).hasSize(7);
        assertThat(context.get("build_previous_number")).isEqualTo(33);
    }
}
