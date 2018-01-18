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
        page = new TagsOverviewPage();

        // when
        String fileName = page.getWebPage();

        // then
        assertThat(fileName).isEqualTo(TagsOverviewPage.WEB_PAGE);
    }

    @Test
    public void preparePageContext_AddsCustomProperties() {

        // given
    	VelocityContext pageContext = new VelocityContext();
        page = new TagsOverviewPage();

        // when
        page.preparePageContext(pageContext, configuration, reportResult);

        // then
        assertThat(pageContext.getKeys()).hasSize(4);

        assertThat(pageContext.get("all_tags")).isEqualTo(tags);
        assertThat(pageContext.get("report_summary")).isEqualTo(reportResult.getTagReport());
        assertThat(pageContext.get("chart_categories")).isEqualTo(TagsOverviewPage.generateTagLabels(tags));
        assertThat(pageContext.get("chart_data")).isEqualTo(TagsOverviewPage.generateTagValues(tags));
    }

    @Test
    public void preparePageContext_setTagsToExcludeFromChart_ReturnsFilteredTags() {

        // give
        page = new TagsOverviewPage();
        configuration.setTagsToExcludeFromChart("@checkout", "@feature.*");

        //when
        VelocityContext pageContext = new VelocityContext();
        page.preparePageContext(pageContext, configuration, reportResult);

        // then
        assertThat(pageContext.get("chart_categories")).isEqualTo(new String[]{"@fast"});
        assertThat(pageContext.get("chart_data")).isEqualTo(new String[][]{
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
