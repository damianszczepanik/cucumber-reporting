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
    public void preparePageContext_AddsCustomProperties() {

        // given
        VelocityContext pageContext = new VelocityContext();
        TagObject tag = tags.get(1);
        page = new TagReportPage(tag);

        // when
        page.preparePageContext(pageContext, configuration, reportResult);

        // then
        assertThat(pageContext.getKeys()).hasSize(1);
        assertThat(pageContext.get("tag")).isEqualTo(tag);
    }
}
