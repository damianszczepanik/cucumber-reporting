package net.masterthought.cucumber.generators;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.velocity.VelocityContext;
import org.junit.Before;
import org.junit.Test;

import net.masterthought.cucumber.generators.integrations.PageTest;
import net.masterthought.cucumber.json.Feature;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class FeatureReportPageTest extends PageTest {

    @Before
    public void setUp() {
        setUpWithJson(SAMPLE_JSON);
    }

    @Test
    public void getWebPage_ReturnsFeatureFileName() {

        // given
        Feature feature = features.get(1);
        page = new FeatureReportPage(reportResult, configuration, feature);

        // when
        String fileName = page.getWebPage();

        // then
        assertThat(fileName).isEqualTo(feature.getReportFileName());
    }

    @Test
    public void prepareReport_AddsCustomProperties() {

        // given
        Feature feature = features.get(1);
        page = new FeatureReportPage(reportResult, configuration, feature);

        // when
        page.prepareReport();

        // then
        VelocityContext context = page.context;
        assertThat(context.getKeys()).hasSize(9);
        assertThat(context.get("parallel")).isEqualTo(configuration.isParallelTesting());
        assertThat(context.get("feature")).isEqualTo(feature);
    }
}
