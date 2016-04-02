package net.masterthought.cucumber.generators;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class ErrorPageIntegrationTest extends Page {

    private final Exception cause = new IllegalArgumentException("Help me!");

    @Test
    public void generatePage_generatesTitle() {

        // given
        setUpWithJson(SAMPLE_JOSN);
        page = new ErrorPage(reportResult, configuration, cause, jsonReports);
        final String titleValue = String.format("Cucumber-JVM Html Reports  - Error Page");

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
        configuration.setBuildNumber("12");
        page = new ErrorPage(reportResult, configuration, cause, jsonReports);

        // when
        page.generatePage();

        // then
        ElementWrapper document = documentFrom(page.getWebPage());
        String leadHeader = getLeadHeader(document).text();
        String leadDescription = getLeadDescription(document).text();

        assertThat(leadHeader).isEqualTo("Error");
        assertThat(leadDescription).isEqualTo(String.format("Something went wrong with project %s, build %s",
                configuration.getProjectName(), configuration.getBuildNumber()));
    }

    @Test
    public void generatePage_generatesErrorMessage() {

        // given
        setUpWithJson(EMPTY_JOSN, SAMPLE_JOSN);
        page = new ErrorPage(reportResult, configuration, cause, jsonReports);

        // when
        page.generatePage();

        // then
        ElementWrapper document = documentFrom(page.getWebPage());
        String error = getErrorMessage(document).text();
        assertThat(error).contains(cause.getMessage());
        assertThat(error).contains(cause.getClass().getName());

        String details = getReportList(document).text();
        for (String fileName : jsonReports) {
            assertThat(details).contains(fileName);
        }
    }

    private ElementWrapper getErrorMessage(ElementWrapper document) {
        return getErrorSection(document).byClass("error-message");
    }

    private ElementWrapper getReportList(ElementWrapper document) {
        return getErrorSection(document).byClass("error-files");
    }

    private ElementWrapper getErrorSection(ElementWrapper document) {
        return document.byId("report");
    }
}
