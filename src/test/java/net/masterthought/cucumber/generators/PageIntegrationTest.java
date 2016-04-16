package net.masterthought.cucumber.generators;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class PageIntegrationTest extends Page {

    @Test
    public void generatePage_onDefaultConfiguration_generatesDefaultItemsInNaviBarfor() {

        // given
        setUpWithJson(SAMPLE_JOSN);
        page = new FeaturesOverviewPage(reportResult, configuration);

        // when
        page.generatePage();

        // then
        ElementWrapper document = documentFrom(page.getWebPage());
        ElementWrapper navigation = extractNaviBar(document);
        ElementWrapper[] menuItems = extractNaviBarLinks(navigation);

        validatePluginName(navigation);
        assertThat(menuItems).hasSize(3);

        validateFeaturesInNaviBar(menuItems[0]);
        validateTagsInNaviBar(menuItems[1]);
        validateStepsInNaviBar(menuItems[2]);
    }

    @Test
    public void generatePage_onJenkinsConfiguration_generatesAllItemsInNaviBarfor() {

        // given
        setUpWithJson(SAMPLE_JOSN);
        configuration.setRunWithJenkins(true);
        configuration.setBuildNumber("123");

        page = new TagReportPage(reportResult, configuration, reportResult.getAllTags().get(0));

        // when
        page.generatePage();

        // then
        ElementWrapper document = documentFrom(page.getWebPage());
        ElementWrapper navigation = extractNaviBar(document);
        ElementWrapper[] menuItems = extractNaviBarLinks(navigation);

        validatePluginName(navigation);
        assertThat(menuItems).hasSize(6);

        validateJenkinsInNaviBar(menuItems[0]);
        validatePreviousResultInNaviBar(menuItems[1]);
        validateLastResultInNaviBar(menuItems[2]);

        validateFeaturesInNaviBar(menuItems[3]);
        validateTagsInNaviBar(menuItems[4]);
        validateStepsInNaviBar(menuItems[5]);
    }

    @Test
    public void generatePage_onDefaultConfiguration_generatesSummaryTable() {

        // given
        setUpWithJson(SAMPLE_JOSN);
        page = new StepsOverviewPage(reportResult, configuration);

        // when
        page.generatePage();

        // then
        ElementWrapper document = documentFrom(page.getWebPage());
        ElementWrapper summary = extractBuildInfo(document);

        ElementWrapper[] headValues = extractSummaryHeaderValues(summary);
        assertThat(headValues).hasSize(2);
        validateProjectSummaryHeader(headValues[0]);
        validateDateSummaryHeader(headValues[1]);

        ElementWrapper[] bodyValues = extractSummaryBodyValues(summary);
        validateProjectSummaryBody(bodyValues[0]);
        validateBuildSummaryBody(bodyValues[1]);
    }

    @Test
    public void generatePage_onJenkinsConfiguration_generatesSummaryTableWithBuildNumber() {

        // given
        setUpWithJson(SAMPLE_JOSN);
        configuration.setRunWithJenkins(true);
        configuration.setBuildNumber("123");

        page = new StepsOverviewPage(reportResult, configuration);

        // when
        page.generatePage();

        // then
        ElementWrapper document = documentFrom(page.getWebPage());
        ElementWrapper summary = extractBuildInfo(document);

        ElementWrapper[] headValues = extractSummaryHeaderValues(summary);
        assertThat(headValues).hasSize(3);
        validateProjectSummaryHeader(headValues[0]);
        validateBuildNumberSummaryHeader(headValues[1]);
        validateDateSummaryHeader(headValues[2]);

        ElementWrapper[] bodyValues = extractSummaryBodyValues(summary);
        validateProjectSummaryBody(bodyValues[0]);
        validateBuildNumberSummaryBody(bodyValues[1]);
        validateBuildSummaryBody(bodyValues[2]);
    }

    @Test
    public void generatePage_generatesFooter() {

        // given
        setUpWithJson(SAMPLE_JOSN);
        page = new TagReportPage(reportResult, configuration, reportResult.getAllTags().get(0));

        // when
        page.generatePage();

        // then
        ElementWrapper document = documentFrom(page.getWebPage());
        ElementWrapper footer = extractFooter(document);
        ElementWrapper[] footerLinks = extractFooterLinks(footer);

        assertThat(footerLinks).hasSize(2);
        validateLink(footerLinks[0], "https://github.com/jenkinsci/cucumber-reports-plugin", "Jenkins Plugin");
        validateLink(footerLinks[1], "https://github.com/damianszczepanik/cucumber-reporting", "Cucumber-JVM Reports");
    }

    private ElementWrapper extractNaviBar(ElementWrapper document) {
        return document.byId("navigation");
    }

    private ElementWrapper[] extractNaviBarLinks(ElementWrapper navigation) {
        return navigation.allBySelector("a");
    }

    private void validatePluginName(ElementWrapper navigation) {
        String pluginName = navigation.oneBySelector("p").text();
        assertThat(pluginName).isEqualTo("Cucumber-JVM Report");
    }

    private void validateJenkinsInNaviBar(ElementWrapper featureLink) {
        validateLink(featureLink, "/job/" + configuration.getProjectName() + "/" + configuration.getBuildNumber(),
                "Jenkins");
    }

    private void validatePreviousResultInNaviBar(ElementWrapper featureLink) {
        final Integer prevBuildNumber = Integer.parseInt(configuration.getBuildNumber()) - 1;
        validateLink(featureLink, "/job/" + configuration.getProjectName() + "/" + prevBuildNumber
                + "/cucumber-html-reports/" + page.getWebPage(), "Previous results");
    }

    private void validateLastResultInNaviBar(ElementWrapper featureLink) {
        validateLink(featureLink,
                "/job/" + configuration.getProjectName() + "/cucumber-html-reports/" + page.getWebPage(),
                "Last results");
    }

    private void validateFeaturesInNaviBar(ElementWrapper featureLink) {
        validateLink(featureLink, "feature-overview.html", "Features");
    }

    private void validateTagsInNaviBar(ElementWrapper featureLink) {
        validateLink(featureLink, "tag-overview.html", "Tags");
    }

    private void validateStepsInNaviBar(ElementWrapper featureLink) {
        validateLink(featureLink, "step-overview.html", "Steps");
    }

    private ElementWrapper extractBuildInfo(ElementWrapper navigation) {
        return navigation.oneByClass("buildInfo");
    }

    private ElementWrapper[] extractSummaryHeaderValues(ElementWrapper document) {
        return document.allBySelector("thead > tr > th");
    }

    private void validateProjectSummaryHeader(ElementWrapper value) {
        assertThat(value.text()).isEqualTo("Project");
    }

    private void validateDateSummaryHeader(ElementWrapper value) {
        assertThat(value.text()).isEqualTo("Date");
    }

    private void validateBuildNumberSummaryHeader(ElementWrapper value) {
        assertThat(value.text()).isEqualTo("Number");
    }

    private ElementWrapper[] extractSummaryBodyValues(ElementWrapper document) {
        return document.allBySelector("tbody > tr > td");
    }

    private void validateProjectSummaryBody(ElementWrapper project) {
        assertThat(project.text()).isEqualTo(configuration.getProjectName());
    }

    private void validateBuildSummaryBody(ElementWrapper date) {
        // date format: dd MMM yyyy, HH:mm
        assertThat(date.text()).matches("^[0-3][0-9] \\w{3} \\d{4}, \\d{2}:\\d{2}$");
    }

    private void validateBuildNumberSummaryBody(ElementWrapper buildNumber) {
        assertThat(buildNumber.text()).isEqualTo(configuration.getBuildNumber());
    }

    private ElementWrapper extractFooter(ElementWrapper document) {
        return document.byId("footer");
    }

    private ElementWrapper[] extractFooterLinks(ElementWrapper footer) {
        return footer.allBySelector("a");
    }

}
