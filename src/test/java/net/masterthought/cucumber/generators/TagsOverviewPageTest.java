package net.masterthought.cucumber.generators;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

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
    public void prepareReport_AddsCustomProperties() {

        // given
        page = new TagsOverviewPage(reportResult, configuration);

        // when
        page.prepareReport();

        // then
        VelocityContext context = page.context;
        assertThat(context.getKeys()).hasSize(11);

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
        VelocityContext context = page.context;
        assertThat(context.get("chart_categories")).isEqualTo(new String[]{"@fast"});
        assertThat(context.get("chart_data")).isEqualTo(new String[][]{
                {"100.00"},
                {"0.00"},
                {"0.00"},
                {"0.00"},
                {"0.00"}
        });
    }

    @Test
    public void generateTagLabels_ReturnsTags() {

        // given
        List<TagObject> allTags = this.tags;

        // when
        String[] labels = TagsOverviewPage.generateTagLabels(allTags);

        // then
        assertThat(labels).isEqualTo(new String[]{"@checkout", "@fast", "@featureTag"});
    }

    @Test
    public void generateTagValues_ReturnsTagValues() {

        // given
        List<TagObject> allTags = this.tags;

        // when
        String[][] labels = TagsOverviewPage.generateTagValues(allTags);

        // then
        assertThat(labels).isEqualTo(new String[][]{
                {"62.50", "100.00", "100.00"},
                {"6.25", "0.00", "0.00"},
                {"12.50", "0.00", "0.00"},
                {"6.25", "0.00", "0.00"},
                {"12.50", "0.00", "0.00"}
        });
    }
}
