package net.masterthought.cucumber.generators.integrations;

import static org.assertj.core.api.Assertions.assertThat;

import net.masterthought.cucumber.ReportBuilder;
import net.masterthought.cucumber.Trends;
import net.masterthought.cucumber.generators.TrendsOverviewPage;
import net.masterthought.cucumber.generators.integrations.helpers.DocumentAssertion;
import net.masterthought.cucumber.generators.integrations.helpers.LeadAssertion;
import net.masterthought.cucumber.generators.integrations.helpers.WebAssertion;
import org.junit.jupiter.api.Test;
import org.powermock.reflect.Whitebox;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
class TrendsOverviewPageIntegrationTest extends PageTest {

    @Test
    void generatePage_generatesTitle() throws Exception {

        // given
        setUpWithJson(SAMPLE_JSON);
        Trends trends = Whitebox.invokeMethod(ReportBuilder.class, "loadTrends", TRENDS_FILE);
        page = new TrendsOverviewPage(reportResult, configuration, trends);
        final String titleValue = String.format("Cucumber Reports (no %s) - Trends Overview",
                configuration.getBuildNumber());

        // when
        page.generatePage();

        // then
        DocumentAssertion document = documentFrom(page.getWebPage());
        String title = document.getHead().getTitle();

        assertThat(title).isEqualTo(titleValue);
    }

    @Test
    void generatePage_generatesLead() throws Exception {

        // given
        setUpWithJson(SAMPLE_JSON);
        Trends trends = Whitebox.invokeMethod(ReportBuilder.class, "loadTrends", TRENDS_FILE);
        page = new TrendsOverviewPage(reportResult, configuration, trends);

        // when
        page.generatePage();

        // then
        DocumentAssertion document = documentFrom(page.getWebPage());
        LeadAssertion lead = document.getLead();

        assertThat(lead.getHeader()).isEqualTo("Trends Statistics");
        assertThat(lead.getDescription()).isEqualTo("The following graph shows features, scenarios and steps for a period of time.");
    }

    @Test
    void generatePage_generatesCharts() throws Exception {

        // given
        setUpWithJson(SAMPLE_JSON);
        Trends trends = Whitebox.invokeMethod(ReportBuilder.class, "loadTrends", TRENDS_FILE);
        page = new TrendsOverviewPage(reportResult, configuration, trends);

        // when
        page.generatePage();

        // then
        DocumentAssertion document = documentFrom(page.getWebPage());

        // naive checking
        assertThat(document.byId("trends-features-chart", WebAssertion.class)).isNotNull();
        assertThat(document.byId("trends-scenarios-chart", WebAssertion.class)).isNotNull();
        assertThat(document.byId("trends-steps-chart", WebAssertion.class)).isNotNull();
    }

    @Test
    void generatePage_insertsChartData() throws Exception {

        // given
        setUpWithJson(SAMPLE_JSON);
        Trends trends = Whitebox.invokeMethod(ReportBuilder.class, "loadTrends", TRENDS_FILE);
        page = new TrendsOverviewPage(reportResult, configuration, trends);

        // when
        page.generatePage();

        // then
        DocumentAssertion document = documentFrom(page.getWebPage());

        // check that data used by the charts is correctly inserted into the script section
        assertThat(document.html()).contains("var buildNumbers =  [ \"01_first\",  \"other build\",  \"05last\", ] ;");
        assertThat(document.html()).contains("var failedFeatures =  [ 1,  2,  5, ] ;");
        assertThat(document.html()).contains("var passedFeatures =  [ 9,  18,  25, ] ;");
        assertThat(document.html()).contains("var failedScenarios =  [ 10,  20,  20, ] ;");
        assertThat(document.html()).contains("var passedScenarios =  [ 10,  20,  20, ] ;");
        assertThat(document.html()).contains("var passedSteps =  [ 1,  3,  5, ] ;");
        assertThat(document.html()).contains("var failedSteps =  [ 10,  30,  50, ] ;");
        assertThat(document.html()).contains("var skippedSteps =  [ 100,  300,  500, ] ;");
        assertThat(document.html()).contains("var pendingSteps =  [ 1000,  3000,  5000, ] ;");
        assertThat(document.html()).contains("var undefinedSteps =  [ 10000,  30000,  50000, ] ;");
        assertThat(document.html()).contains("var durations =  [ 3206126182398,  3206126182399,  3206126182310, ] ;");
    }
}
