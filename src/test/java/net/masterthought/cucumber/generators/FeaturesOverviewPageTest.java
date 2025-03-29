package net.masterthought.cucumber.generators;

import static org.assertj.core.api.Assertions.assertThat;

import net.masterthought.cucumber.ReportConstants;
import org.apache.velocity.VelocityContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import net.masterthought.cucumber.ReportBuilder;
import net.masterthought.cucumber.generators.integrations.PageTest;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
class FeaturesOverviewPageTest extends PageTest {

    @BeforeEach
    void setUp() {
        setUpWithJson(SAMPLE_JSON);
    }

    @Test
    void getWebPage_ReturnsFeatureFileName() {

        // given
        page = new FeaturesOverviewPage(reportResult, configuration);

        // when
        String fileName = page.getWebPage();

        // then
        assertThat(fileName).isEqualTo(ReportConstants.HOME_PAGE);
    }

    @Test
    void prepareReport_AddsCustomProperties() {

        // given
        page = new FeaturesOverviewPage(reportResult, configuration);

        // when
        page.prepareReport();

        // then
        VelocityContext context = page.context;
        assertThat(context.getKeys()).hasSize(17);

        assertThat(context.get("all_features")).isEqualTo(features);
        assertThat(context.get("report_summary")).isEqualTo(reportResult.getFeatureReport());
    }
}
