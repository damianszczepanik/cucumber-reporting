package net.masterthought.cucumber.generators;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import mockit.Deencapsulation;
import org.apache.velocity.VelocityContext;
import org.junit.Before;
import org.junit.Test;

import net.masterthought.cucumber.generators.integrations.PageTest;
import net.masterthought.cucumber.json.support.TagObject;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class TagsOverviewPageTest extends PageTest {

    @Before
    public void setUp() {
        setUpWithJson(SAMPLE_JSON);
    }

    @Test
    public void getWebPage_ReturnsTagOverviewReportFileName() {

        // given
        page = new TagsOverviewPage(reportResult, configuration);

        // when
        String fileName = page.getWebPage();

        // then
        assertThat(fileName).isEqualTo(TagsOverviewPage.WEB_PAGE);
    }

    @Test
    public void prepareReportAddsCustomProperties() {

        // given
        page = new TagsOverviewPage(reportResult, configuration);

        // when
        page.prepareReport();

        // then
        VelocityContext context = Deencapsulation.getField(page, "context");
        assertThat(context.getKeys()).hasSize(10);

        assertThat(context.get("all_tags")).isEqualTo(tags);
        assertThat(context.get("report_summary")).isEqualTo(reportResult.getTagReport());
        assertThat(context.get("chart_categories")).isEqualTo(TagsOverviewPage.generateTagLabels(tags));
        assertThat(context.get("chart_data")).isEqualTo(TagsOverviewPage.generateTagValues(tags));
    }

    @Test
    public void prepareReport_setTagsToExcludeFromChart_ReturnsFilteredTags() {

        // give
        page = new TagsOverviewPage(reportResult, configuration);
        configuration.setTagsToExcludeFromChart("@checkout", "@feature.*");

        //when
        page.prepareReport();

        // then
        VelocityContext context = Deencapsulation.getField(page, "context");
        assertThat(context.get("chart_categories")).isEqualTo("[\"@fast\"]");
        assertThat(context.get("chart_data")).isEqualTo(asList(
                "[57.14]",
                "[0.00]",
                "[0.00]",
                "[28.57]",
                "[14.29]"));
    }

    @Test
    public void generateTagLabels_ReturnsTags() {

        // given
        List<TagObject> allTags = this.tags;

        // when
        String labels = TagsOverviewPage.generateTagLabels(allTags);

        // then
        assertThat(labels).isEqualTo("[\"@checkout\",\"@fast\",\"@featureTag\"]");
    }

    @Test
    public void generateTagValues_ReturnsTagValues() {

        // given
        List<TagObject> allTags = this.tags;

        // when
        List<String> labels = TagsOverviewPage.generateTagValues(allTags);

        // then
        assertThat(labels).containsExactly(
                "[50.00,57.14,57.14]",
                "[0.00,0.00,0.00]",
                "[25.00,0.00,0.00]",
                "[12.50,28.57,28.57]",
                "[12.50,14.29,14.29]");
    }

    @Test
    public void format_ReturnsFormatedValue() {

        // given
        final int[][] values = { { 1, 3 }, { 2, 2 }, { 1, 5 }, { 0, 5 } };
        String[] formatted = { "33.33", "100.00", "20.00", "0.00" };

        // then
        for (int i = 0; i < values.length; i++) {
            assertThat(TagsOverviewPage.format(values[i][0], values[i][1])).isEqualTo(formatted[i]);
        }
    }
}
