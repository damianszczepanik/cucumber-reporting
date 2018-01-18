package net.masterthought.cucumber.generators;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.VelocityContext;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import mockit.Deencapsulation;
import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportBuilder;
import net.masterthought.cucumber.ReportResult;
import net.masterthought.cucumber.ValidationException;
import net.masterthought.cucumber.generators.integrations.PageTest;
import net.masterthought.cucumber.generators.integrations.helpers.DocumentAssertion;
import net.masterthought.cucumber.util.Counter;
import net.masterthought.cucumber.util.Util;

public class PageGeneratorTest extends PageTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() {
        setUpWithJson(SAMPLE_JSON);
    }

    @Test
    public void generatePage_CreatesFile() {

        // given
        page = new FeaturesOverviewPage();

        // when
        generatePage(page);

        // then
        File reportFile = new File(configuration.getReportDirectory(), ReportBuilder.BASE_DIRECTORY + File.separatorChar + page.getWebPage());
        assertThat(reportFile).exists();
    }

    @Test
    public void generatePage_DisplaysContentAsEscapedText() {

        // given
        page = new FeatureReportPage(features.get(1));

        // when
        generatePage(page);

        // then
        DocumentAssertion document = documentFrom(page.getWebPage());
        assertThat(document.getFeature().getDescription()).isEqualTo("As an Account Holder I want to withdraw cash from an ATM,<br>so that I can get money when the bank is closed");
        assertThat(document.getFeature().getElements()[0].getStepsSection().getSteps()[5].getEmbedding()[3].text()).isEqualTo("Attachment 4 (HTML)");
        assertThat(document.getFeature().getElements()[0].getStepsSection().getSteps()[5].getMessage().text()).isEqualTo("java.lang.AssertionError: java.lang.AssertionError: \n" + "Expected: is <80>\n" + "     got: <90>\n" + "\n" + "\tat org.junit.Assert.assertThat(Assert.java:780)\n" + "\tat org.junit.Assert.assertThat(Assert.java:738)\n" + "\tat net.masterthought.example.ATMScenario.checkBalance(ATMScenario.java:69)\n" + "\tat ✽.And the account balance should be 90(net/masterthought/example/ATMK.feature:12)");
    }

    @Test
    public void generateReport_OnInvalidPath_ThrowsException() throws IOException {

        // given
        File outputDir = new File("target", String.valueOf(System.currentTimeMillis()));
        Files.createDirectories(outputDir.toPath());
        configuration = new Configuration(outputDir, null);
        File reportFile = new File(configuration.getReportDirectory(), ReportBuilder.BASE_DIRECTORY);
        FileUtils.touch(reportFile);

        // when
        thrown.expect(ValidationException.class);
        generatePage(new AbstractPage("test") {

            @Override
            public void preparePageContext(VelocityContext context, Configuration configuration, ReportResult reportResult) {
                // TODO Auto-generated method stub

            }

            @Override
            public String getWebPage() {
                // TODO Auto-generated method stub
                return null;
            }
        });
    }

    @Test
    public void generatePage_OnInvalidPath_ThrowsException() {

        // given
        page = new FeaturesOverviewPage() {
            @Override
            public String getWebPage() {
                // invalid file path
                return StringUtils.EMPTY;
            }
        };

        // when
        thrown.expect(ValidationException.class);
        generatePage(page);
    }

    @Test
    public void buildProperties_ReturnsProperties() {

        // given
        PageGenerator pageGenerator = new PageGenerator(configuration, reportResult);

        // when
        Properties props = Deencapsulation.invoke(pageGenerator, "buildProperties");

        // then
        assertThat(props).hasSize(3);
        assertThat(props.getProperty("resource.loader")).isNotNull();
        assertThat(props.getProperty("class.resource.loader.class")).isNotNull();
        assertThat(props.getProperty("runtime.log")).isNotNull();
    }

    @Test
    public void newGlobalContext_AddsCommonPropertiesForAllPages() {

        // given
        PageGenerator pageGenerator = new PageGenerator(configuration, reportResult);

        // when
        // newGlobalContext() already called by constructor

        // then
        VelocityContext context = pageGenerator.globalContext;
        assertThat(context.getKeys()).hasSize(6);

        assertThat(context.get("util")).isInstanceOf(Util.class);

        assertThat(context.get("run_with_jenkins")).isEqualTo(configuration.isRunWithJenkins());
        assertThat(context.get("build_project_name")).isEqualTo(configuration.getProjectName());
        assertThat(context.get("build_number")).isEqualTo(configuration.getBuildNumber());
    }

    @Test
    public void newGlobalContext_OnInvalidBuildNumber_SkipsBuildPreviousNumberProperty() {

        // given
        configuration.setBuildNumber("notAnumber");
        configuration.setRunWithJenkins(true);
        PageGenerator pageGenerator = new PageGenerator(configuration, reportResult);

        // when
        // newGlobalContext() already called by constructor

        // then
        VelocityContext context = pageGenerator.globalContext;
        assertThat(context.getKeys()).hasSize(6);
        assertThat(context.get("build_number")).isNotNull();
        assertThat(context.get("build_previous_number")).isNull();
    }

    @Test
    public void newGlobalContext_OnBuildNumber_AddsBuildPreviousNumberProperty() {

        // given
        configuration.setBuildNumber("12");
        configuration.setRunWithJenkins(true);
        PageGenerator pageGenerator = new PageGenerator(configuration, reportResult);

        // when
        // newGlobalContext() already called by constructor

        // then
        VelocityContext context = pageGenerator.globalContext;
        assertThat(context.getKeys()).hasSize(7);
        assertThat(context.get("build_number")).isNotNull();
        assertThat(context.get("build_previous_number")).isNotNull();
        assertThat(context.get("build_previous_number")).isEqualTo(11);
    }

    @Test
    public void newGlobalContext_OnBuildNumber_NotInJenkins_DoesntAddsBuildPreviousNumberProperty() {

        // given
        configuration.setBuildNumber("12");
        configuration.setRunWithJenkins(false);
        PageGenerator pageGenerator = new PageGenerator(configuration, reportResult);

        // when
        // newGlobalContext() already called by constructor

        // then
        VelocityContext context = pageGenerator.globalContext;
        assertThat(context.getKeys()).hasSize(6);
        assertThat(context.get("build_number")).isNotNull();
        assertThat(context.get("build_previous_number")).isNull();
    }

    @Test
    public void newGlobalContext_OnTrendsStatsFile_AddsTrendsFlag() {

        // given
        configuration.setTrendsStatsFile(TRENDS_FILE);
        PageGenerator pageGenerator = new PageGenerator(configuration, reportResult);

        // when
        // newGlobalContext() already called by constructor

        // then
        VelocityContext context = pageGenerator.globalContext;
        Boolean hasTrends = (Boolean) context.get("trends_present");

        assertThat(hasTrends).isNotNull();
        assertThat(hasTrends).isTrue();
    }

    @Test
    public void newPageContext_AddsCommonPropertiesForOnePageAndKeepsGlobalOnes() {

        // given
        PageGenerator pageGenerator = new PageGenerator(configuration, reportResult);

        // when
        VelocityContext context = Deencapsulation.invoke(pageGenerator, "newPageContext");

        // then
        assertThat(context.getKeys()).hasSize(1);
        assertThat(context.getChainedContext().getKeys()).hasSize(6);

        Object obj = context.get("counter");
        assertThat(obj).isInstanceOf(Counter.class);
        Counter counter = (Counter) obj;
        assertThat(counter.next()).isEqualTo(1);
    }
}
