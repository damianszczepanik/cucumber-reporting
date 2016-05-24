package net.masterthought.cucumber.generators;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.velocity.VelocityContext;
import org.junit.Before;
import org.junit.Test;

import mockit.Deencapsulation;
import net.masterthought.cucumber.generators.integrations.PageTest;
import net.masterthought.cucumber.util.ChartUtil;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class TagsOverviewPageTest extends PageTest {

    @Before
    public void setUp() {
        setUpWithJson(SAMPLE_JOSN);
    }

    @Test
    public void getWebPageReturnsFeatureFileName() {

        // give
        page = new TagsOverviewPage(reportResult, configuration);

        // when
        String fileName = page.getWebPage();

        // then
        assertThat(fileName).isEqualTo("tag-overview.html");
    }

    @Test
    public void prepareReportAddsCustomProperties() {

        // give
        page = new TagsOverviewPage(reportResult, configuration);

        // when
        page.prepareReport();

        // then
        VelocityContext context = Deencapsulation.getField(page, "context");
        assertThat(context.getKeys()).hasSize(9);

        assertThat(context.get("all_tags")).isEqualTo(tags);
        assertThat(context.get("report_summary")).isEqualTo(reportResult.getTagReport());
        assertThat(context.get("chart_categories")).isEqualTo(ChartUtil.getTags(tags));
        assertThat(context.get("chart_data")).isEqualTo(ChartUtil.generateTagChartDataForHighCharts(tags));
    }
}
