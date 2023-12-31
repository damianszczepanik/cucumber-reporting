package net.masterthought.cucumber.generators;

import net.masterthought.cucumber.generators.integrations.PageTest;
import net.masterthought.cucumber.json.support.TagObject;
import org.apache.velocity.VelocityContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
class TagReportPageTest extends PageTest {

    @BeforeEach
    void setUp() {
        setUpWithJson(SAMPLE_JSON);
    }

    @Test
    void getWebPage_ReturnsTagReportFileName() {

        // given
        TagObject tag = tags.get(0);
        page = new TagReportPage(reportResult, configuration, tag);

        // when
        String fileName = page.getWebPage();

        // then
        assertThat(fileName).isEqualTo(tag.getReportFileName());
    }

    @Test
    void prepareReport_AddsCustomProperties() {

        // given
        TagObject tag = tags.get(1);
        page = new TagReportPage(reportResult, configuration, tag);

        // when
        page.prepareReport();

        // then
        VelocityContext context = page.context;
        assertThat(context.getKeys()).hasSize(14);
        assertThat(context.get("tag")).isEqualTo(tag);
    }
}
