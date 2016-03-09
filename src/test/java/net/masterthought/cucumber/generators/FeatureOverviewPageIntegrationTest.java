package net.masterthought.cucumber.generators;

import static org.assertj.core.api.Assertions.assertThat;

import org.jsoup.select.Elements;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class FeatureOverviewPageIntegrationTest extends Page {

    private AbstractPage page;

    @Before
    public void setup() {
        addReport("sample.json");
        createConfiguration();
    }

    @After
    public void cleanup() {
        page = null;
    }

    @Test
    public void generatePage_generatesStatsTableHeader() {

        // given
        createReportBuilder();
        page = new FeatureOverviewPage(reportResult, configuration);

        // when
        page.generatePage();

        // then
        ElementWrapper document = documentFrom(page.getWebPage());
        ElementWrapper headerTable = extractHeaderStatsTable(document);
        Elements headerRows = getRows(headerTable);

        assertThat(headerRows).hasSize(2);

        Elements firstRow = getHeaderCells(headerRows.get(0));
        validateElements(firstRow, "", "Scenarios", "Steps", "", "");

        Elements secondRow = getHeaderCells(headerRows.get(1));
        validateElements(secondRow, "Feature", "Total", "Passed", "Failed", "Total", "Passed", "Failed", "Skipped",
                "Pending", "Undefined", "Missing", "Duration", "Status");
    }

    @Test
    public void generatePage_generatesStatsTableBody() {

        // given
        createReportBuilder();
        page = new FeatureOverviewPage(reportResult, configuration);

        // when
        page.generatePage();

        // then
        ElementWrapper document = documentFrom(page.getWebPage());
        Elements bodyRows = extractBodyStatsTable(document);

        assertThat(bodyRows).hasSize(2);

        Elements firstRow = getCells(bodyRows.get(0));
        validateElements(firstRow, "First feature", "1", "1", "0", "10", "7", "0", "0", "2", "1", "0", "111ms",
                "Passed");

        Elements secondRow = getCells(bodyRows.get(1));
        validateElements(secondRow, "2nd feature", "1", "0", "1", "9", "4", "1", "3", "0", "0", "1", "002ms", "Failed");
    }

    @Test
    public void generatePage_generatesStatsTableFooter() {

        // given
        createReportBuilder();
        configuration.setStatusFlags(true, false, false, true);
        page = new FeatureOverviewPage(reportResult, configuration);

        // when
        page.generatePage();

        // then
        ElementWrapper document = documentFrom(page.getWebPage());
        Elements footerCells = extractFooterCellsInStatsTable(document);

        validateElements(footerCells, "2", "2", "1", "1", "19", "11", "1", "3", "2", "1", "1", "113ms", "Totals");
    }

}
