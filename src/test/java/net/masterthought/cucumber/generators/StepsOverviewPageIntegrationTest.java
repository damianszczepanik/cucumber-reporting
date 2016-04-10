package net.masterthought.cucumber.generators;

import static org.assertj.core.api.Assertions.assertThat;

import org.jsoup.select.Elements;
import org.junit.Test;

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
        ElementWrapper document = documentFrom(page.getWebPage());
        String title = getTitle(document).text();

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
        ElementWrapper document = documentFrom(page.getWebPage());
        String leadHeader = getLeadHeader(document).text();
        String leadDescription = getLeadDescription(document).text();

        assertThat(leadHeader).isEqualTo("Steps Statistics");
        assertThat(leadDescription).isEqualTo("The following graph shows step statistics for this build."
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
        ElementWrapper document = documentFrom(page.getWebPage());
        ElementWrapper headerTable = getHeaderOfStatsTable(document);
        Elements headerRows = getRows(headerTable);

        assertThat(headerRows).hasSize(1);

        Elements firstRow = getHeaderCells(headerRows.get(0));
        validateElements(firstRow, "Implementation", "Occurrences", "Duration", "Average", "Ratio");
    }

    @Test
    public void generatePage_generatesStatsTableBody() {

        // given
        setUpWithJson(SAMPLE_JOSN);
        page = new StepsOverviewPage(reportResult, configuration);

        // when
        page.generatePage();

        // then
        ElementWrapper document = documentFrom(page.getWebPage());
        Elements bodyRows = getBodyOfStatsTable(document);

        assertThat(bodyRows).hasSize(14);

        Elements firstRow = getCells(bodyRows.get(0));
        validateElements(firstRow, "ATMScenario.I_have_a_new_credit_card()", "1", "107 ms", "107 ms",
                "100.00%");
        validateCSSClasses(firstRow, "location", "", "duration", "duration", "passed");

        Elements failedRow = getCells(bodyRows.get(4));
        validateElements(failedRow, "ATMScenario.createCreditCard()", "3", "033 ms", "011 ms", "33.33%");
        validateCSSClasses(failedRow, "location", "", "duration", "duration", "failed");

        Elements secondRow = getCells(bodyRows.get(10));
        validateElements(secondRow, "ATMScenario.checkMoney(int)", "2", "003 ms", "001 ms", "100.00%");
        validateCSSClasses(secondRow, "location", "", "duration", "duration", "passed");

        Elements lastRow = getCells(bodyRows.get(13));
        validateElements(lastRow, "ATMScenario.its_not_implemented()", "1", "000 ms", "000 ms", "0.00%");
        validateCSSClasses(lastRow, "location", "", "duration", "duration", "skipped");
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
        ElementWrapper document = documentFrom(page.getWebPage());
        Elements footerCells = getFooterCellsOfStatsTable(document);

        validateElements(footerCells, "14", "21", "418 ms", "019 ms", "Totals");
    }

    @Test
    public void generatePage_onEmptyJsons_generatesProperMessage() {

        // given
        setUpWithJson(EMPTY_JOSN);
        page = new StepsOverviewPage(reportResult, configuration);

        // when
        page.generatePage();

        // then
        ElementWrapper document = documentFrom(page.getWebPage());
        assertThat(getEmptyReportMessage(document).text()).isEqualTo("You have no features in your cucumber report");
    }
}
