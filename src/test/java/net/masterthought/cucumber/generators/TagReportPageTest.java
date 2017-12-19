package net.masterthought.cucumber.generators;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.velocity.VelocityContext;
import org.junit.Before;
import org.junit.Test;

import net.masterthought.cucumber.generators.integrations.PageTest;
import net.masterthought.cucumber.json.support.TagObject;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class TagReportPageTest extends PageTest {

    @Before
    public void setUp() {
        setUpWithJson(SAMPLE_JSON);
    }

    @Test
    public void getWebPage_ReturnsTagReportFileName() {

        // given
        TagObject tag = tags.get(0);
        page = new TagReportPage(tag);

        // when
        String fileName = page.getWebPage();

        // then
        assertThat(fileName).isEqualTo(tag.getReportFileName());
    }

    @Test
    public void prepareReport_AddsCustomProperties() {

        // given
    	VelocityContext context = new VelocityContext();
        TagObject tag = tags.get(1);
        page = new TagReportPage(tag);

        // when
        page.preparePageContext(context, configuration, reportResult);

        // then
        assertThat(context.getKeys()).hasSize(1);
        assertThat(context.get("tag")).isEqualTo(tag);
    }
}
