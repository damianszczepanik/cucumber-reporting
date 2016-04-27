package net.masterthought.cucumber.generators;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import net.masterthought.cucumber.generators.helpers.DocumentAssertion;
import net.masterthought.cucumber.generators.helpers.LeadAssertion;
import net.masterthought.cucumber.generators.helpers.SummaryAssertion;
import net.masterthought.cucumber.generators.helpers.TableRowAssertion;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class StepsOverviewPageIntegrationTest extends Page {

    @Test
    public void generatePage_generatesTitle() {

        // given
        setUpWithJson(SAMPLE_JOSN);
        configuration.setRunWithJenkins(true);
        configuration.setBuildNumber("333");
        page = new StepsOverviewPage(reportResult, configuration);
        final String titleValue = String.format("Cucumber-JVM Html Reports (no %s) - Steps Overview",
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
        page = new StepsOverviewPage(reportResult, configuration);

        // when
        page.generatePage();

        // then
        DocumentAssertion document = documentFrom(page.getWebPage());
        LeadAssertion lead = document.getLead();

        assertThat(lead.getHeader()).isEqualTo("Steps Statistics");
        assertThat(lead.getDescription()).isEqualTo("The following graph shows step statistics for this build."
                + " Below list is based on results. step does not provide information about result then is not listed below."
                + " Additionally @Before and @After are not counted because they are part of the scenarios, not steps.");
    }

    @Test
    public void generatePage_generatesStatsTableHeader() {

        // given
        setUpWithJson(SAMPLE_JOSN);
        page = new StepsOverviewPage(reportResult, configuration);

        // when
        page.generatePage();

        // then
        DocumentAssertion document = documentFrom(page.getWebPage());
        TableRowAssertion[] headerRows = document.getSummary().getTableStats().getHeaderRows();

        assertThat(headerRows).hasSize(1);

        TableRowAssertion firstRow = headerRows[0];
        firstRow.hasExactValues("Implementation", "Occurrences", "Duration", "Average", "Ratio");
    }

    @Test
    public void generatePage_generatesStatsTableBody() {

        // given
        setUpWithJson(SAMPLE_JOSN);
        page = new StepsOverviewPage(reportResult, configuration);

        // when
        page.generatePage();

        // then
        DocumentAssertion document = documentFrom(page.getWebPage());
        TableRowAssertion[] bodyRows = document.getSummary().getTableStats().getBodyRows();

        assertThat(bodyRows).hasSameSizeAs(steps);

        TableRowAssertion firstRow = bodyRows[1];
        firstRow.hasExactValues("ATMScenario.I_have_a_new_credit_card()", "1", "1m 39s 107 ms", "1m 39s 107 ms", "100.00%");
        firstRow.hasExactCSSClasses("location", "", "duration", "duration", "passed");
        firstRow.hasExactDataValues("", "", "99107447000", "99107447000", "");

        // also verify the average durations is written to data-values correctly
        TableRowAssertion secondRow = bodyRows[2];
        secondRow.hasExactDataValues("", "", "90000000", "45000000", "");
    }

    @Test
    public void generatePage_generatesStatsTableFooter() {

        // given
        setUpWithJson(SAMPLE_JOSN);
        configuration.setStatusFlags(true, false, false, true);
        page = new StepsOverviewPage(reportResult, configuration);

        // when
        page.generatePage();

        // then
        DocumentAssertion document = documentFrom(page.getWebPage());
        TableRowAssertion footerCells = document.getSummary().getTableStats().getFooterRow();

        footerCells.hasExactValues("15", "22", "1m 39s 482 ms", "04s 521 ms", "Totals");
    }

    @Test
    public void generatePage_onEmptyJsons_generatesProperMessage() {

        // given
        setUpWithJson(EMPTY_JOSN);
        page = new StepsOverviewPage(reportResult, configuration);

        // when
        page.generatePage();

        // then
        DocumentAssertion document = documentFrom(page.getWebPage());
        SummaryAssertion summary = document.getSummary();
        assertThat(summary.getEmptyReportMessage()).isEqualTo("You have no features in your cucumber report");
    }
}
