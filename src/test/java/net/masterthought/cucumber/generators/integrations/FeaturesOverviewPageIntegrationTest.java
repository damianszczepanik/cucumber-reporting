package net.masterthought.cucumber.generators.integrations;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import net.masterthought.cucumber.generators.FeaturesOverviewPage;
import net.masterthought.cucumber.generators.integrations.helpers.DocumentAssertion;
import net.masterthought.cucumber.generators.integrations.helpers.LeadAssertion;
import net.masterthought.cucumber.generators.integrations.helpers.TableRowAssertion;
import net.masterthought.cucumber.generators.integrations.helpers.WebAssertion;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class FeaturesOverviewPageIntegrationTest extends PageTest {

    @Test
    public void generatePage_generatesTitle() {

        // given
        setUpWithJson(SAMPLE_JSON);
        configuration.setRunWithJenkins(true);
        configuration.setBuildNumber("1");
        page = new FeaturesOverviewPage(reportResult, configuration);
        final String titleValue = String.format("Cucumber-JVM Reports (no %s) - Features Overview",
                configuration.getBuildNumber());

        // when
        page.generatePage();

        // then
        DocumentAssertion document = documentFrom(page.getWebPage());
        String title = document.getHead().getTitle();

        assertThat(title).isEqualTo(titleValue);
    }

    @Test
    public void generatePage_generatesLead() {

        // given
        setUpWithJson(SAMPLE_JSON);
        page = new FeaturesOverviewPage(reportResult, configuration);

        // when
        page.generatePage();

        // then
        DocumentAssertion document = documentFrom(page.getWebPage());
        LeadAssertion lead = document.getLead();

        assertThat(lead.getHeader()).isEqualTo("Features Statistics");
        assertThat(lead.getDescription()).isEqualTo("The following graphs show passing and failing statistics for features");
    }

    @Test
    public void generatePage_generatesCharts() {

        // given
        setUpWithJson(SAMPLE_JSON);
        page = new FeaturesOverviewPage(reportResult, configuration);

        // when
        page.generatePage();

        // then
        DocumentAssertion document = documentFrom(page.getWebPage());

        assertThat(document.byId("charts", WebAssertion.class)).isNotNull();
    }

    @Test
    public void generatePage_generatesStatsTableHeader() {

        // given
        setUpWithJson(SAMPLE_JSON);
        page = new FeaturesOverviewPage(reportResult, configuration);

        // when
        page.generatePage();

        // then
        DocumentAssertion document = documentFrom(page.getWebPage());
        TableRowAssertion[] headerRows = document.getSummary().getTableStats().getHeaderRows();

        assertThat(headerRows).hasSize(2);

        TableRowAssertion firstRow = headerRows[0];
        firstRow.hasExactValues("", "Steps", "Scenarios", "Features");

        TableRowAssertion secondRow = headerRows[1];
        secondRow.hasExactValues("Feature", "Passed", "Failed", "Skipped", "Pending", "Undefined", "Total",
                "Passed", "Failed", "Total", "Duration", "Status");
    }

    @Test
    public void generatePage_generatesStatsTableBody() {

        // given
        setUpWithJson(SAMPLE_JSON);
        page = new FeaturesOverviewPage(reportResult, configuration);

        // when
        page.generatePage();

        // then
        DocumentAssertion document = documentFrom(page.getWebPage());
        TableRowAssertion[] bodyRows = document.getSummary().getTableStats().getBodyRows();

        assertThat(bodyRows).hasSize(2);

        TableRowAssertion firstRow = bodyRows[0];
        firstRow.hasExactValues("1st feature", "10", "0", "0", "0", "0", "10", "1", "0", "1", "1m 39s 263ms", "Passed");
        firstRow.hasExactCSSClasses("tagname", "passed", "", "", "", "", "total", "passed", "", "total", "duration", "passed");
        firstRow.hasExactDataValues("", "", "", "", "", "", "", "", "", "", "99263122889", "");
        firstRow.getReportLink().hasLabelAndAddress("1st feature", "report-feature_net-masterthought-example-s--ATM-local-feature.html");

        TableRowAssertion secondRow = bodyRows[1];
        secondRow.hasExactValues("Second feature", "5", "1", "2", "1", "2", "11", "1", "1", "2", "092ms", "Failed");
        secondRow.hasExactCSSClasses("tagname", "passed", "failed", "skipped", "pending", "undefined", "total", "passed", "failed", "total", "duration", "failed");
        secondRow.hasExactDataValues("", "", "", "", "", "", "", "", "", "", "92610000", "");
        secondRow.getReportLink().hasLabelAndAddress("Second feature", "report-feature_net-masterthought-example-ATMK-feature.html");
    }

    @Test
    public void generatePage_generatesStatsTableFooter() {

        // given
        setUpWithJson(SAMPLE_JSON);
        page = new FeaturesOverviewPage(reportResult, configuration);

        // when
        page.generatePage();

        // then
        DocumentAssertion document = documentFrom(page.getWebPage());
        TableRowAssertion[] footerRows = document.getSummary().getTableStats().getAllFooterRows();

        assertThat(footerRows).hasSize(2);
        footerRows[0].hasExactValues("2", "15", "1", "2", "1", "2", "21", "2", "1", "3", "1m 39s 355ms", "");
        footerRows[1].hasExactValues("", "71.43%", "4.76%", "9.52%", "4.76%", "9.52%", "", "66.67%", "33.33%", "", "", "33.33%");

    }
}
