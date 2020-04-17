package net.masterthought.cucumber.generators.integrations;

import static org.assertj.core.api.Assertions.assertThat;

import net.masterthought.cucumber.generators.TagsOverviewPage;
import net.masterthought.cucumber.generators.integrations.helpers.DocumentAssertion;
import net.masterthought.cucumber.generators.integrations.helpers.LeadAssertion;
import net.masterthought.cucumber.generators.integrations.helpers.SummaryAssertion;
import net.masterthought.cucumber.generators.integrations.helpers.TableRowAssertion;
import net.masterthought.cucumber.generators.integrations.helpers.WebAssertion;
import org.junit.Test;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class TagsOverviewPageIntegrationTest extends PageTest {

    @Test
    public void generatePage_generatesTitle() {

        // given
        setUpWithJson(SAMPLE_JSON);
        page = new TagsOverviewPage(reportResult, configuration);
        final String titleValue = String.format("Cucumber Reports  - Tags Overview",
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
    public void generatePage_insertsChartData() {

        // given
        setUpWithJson(SAMPLE_JSON);
        page = new TagsOverviewPage(reportResult, configuration);

        // when
        page.generatePage();

        // then
        DocumentAssertion document = documentFrom(page.getWebPage());

        // check that data used by the charts is correctly inserted into the script section

        assertThat(document.html()).contains("labels:  [ \"@checkout\",  \"@fast\",  \"@featureTag\", ]");
        assertThat(document.html()).contains("data:  [ 62.50,  100.00,  100.00, ]");
        assertThat(document.html()).contains("data:  [ 6.25,  0.00,  0.00, ]");
        assertThat(document.html()).contains("data:  [ 12.50,  0.00,  0.00, ]");
        assertThat(document.html()).contains("data:  [ 6.25,  0.00,  0.00, ]");
        assertThat(document.html()).contains("data:  [ 12.50,  0.00,  0.00, ]");

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
        TableRowAssertion[] headerRows = document.getReport().getTableStats().getHeaderRows();

        assertThat(headerRows).hasSize(2);

        TableRowAssertion firstRow = headerRows[0];
        firstRow.hasExactValues("", "Steps", "Scenarios", "Features");

        TableRowAssertion secondRow = headerRows[1];
        secondRow.hasExactValues("Tag", "Passed", "Failed", "Skipped", "Pending", "Undefined", "Total",
                "Passed", "Failed", "Total", "Duration", "Status");
    }

    @Test
    public void generatePage_generatesStatsTableBody() {

        // given
        setUpWithJson(SAMPLE_JSON);
        page = new TagsOverviewPage(reportResult, configuration);

        // when
        page.generatePage();

        // then
        DocumentAssertion document = documentFrom(page.getWebPage());
        TableRowAssertion[] bodyRows = document.getReport().getTableStats().getBodyRows();

        assertThat(bodyRows).hasSize(3);

        TableRowAssertion firstRow = bodyRows[0];
        firstRow.hasExactValues("@checkout", "10", "1", "2", "1", "2", "16", "1", "1", "2", "0.231", "Failed");
        firstRow.hasExactCSSClasses("tagname", "passed", "failed", "skipped", "pending", "undefined", "total", "passed", "failed", "total", "duration", "failed");
        firstRow.hasExactDataValues("", "", "", "", "", "", "", "", "", "", "231054778", "");
        firstRow.getReportLink().hasLabelAndAddress("@checkout", "report-tag_3971419525.html");

        TableRowAssertion secondRow = bodyRows[1];
        secondRow.hasExactValues("@fast", "6", "0", "0", "0", "0", "6", "1", "0", "1", "0.139", "Passed");
        secondRow.hasExactCSSClasses("tagname", "passed", "", "", "", "", "total", "passed", "", "total", "duration", "passed");
        secondRow.hasExactDataValues("", "", "", "", "", "", "", "", "", "", "139004778", "");
        secondRow.getReportLink().hasLabelAndAddress("@fast", "report-tag_2209724571.html");

        TableRowAssertion lastRow = bodyRows[2];
        lastRow.hasExactValues("@featureTag", "6", "0", "0", "0", "0", "6", "1", "0", "1", "0.139", "Passed");
        lastRow.hasExactCSSClasses("tagname", "passed", "", "", "", "", "total", "passed", "", "total", "duration", "passed");
        lastRow.hasExactDataValues("", "", "", "", "", "", "", "", "", "", "139004778", "");
        lastRow.getReportLink().hasLabelAndAddress("@featureTag", "report-tag_2956005635.html");
    }

    @Test
    public void generatePage_WithExculedTags_generatesStatsTableBody() {

        // given
        configuration.setTagsToExcludeFromChart("@checkout", "@feature.*");
        setUpWithJson(SAMPLE_JSON);
        page = new TagsOverviewPage(reportResult, configuration);

        // when
        page.generatePage();

        // then
        DocumentAssertion document = documentFrom(page.getWebPage());
        TableRowAssertion[] bodyRows = document.getReport().getTableStats().getBodyRows();

        assertThat(bodyRows).hasSize(1);

        TableRowAssertion firstRow = bodyRows[0];
        firstRow.hasExactValues("@fast", "6", "0", "0", "0", "0", "6", "1", "0", "1", "0.139", "Passed");
        firstRow.hasExactCSSClasses("tagname", "passed", "", "", "", "", "total", "passed", "", "total", "duration", "passed");
        firstRow.hasExactDataValues("", "", "", "", "", "", "", "", "", "", "139004778", "");
        firstRow.getReportLink().hasLabelAndAddress("@fast", "report-tag_2209724571.html");
    }

    @Test
    public void generatePage_generatesStatsTableFooter() {

        // given
        setUpWithJson(SAMPLE_JSON);
        page = new TagsOverviewPage(reportResult, configuration);

        // when
        page.generatePage();

        // then
        DocumentAssertion document = documentFrom(page.getWebPage());
        TableRowAssertion[] footerRows = document.getReport().getTableStats().getAllFooterRows();

        assertThat(footerRows).hasSize(2);
        footerRows[0].hasExactValues("", "22", "1", "2", "1", "2", "28", "3", "1", "4", "0.509", "3");
        footerRows[1].hasExactValues("", "78.57%", "3.57%", "7.14%", "3.57%", "7.14%", "", "75.00%", "25.00%", "", "", "50.00%");
    }

    @Test
    public void generatePage_onJsonWithoutTags_generatesProperMessage() {

        // given
        setUpWithJson(SIMPLE_JSON);
        page = new TagsOverviewPage(reportResult, configuration);

        // when
        page.generatePage();

        // then
        DocumentAssertion document = documentFrom(page.getWebPage());
        SummaryAssertion summary = document.getReport();
        assertThat(summary.getEmptyReportMessage()).isEqualTo("You have no tags in your cucumber report");
    }
}
