package net.masterthought.cucumber.generators.integrations;

import net.masterthought.cucumber.generators.FeaturesOverviewPage;
import net.masterthought.cucumber.generators.integrations.helpers.DocumentAssertion;
import net.masterthought.cucumber.generators.integrations.helpers.LeadAssertion;
import net.masterthought.cucumber.generators.integrations.helpers.TableRowAssertion;
import net.masterthought.cucumber.generators.integrations.helpers.WebAssertion;
import net.masterthought.cucumber.presentation.PresentationMode;
import org.apache.commons.io.FilenameUtils;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class FeaturesOverviewPageIntegrationTest extends PageTest {

    @Test
    public void generatePage_generatesTitle() {

        // given
        setUpWithJson(SAMPLE_JSON);
        configuration.addPresentationModes(PresentationMode.RUN_WITH_JENKINS);
        configuration.setBuildNumber("1");
        page = new FeaturesOverviewPage(reportResult, configuration);
        final String titleValue = String.format("Cucumber Reports (no %s) - Features Overview",
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
    public void generatePage_generatesClassifications() {

        // given
        final String[] names = {"Platform", "Browser", "Branch", "Repository"};
        final String[] values = {"Win", "Opera", "master", "<a href=\"example.com\" rel=\"nofollow noopener noreferrer\">Example Repository</a>"};
        setUpWithJson(SAMPLE_JSON);
        for (int i = 0; i < names.length; i++) {
            configuration.addClassifications(names[i], values[i]);
        }
        page = new FeaturesOverviewPage(reportResult, configuration);

        // when
        page.generatePage();

        // then
        DocumentAssertion document = documentFrom(page.getWebPage());
        TableRowAssertion[] classifications = document.getClassifications();

        assertThat(classifications).hasSize(names.length);
        for (int i = 0; i < names.length; i++) {
            String[] cells = classifications[i].getCellsHtml();
            assertThat(cells).containsExactly(names[i], values[i]);
        }
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
        TableRowAssertion[] headerRows = document.getReport().getTableStats().getHeaderRows();

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
        TableRowAssertion[] bodyRows = document.getReport().getTableStats().getBodyRows();

        assertThat(bodyRows).hasSize(2);

        TableRowAssertion firstRow = bodyRows[0];
        firstRow.hasExactValues("1st feature", "10", "0", "0", "0", "0", "10", "1", "0", "1", "1:39.263", "Passed");
        firstRow.hasExactCSSClasses("tagname", "passed", "", "", "", "", "total", "passed", "", "total", "duration", "passed");
        firstRow.hasExactDataValues("", "", "", "", "", "", "", "", "", "", "99263122889", "");
        firstRow.getReportLink().hasLabelAndAddress("1st feature", "report-feature_1920820787.html");

        TableRowAssertion secondRow = bodyRows[1];
        secondRow.hasExactValues("Second feature", "5", "1", "2", "1", "3", "12", "1", "2", "3", "0.092", "Failed");
        secondRow.hasExactCSSClasses("tagname", "passed", "failed", "skipped", "pending", "undefined", "total", "passed", "failed", "total", "duration", "failed");
        secondRow.hasExactDataValues("", "", "", "", "", "", "", "", "", "", "92610000", "");
        secondRow.getReportLink().hasLabelAndAddress("Second feature", "report-feature_1_1515379431.html");
    }

    @Test
    public void generatePage_WithSpecifiedQualifier_generatesStatsTableBody() {

        // given
        String testQualifier = "testQualifier";
        configuration.setQualifier(FilenameUtils.removeExtension(SAMPLE_JSON), testQualifier);
        setUpWithJson(SAMPLE_JSON);
        configuration.addPresentationModes(PresentationMode.PARALLEL_TESTING);
        page = new FeaturesOverviewPage(reportResult, configuration);

        // when
        page.generatePage();

        // then
        DocumentAssertion document = documentFrom(page.getWebPage());
        TableRowAssertion[] bodyRows = document.getReport().getTableStats().getBodyRows();

        assertThat(bodyRows).hasSize(2);

        TableRowAssertion firstRow = bodyRows[0];
        firstRow.hasExactValues("1st feature", testQualifier, "10", "0", "0", "0", "0", "10", "1", "0", "1", "1:39.263", "Passed");
    }

    @Test
    public void generatePage_OnParallelTesting_generatesStatsTableBody() {

        // given
        setUpWithJson(SAMPLE_JSON);
        configuration.addPresentationModes(PresentationMode.PARALLEL_TESTING);
        page = new FeaturesOverviewPage(reportResult, configuration);

        // when
        page.generatePage();

        // then
        DocumentAssertion document = documentFrom(page.getWebPage());
        TableRowAssertion[] bodyRows = document.getReport().getTableStats().getBodyRows();

        assertThat(bodyRows).hasSize(2);

        TableRowAssertion firstRow = bodyRows[0];
        firstRow.hasExactValues("1st feature", "sample", "10", "0", "0", "0", "0", "10", "1", "0", "1", "1:39.263", "Passed");
        firstRow.hasExactCSSClasses("tagname", "", "passed", "", "", "", "", "total", "passed", "", "total", "duration", "passed");
        firstRow.hasExactDataValues("", "", "", "", "", "", "", "", "", "", "", "99263122889", "");
        firstRow.getReportLink().hasLabelAndAddress("1st feature", "report-feature_1920820787.html");

        TableRowAssertion secondRow = bodyRows[1];
        secondRow.hasExactValues("Second feature", "sample", "5", "1", "2", "1", "3", "12", "1", "2", "3", "0.092", "Failed");
        secondRow.hasExactCSSClasses("tagname", "", "passed", "failed", "skipped", "pending", "undefined", "total", "passed", "failed", "total", "duration", "failed");
        secondRow.hasExactDataValues("", "", "", "", "", "", "", "", "", "", "", "92610000", "");
        secondRow.getReportLink().hasLabelAndAddress("Second feature", "report-feature_1_1515379431.html");
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
        TableRowAssertion[] footerRows = document.getReport().getTableStats().getAllFooterRows();

        assertThat(footerRows).hasSize(2);
        footerRows[0].hasExactValues("", "15", "1", "2", "1", "3", "22", "2", "2", "4", "1:39.355", "2");
        footerRows[1].hasExactValues("", "68.18%", "4.55%", "9.09%", "4.55%", "13.64%", "", "50.00%", "50.00%", "", "", "50.00%");
    }

    @Test
    public void generatePage_OnParallelTesting_generatesStatsTableFooter() {

        // given
        setUpWithJson(SAMPLE_JSON);
        configuration.addPresentationModes(PresentationMode.PARALLEL_TESTING);
        page = new FeaturesOverviewPage(reportResult, configuration);

        // when
        page.generatePage();

        // then
        DocumentAssertion document = documentFrom(page.getWebPage());
        TableRowAssertion[] footerRows = document.getReport().getTableStats().getAllFooterRows();

        assertThat(footerRows).hasSize(2);
        footerRows[0].hasExactValues("", "", "15", "1", "2", "1", "3", "22", "2", "2", "4", "1:39.355", "2");
        footerRows[1].hasExactValues("", "", "68.18%", "4.55%", "9.09%", "4.55%", "13.64%", "", "50.00%", "50.00%", "", "", "50.00%");
    }
}
