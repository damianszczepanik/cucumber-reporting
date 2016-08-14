package net.masterthought.cucumber.generators.integrations;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import net.masterthought.cucumber.generators.TagsOverviewPage;
import net.masterthought.cucumber.generators.integrations.helpers.DocumentAssertion;
import net.masterthought.cucumber.generators.integrations.helpers.LeadAssertion;
import net.masterthought.cucumber.generators.integrations.helpers.SummaryAssertion;
import net.masterthought.cucumber.generators.integrations.helpers.TableRowAssertion;
import net.masterthought.cucumber.generators.integrations.helpers.WebAssertion;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class TagsOverviewPageIntegrationTest extends PageTest {

    @Test
    public void generatePage_generatesTitle() {

        // given
        setUpWithJson(SAMPLE_JSON);
        page = new TagsOverviewPage(reportResult, configuration);
        final String titleValue = String.format("Cucumber-JVM Reports  - Tags Overview",
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
        setUpWithJson(SAMPLE_JSON);
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
        setUpWithJson(SAMPLE_JSON);
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
        secondRow.hasExactValues("Tag", "Passed", "Failed", "Total", "Passed", "Failed", "Skipped", "Pending",
                "Undefined", "Total", "Duration", "Status");
    }

    @Test
    public void generatePage_generatesStatsTableBody() {

        // given
        setUpWithJson(SAMPLE_JSON);
        configuration.setStatusFlags(true, false, false);
        page = new TagsOverviewPage(reportResult, configuration);

        // when
        page.generatePage();

        // then
        DocumentAssertion document = documentFrom(page.getWebPage());
        TableRowAssertion[] bodyRows = document.getSummary().getTableStats().getBodyRows();

        assertThat(bodyRows).hasSize(3);

        TableRowAssertion firstRow = bodyRows[0];
        firstRow.hasExactValues("@checkout", "1", "1", "2", "8", "0", "4", "2", "2", "16", "231ms", "Failed");
        firstRow.hasExactCSSClasses("tagname", "passed", "failed", "total", "passed", "", "skipped", "pending", "undefined", "total", "duration", "failed");
        firstRow.hasExactDataValues("", "", "", "", "", "", "", "", "", "", "231054778", "");
        firstRow.getReportLink().hasLabelAndAddress("@checkout", "checkout.html");

        TableRowAssertion secondRow = bodyRows[1];
        secondRow.hasExactValues("@fast", "1", "0", "1", "4", "0", "0", "2", "1", "7", "229ms", "Passed");
        secondRow.hasExactCSSClasses("tagname", "passed", "", "total", "passed", "", "", "pending", "undefined", "total", "duration", "passed");
        secondRow.hasExactDataValues("", "", "", "", "", "", "", "", "", "", "229004778", "");
        secondRow.getReportLink().hasLabelAndAddress("@fast", "fast.html");

        TableRowAssertion lastRow = bodyRows[2];
        lastRow.hasExactValues("@featureTag", "1", "0", "1", "4", "0", "0", "2", "1", "7", "229ms", "Passed");
        lastRow.hasExactCSSClasses("tagname", "passed", "", "total", "passed", "", "", "pending", "undefined", "total", "duration", "passed");
        lastRow.hasExactDataValues("", "", "", "", "", "", "", "", "", "", "229004778", "");
        lastRow.getReportLink().hasLabelAndAddress("@featureTag", "featureTag.html");
    }

    @Test
    public void generatePage_generatesStatsTableFooter() {

        // given
        setUpWithJson(SAMPLE_JSON);
        configuration.setStatusFlags(true, false, false);
        page = new TagsOverviewPage(reportResult, configuration);

        // when
        page.generatePage();

        // then
        DocumentAssertion document = documentFrom(page.getWebPage());
        TableRowAssertion footerCells = document.getSummary().getTableStats().getFooterRow();
        footerCells.hasExactValues("3", "3", "1", "4", "16", "0", "4", "6", "4", "30", "689ms", "Totals");
    }

    @Test
    public void generatePage_onEmptyJsons_generatesProperMessage() {

        // given
        setUpWithJson(EMPTY_JSON);
        page = new TagsOverviewPage(reportResult, configuration);

        // when
        page.generatePage();

        // then
        DocumentAssertion document = documentFrom(page.getWebPage());
        SummaryAssertion summary = document.getSummary();
        assertThat(summary.getEmptyReportMessage()).isEqualTo("You have no tags in your cucumber report");
    }
}
