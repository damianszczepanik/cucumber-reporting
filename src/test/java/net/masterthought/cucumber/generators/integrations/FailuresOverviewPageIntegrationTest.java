package net.masterthought.cucumber.generators.integrations;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import net.masterthought.cucumber.generators.FailuresOverviewPage;
import net.masterthought.cucumber.generators.integrations.helpers.DocumentAssertion;
import net.masterthought.cucumber.generators.integrations.helpers.ElementAssertion;
import net.masterthought.cucumber.generators.integrations.helpers.LeadAssertion;
import net.masterthought.cucumber.generators.integrations.helpers.SummaryAssertion;
import net.masterthought.cucumber.presentation.PresentationMode;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class FailuresOverviewPageIntegrationTest extends PageTest {

    @Test
    public void generatePage_generatesTitle() {

        // given
        setUpWithJson(SAMPLE_JSON);
        configuration.addPresentationModes(PresentationMode.RUN_WITH_JENKINS);
        configuration.setBuildNumber("1");
        page = new FailuresOverviewPage(reportResult, configuration);
        final String titleValue = String.format("Cucumber Reports (no %s) - Failures Overview",
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
        page = new FailuresOverviewPage(reportResult, configuration);

        // when
        page.generatePage();

        // then
        DocumentAssertion document = documentFrom(page.getWebPage());
        LeadAssertion lead = document.getLead();

        assertThat(lead.getHeader()).isEqualTo("Failures Overview");
        assertThat(lead.getDescription()).isEqualTo("The following summary displays scenarios that failed.");
    }

    @Test
    public void generatePage_onJsonWithoutFailedSteps_generatesProperMessage() {

        // given
        setUpWithJson(SIMPLE_JSON);
        page = new FailuresOverviewPage(reportResult, configuration);

        // when
        page.generatePage();

        // then
        DocumentAssertion document = documentFrom(page.getWebPage());
        SummaryAssertion summary = document.getReport();
        assertThat(summary.getEmptyReportMessage()).isEqualTo("You have no failed scenarios in your Cucumber report");
    }

    @Test
    public void generatePage_generatesSummary() {

        // given
        setUpWithJson(SAMPLE_JSON);
        page = new FailuresOverviewPage(reportResult, configuration);

        // when
        page.generatePage();

        // then
        DocumentAssertion document = documentFrom(page.getWebPage());
        ElementAssertion[] elements = document.getElements();
        assertThat(elements).hasSize(2);
        assertThat(elements[0].getBrief().getName()).isEqualTo(features.get(1).getElements()[0].getName());
    }
}
