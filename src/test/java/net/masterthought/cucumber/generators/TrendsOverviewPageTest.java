package net.masterthought.cucumber.generators;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;

import mockit.Deencapsulation;
import org.apache.commons.io.FileUtils;
import org.apache.velocity.VelocityContext;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import net.masterthought.cucumber.ReportBuilder;
import net.masterthought.cucumber.ReportResult;
import net.masterthought.cucumber.Trends;
import net.masterthought.cucumber.generators.integrations.PageTest;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class TrendsOverviewPageTest extends PageTest {

    private final String TRENDS_FILE = pathToTestFile("cucumber-trends.json");
    private final String TRENDS_TMP_FILE = TRENDS_FILE + "-tmp";

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() throws IOException {
        setUpWithJson(SAMPLE_JSON);
        // refresh the file if it was already copied by another/previous test
        FileUtils.copyFile(new File(TRENDS_FILE), new File(TRENDS_TMP_FILE));
    }

    @Test
    public void getWebPage_ReturnsTrendsOverviewFileName() {

        // given
        page = new TrendsOverviewPage(reportResult, configuration, null);

        // when
        String fileName = page.getWebPage();

        // then
        assertThat(fileName).isEqualTo(TrendsOverviewPage.WEB_PAGE);
    }

    @Test
    public void prepareReport_AddsCustomProperties() {

        // given
        configuration.setBuildNumber("myBuild");
        Trends trends = Deencapsulation.invoke(ReportBuilder.class, "loadTrends", new File(TRENDS_TMP_FILE));
        page = new TrendsOverviewPage(reportResult, configuration, trends);

        Deencapsulation.setField(page, "reportResult", new ReportResult(features));

        // when
        page.prepareReport();

        // then
        VelocityContext context = page.context;
        assertThat(context.getKeys()).hasSize(15);

        assertThat(context.get("buildNumbers")).isEqualTo("[\"01_first\",\"other build\",\"05last\"]");
        assertThat(context.get("failedFeatures")).isEqualTo("[1,2,5]");
        assertThat(context.get("totalFeatures")).isEqualTo("[10,20,30]");
        assertThat(context.get("failedScenarios")).isEqualTo("[10,20,20]");
        assertThat(context.get("totalScenarios")).isEqualTo("[10,2,5]");
        assertThat(context.get("failedSteps")).isEqualTo("[100,20,30]");
        assertThat(context.get("totalSteps")).isEqualTo("[150,200,300]");
    }

    @Test
    public void toJavaScriptArray_ReturnsStringArraysAsString() {

        // given
        final Object toConvert = new String[]{"1", "2", "5"};

        // when
        Class<?>[] types = {String[].class};
        String converted = Deencapsulation.invoke(TrendsOverviewPage.class, "toJavaScriptArray", types, toConvert);

        // then
        assertThat(converted).isEqualTo("[\"1\",\"2\",\"5\"]");
    }

    @Test
    public void toJavaScriptArray_ReturnsIntArraysAsString() {

        // given
        final Object toConvert = new int[]{10, 20, 50};

        // when
        Class<?>[] types = {int[].class};
        String converted = Deencapsulation.invoke(TrendsOverviewPage.class, "toJavaScriptArray", types, toConvert);

        // then
        assertThat(converted).isEqualTo("[10,20,50]");
    }
}
