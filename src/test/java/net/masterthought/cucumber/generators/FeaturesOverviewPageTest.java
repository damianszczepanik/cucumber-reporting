package net.masterthought.cucumber.generators;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.velocity.VelocityContext;
import org.junit.Before;
import org.junit.Test;

import net.masterthought.cucumber.ReportBuilder;
import net.masterthought.cucumber.generators.integrations.PageTest;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class FeaturesOverviewPageTest extends PageTest {

    @Before
    public void setUp() {
        setUpWithJson(SAMPLE_JSON);
    }

    @Test
    public void getWebPage_ReturnsFeatureFileName() {

        // given
        page = new FeaturesOverviewPage();

        // when
        String fileName = page.getWebPage();

        // then
        assertThat(fileName).isEqualTo(ReportBuilder.HOME_PAGE);
    }

    @Test
    public void prepareReport_AddsCustomProperties() {

        // given
    	VelocityContext context = new VelocityContext();
        page = new FeaturesOverviewPage();

        // when
        page.preparePageContext(context, configuration, reportResult);

        // then
        assertThat(context.getKeys()).hasSize(4);

        assertThat(context.get("all_features")).isEqualTo(features);
        assertThat(context.get("report_summary")).isEqualTo(reportResult.getFeatureReport());
    }
}
