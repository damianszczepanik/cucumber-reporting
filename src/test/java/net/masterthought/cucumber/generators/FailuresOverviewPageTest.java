package net.masterthought.cucumber.generators;

import static org.assertj.core.api.Assertions.assertThat;

import mockit.Deencapsulation;
import org.apache.velocity.VelocityContext;
import org.junit.Before;
import org.junit.Test;

import net.masterthought.cucumber.generators.integrations.PageTest;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class FailuresOverviewPageTest extends PageTest {

    @Before
    public void setUp() {
        setUpWithJson(SAMPLE_JSON);
    }

    @Test
    public void getWebPage_ReturnsFeatureFileName() {

        // given
        page = new FailuresOverviewPage(reportResult, configuration);

        // when
        String fileName = page.getWebPage();

        // then
        assertThat(fileName).isEqualTo("failures-overview.html");
    }

    @Test
    public void prepareReportAddsCustomProperties() {

        // given
        page = new FailuresOverviewPage(reportResult, configuration);

        // when
        page.prepareReport();

        // then
        VelocityContext context = Deencapsulation.getField(page, "context");
        assertThat(context.getKeys()).hasSize(6);

        assertThat(context.get("all_features")).isEqualTo(features);
    }
}
