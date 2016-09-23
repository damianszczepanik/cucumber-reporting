package net.masterthought.cucumber.generators;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.util.Properties;

import mockit.Deencapsulation;
import org.apache.velocity.VelocityContext;
import org.junit.Before;
import org.junit.Test;

import net.masterthought.cucumber.ReportBuilder;
import net.masterthought.cucumber.generators.integrations.PageTest;
import net.masterthought.cucumber.util.Counter;
import net.masterthought.cucumber.util.Util;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class AbstractPageTest extends PageTest {

    @Before
    public void setUp() {
        setUpWithJson(SAMPLE_JSON);
    }

    @Test
    public void generateReport_CreatesReportFile() {

        // given
        page = new FeaturesOverviewPage(reportResult, configuration);

        // when
        page.generatePage();

        // then
        File reportFile = new File(configuration.getReportDirectory(),
                ReportBuilder.BASE_DIRECTORY + File.separatorChar + page.getWebPage());
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
    public void buildGeneralParameters_AddsCommonProperties() {

        // given
        page = new TagsOverviewPage(reportResult, configuration);

        // when
        // buildGeneralParameters() already called by constructor

        // then
        VelocityContext context = page.context;
        assertThat(context.getKeys()).hasSize(8);

        Object obj = context.get("counter");
        assertThat(obj).isInstanceOf(Counter.class);
        Counter counter = (Counter) obj;
        assertThat(counter.next()).isEqualTo(1);

        assertThat(context.get("util")).isInstanceOf(Util.class);

        assertThat(context.get("jenkins_source")).isEqualTo(configuration.isRunWithJenkins());
        assertThat(context.get("jenkins_base")).isEqualTo(configuration.getJenkinsBasePath());
        assertThat(context.get("build_project_name")).isEqualTo(configuration.getProjectName());
        assertThat(context.get("build_number")).isEqualTo(configuration.getBuildNumber());
        assertThat(context.get("jenkins_source")).isNotNull();
    }

    @Test
    public void buildGeneralParameters_OnBuildNumber_AddsBuildPreviousNumberProperty() {

        // given
        configuration.setBuildNumber("12");
        page = new ErrorPage(null, configuration, null, jsonReports);

        // when
        // buildGeneralParameters() already called by constructor

        // then
        VelocityContext context = page.context;
        assertThat(context.getKeys()).hasSize(9);
        assertThat(context.get("build_time")).isNotNull();
    }

    @Test
    public void buildGeneralParameters_OnErrorPage_AddsExtraProperties() {

        // given
        configuration.setBuildNumber("3@");
        page = new ErrorPage(null, configuration, null, jsonReports);

        // when
        // buildGeneralParameters() already called by constructor

        // then
        VelocityContext context = page.context;
        assertThat(context.getKeys()).hasSize(8);
        assertThat(context.get("build_previous_number")).isNull();
    }

    @Test
    public void buildGeneralParameters_OnInvalidBuildNumber_DoesNotAddPreviousBuildNumberProperty() {

        // given
        configuration.setBuildNumber("34");
        page = new TagsOverviewPage(reportResult, configuration);

        // when
        // buildGeneralParameters() already called by constructor

        // then
        VelocityContext context = page.context;
        assertThat(context.getKeys()).hasSize(9);
        assertThat(context.get("build_previous_number")).isEqualTo(33);
    }

    @Test
    public void buildGeneralParameters_OnTrendsStatsFile_AddsTrendsFlag() {

        // given
        configuration.setTrendsStatsFile(new File("."));
        page = new TrendsOverviewPage(reportResult, configuration);

        // when
        boolean hasTrends = (Boolean) page.context.get("trends_present");

        // then
        assertThat(hasTrends).isTrue();
    }
}
