package net.masterthought.cucumber.generators;

import net.masterthought.cucumber.generators.helpers.*;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class FeaturesOverviewPageIntegrationTest extends Page {

    @Test
    public void generatePage_generatesTitle() {

        // given
        setUpWithJson(SAMPLE_JOSN);
        configuration.setRunWithJenkins(true);
        configuration.setBuildNumber("1");
        page = new FeaturesOverviewPage(reportResult, configuration);
        final String titleValue = String.format("Cucumber-JVM Html Reports (no %s) - Features Overview",
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
        setUpWithJson(SAMPLE_JOSN);
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
        setUpWithJson(SAMPLE_JOSN);
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
        setUpWithJson(SAMPLE_JOSN);
        page = new FeaturesOverviewPage(reportResult, configuration);

        // when
        page.generatePage();

        // then
        DocumentAssertion document = documentFrom(page.getWebPage());
        TableRowAssertion[] headerRows = document.getSummary().getTableStats().getHeaderRows();

        assertThat(headerRows).hasSize(2);

        TableRowAssertion firstRow = headerRows[0];
        firstRow.hasExactValues("", "Scenarios", "Steps", "", "");

        TableRowAssertion secondRow = headerRows[1];
        secondRow.hasExactValues("Feature", "Total", "Passed", "Failed", "Total", "Passed", "Failed", "Skipped",
                "Pending", "Undefined", "Missing", "Duration", "Status");
    }

    @Test
    public void generatePage_generatesStatsTableBody() {

        // given
        setUpWithJson(SAMPLE_JOSN);
        configuration.setStatusFlags(true, false, false, true);
        page = new FeaturesOverviewPage(reportResult, configuration);

        // when
        page.generatePage();

        // then
        DocumentAssertion document = documentFrom(page.getWebPage());
        TableRowAssertion[] bodyRows = document.getSummary().getTableStats().getBodyRows();

        assertThat(bodyRows).hasSize(2);

        TableRowAssertion firstRow = bodyRows[0];
        firstRow.hasExactValues("1st feature", "1", "0", "0", "10", "7", "0", "0", "2", "1", "0", "1m 39s 343 ms",
                "Failed");
        firstRow.hasExactCSSClasses("tagname", "", "", "", "", "", "", "", "pending", "undefined", "", "duration", "failed");
        firstRow.hasExactDataValues("", "", "", "", "", "", "", "", "", "", "", "99343602889", "");
        firstRow.getReportLink().hasLabelAndAddress("1st feature", "net-masterthought-example-s--ATM-local-feature.html");

        TableRowAssertion secondRow = bodyRows[1];
        secondRow.hasExactValues("Second feature", "1", "0", "1", "9", "4", "1", "3", "0", "0", "1", "002 ms", "Failed");
        secondRow.hasExactCSSClasses("tagname", "", "", "", "", "", "failed", "skipped", "", "", "missing", "duration", "failed");
        secondRow.hasExactDataValues("", "", "", "", "", "", "", "", "", "", "", "2050000", "");
        secondRow.getReportLink().hasLabelAndAddress("Second feature", "net-masterthought-example-ATMK-feature.html");
    }

    @Test
    public void generatePage_generatesStatsTableFooter() {

        // given
        setUpWithJson(SAMPLE_JOSN);
        configuration.setStatusFlags(true, false, false, true);
        page = new FeaturesOverviewPage(reportResult, configuration);

        // when
        page.generatePage();

        // then
        DocumentAssertion document = documentFrom(page.getWebPage());
        TableRowAssertion footerCells = document.getSummary().getTableStats().getFooterRow();

        footerCells.hasExactValues("2", "2", "0", "1", "19", "11", "1", "3", "2", "1", "1", "1m 39s 345 ms", "Totals");
    }

    @Test
    public void generatePage_onEmptyJsons_generatesProperMessage() {

        // given
        setUpWithJson(EMPTY_JOSN);
        page = new FeaturesOverviewPage(reportResult, configuration);

        // when
        page.generatePage();

        // then
        DocumentAssertion document = documentFrom(page.getWebPage());
        SummaryAssertion summary = document.getSummary();
        assertThat(summary.getEmptyReportMessage()).isEqualTo("You have no features in your cucumber report");
    }
}
