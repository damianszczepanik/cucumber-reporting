package net.masterthought.cucumber.generators;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.VelocityContext;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import mockit.Deencapsulation;
import net.masterthought.cucumber.ReportBuilder;
import net.masterthought.cucumber.Trends;
import net.masterthought.cucumber.ValidationException;
import net.masterthought.cucumber.generators.integrations.PageTest;
import net.masterthought.cucumber.generators.integrations.helpers.DocumentAssertion;
import net.masterthought.cucumber.util.Counter;
import net.masterthought.cucumber.util.Util;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class AbstractPageTest extends PageTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

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
    public void generateReport_DisplaysContentAsEscapedText() {

        // given
        page = new FeatureReportPage(reportResult, configuration, features.get(1));

        // when
        page.generatePage();

        // then
        DocumentAssertion document = documentFrom(page.getWebPage());
        assertThat(document.getFeature().getDescription())
                .isEqualTo("As an Account Holder I want to withdraw cash from an ATM,<br>so that I can get money when the bank is closed");
        assertThat(document.getFeature().getElements()[0].getStepsSection().getSteps()[5].getEmbedding()[3].text())
                .isEqualTo("Attachment 4 (HTML)");
        assertThat(document.getFeature().getElements()[0].getStepsSection().getSteps()[5].getMessage().text())
                .isEqualTo("java.lang.AssertionError: java.lang.AssertionError: \n" +
                        "Expected: is <80>\n" +
                        "     got: <90>\n" +
                        "\n" +
                        "\tat org.junit.Assert.assertThat(Assert.java:780)\n" +
                        "\tat org.junit.Assert.assertThat(Assert.java:738)\n" +
                        "\tat net.masterthought.example.ATMScenario.checkBalance(ATMScenario.java:69)\n" +
                        "\tat ✽.And the account balance should be 90(net/masterthought/example/ATMK.feature:12)");
    }

    @Test
    public void generateReport_OnInvalidPath_ThrowsException() {

        // given
        page = new FeaturesOverviewPage(reportResult, configuration) {
            @Override
            public String getWebPage() {
                // invalid file path
                return StringUtils.EMPTY;
            }
        };

        // when
        thrown.expect(ValidationException.class);
        Deencapsulation.invoke(page, "generatePage");
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
        assertThat(context.getKeys()).hasSize(7);

        Object obj = context.get("counter");
        assertThat(obj).isInstanceOf(Counter.class);
        Counter counter = (Counter) obj;
        assertThat(counter.next()).isEqualTo(1);

        assertThat(context.get("util")).isInstanceOf(Util.class);

        assertThat(context.get("run_with_jenkins")).isEqualTo(configuration.isRunWithJenkins());
        assertThat(context.get("build_project_name")).isEqualTo(configuration.getProjectName());
        assertThat(context.get("build_number")).isEqualTo(configuration.getBuildNumber());
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
        assertThat(context.getKeys()).hasSize(8);
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
        assertThat(context.getKeys()).hasSize(7);
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
        assertThat(context.getKeys()).hasSize(8);
        assertThat(context.get("build_previous_number")).isEqualTo(33);
    }

    @Test
    public void buildGeneralParameters_OnTrendsStatsFile_AddsTrendsFlag() {

        // given
        configuration.setTrendsStatsFile(TRENDS_FILE);
        Trends trends = Deencapsulation.invoke(ReportBuilder.class, "loadTrends", TRENDS_FILE);
        page = new TrendsOverviewPage(reportResult, configuration, trends);

        // when
        boolean hasTrends = (Boolean) page.context.get("trends_present");

        // then
        assertThat(hasTrends).isTrue();
    }
}
