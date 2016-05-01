package net.masterthought.cucumber.generators;

import net.masterthought.cucumber.generators.helpers.*;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class TagsOverviewPageIntegrationTest extends Page {

    @Test
    public void generatePage_generatesTitle() {

        // given
        setUpWithJson(SAMPLE_JOSN);
        page = new TagsOverviewPage(reportResult, configuration);
        final String titleValue = String.format("Cucumber-JVM Html Reports  - Tags Overview",
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
        page = new TagsOverviewPage(reportResult, configuration);

        // when
        page.generatePage();

        // then
        DocumentAssertion document = documentFrom(page.getWebPage());
        LeadAssertion lead = document.getLead();

        assertThat(lead.getHeader()).isEqualTo("Tags Statistics");
        assertThat(lead.getDescription()).isEqualTo("The following graph shows passing and failing statistics for tags");
    }

    @Test
    public void generatePage_generatesCharts() {

        // given
        setUpWithJson(SAMPLE_JOSN);
        page = new TagsOverviewPage(reportResult, configuration);

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
        page = new TagsOverviewPage(reportResult, configuration);

        // when
        page.generatePage();

        // then
        DocumentAssertion document = documentFrom(page.getWebPage());
        TableRowAssertion[] headerRows = document.getSummary().getTableStats().getHeaderRows();

        assertThat(headerRows).hasSize(2);

        TableRowAssertion firstRow = headerRows[0];
        firstRow.hasExactValues("", "Scenarios", "Steps", "", "");

        TableRowAssertion secondRow = headerRows[1];
        secondRow.hasExactValues("Tag", "Total", "Passed", "Failed", "Total", "Passed", "Failed", "Skipped", "Pending",
                "Undefined", "Missing", "Duration", "Status");
    }

    @Test
    public void generatePage_generatesStatsTableBody() {

        // given
        setUpWithJson(SAMPLE_JOSN);
        configuration.setStatusFlags(true, false, false, true);
        page = new TagsOverviewPage(reportResult, configuration);

        // when
        page.generatePage();

        // then
        DocumentAssertion document = documentFrom(page.getWebPage());
        TableRowAssertion[] bodyRows = document.getSummary().getTableStats().getBodyRows();

        assertThat(bodyRows).hasSize(3);

        TableRowAssertion firstRow = bodyRows[0];
        firstRow.hasExactValues("@checkout", "2", "0", "1", "16", "8", "1", "3", "2", "1", "1", "231 ms", "Failed");
        firstRow.hasExactCSSClasses("tagname", "", "", "", "", "", "failed", "skipped", "pending", "undefined", "missing", "duration", "failed");
        firstRow.hasExactDataValues("", "", "", "", "", "", "", "", "", "", "", "231054778", "");
        firstRow.getReportLink().hasLabelAndAddress("@checkout", "checkout.html");

        TableRowAssertion secondRow = bodyRows[1];
        secondRow.hasExactValues("@fast", "1", "0", "0", "7", "4", "0", "0", "2", "1", "0", "229 ms", "Failed");
        secondRow.hasExactCSSClasses("tagname", "", "", "", "", "", "", "", "pending", "undefined", "", "duration", "failed");
        secondRow.hasExactDataValues("", "", "", "", "", "", "", "", "", "", "", "229004778", "");
        secondRow.getReportLink().hasLabelAndAddress("@fast", "fast.html");

        TableRowAssertion lastRow = bodyRows[2];
        lastRow.hasExactValues("@featureTag", "1", "0", "0", "7", "4", "0", "0", "2", "1", "0", "229 ms", "Failed");
        lastRow.hasExactCSSClasses("tagname", "", "", "", "", "", "", "", "pending", "undefined", "", "duration", "failed");
        lastRow.hasExactDataValues("", "", "", "", "", "", "", "", "", "", "", "229004778", "");
        lastRow.getReportLink().hasLabelAndAddress("@featureTag", "featureTag.html");
    }

    @Test
    public void generatePage_generatesStatsTableFooter() {

        // given
        setUpWithJson(SAMPLE_JOSN);
        configuration.setStatusFlags(true, false, false, true);
        page = new TagsOverviewPage(reportResult, configuration);

        // when
        page.generatePage();

        // then
        DocumentAssertion document = documentFrom(page.getWebPage());
        TableRowAssertion footerCells = document.getSummary().getTableStats().getFooterRow();
        footerCells.hasExactValues("3", "4", "0", "2", "30", "16", "1", "3", "6", "3", "1", "689 ms", "Totals");
    }

    @Test
    public void generatePage_onEmptyJsons_generatesProperMessage() {

        // given
        setUpWithJson(EMPTY_JOSN);
        page = new TagsOverviewPage(reportResult, configuration);

        // when
        page.generatePage();

        // then
        DocumentAssertion document = documentFrom(page.getWebPage());
        SummaryAssertion summary = document.getSummary();
        assertThat(summary.getEmptyReportMessage()).isEqualTo("You have no tags in your cucumber report");
    }
}
