package net.masterthought.cucumber.generators;

import static org.assertj.core.api.Assertions.assertThat;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
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
        Elements menuItems = extractNaviBarLinks(navigation);

        validatePluginName(navigation);
        assertThat(menuItems).hasSize(3);

        validateFeaturesInNaviBar(menuItems.get(0));
        validateTagsInNaviBar(menuItems.get(1));
        validateStepsInNaviBar(menuItems.get(2));
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
        Elements menuItems = extractNaviBarLinks(navigation);

        validatePluginName(navigation);
        assertThat(menuItems).hasSize(6);

        validateJenkinsInNaviBar(menuItems.get(0));
        validatePreviousResultInNaviBar(menuItems.get(1));
        validateLastResultInNaviBar(menuItems.get(2));

        validateFeaturesInNaviBar(menuItems.get(3));
        validateTagsInNaviBar(menuItems.get(4));
        validateStepsInNaviBar(menuItems.get(5));
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
        ElementWrapper summary = extractHeader(document);

        Elements headValues = extractSummaryHeaderValues(summary);
        assertThat(headValues).hasSize(2);
        validateProjectSummaryHeader(headValues.get(0));
        validateDateSummaryHeader(headValues.get(1));

        Elements bodyValues = extractSummaryBodyValues(summary);
        validateProjectSummaryBody(bodyValues.get(0));
        validateBuildSummaryBody(bodyValues.get(1));
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
        ElementWrapper summary = extractHeader(document);

        Elements headValues = extractSummaryHeaderValues(summary);
        assertThat(headValues).hasSize(3);
        validateProjectSummaryHeader(headValues.get(0));
        validateBuildNumberSummaryHeader(headValues.get(1));
        validateDateSummaryHeader(headValues.get(2));

        Elements bodyValues = extractSummaryBodyValues(summary);
        validateProjectSummaryBody(bodyValues.get(0));
        validateBuildNumberSummaryBody(bodyValues.get(1));
        validateBuildSummaryBody(bodyValues.get(2));
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
        Elements footerLinks = extractFooterLinks(footer);

        assertThat(footerLinks).hasSize(2);
        validateLink(footerLinks.get(0), "https://github.com/jenkinsci/cucumber-reports-plugin", "Jenkins Plugin");
        validateLink(footerLinks.get(1), "https://github.com/damianszczepanik/cucumber-reporting", "Cucumber-JVM Reports");
    }

    private ElementWrapper extractNaviBar(ElementWrapper document) {
        return document.byId("navigation");
    }

    private Elements extractNaviBarLinks(ElementWrapper navigation) {
        return navigation.bySelectors("a");
    }

    private void validatePluginName(ElementWrapper navigation) {
        String pluginName = navigation.bySelector("p").text();
        assertThat(pluginName).isEqualTo("Cucumber-JVM Report");
    }

    private void validateJenkinsInNaviBar(Element featureLink) {
        validateLink(featureLink, "/job/" + configuration.getProjectName() + "/" + configuration.getBuildNumber(),
                "Jenkins");
    }

    private void validatePreviousResultInNaviBar(Element featureLink) {
        final Integer prevBuildNumber = Integer.parseInt(configuration.getBuildNumber()) - 1;
        validateLink(featureLink, "/job/" + configuration.getProjectName() + "/" + prevBuildNumber
                + "/cucumber-html-reports/" + page.getWebPage(), "Previous results");
    }

    private void validateLastResultInNaviBar(Element featureLink) {
        validateLink(featureLink,
                "/job/" + configuration.getProjectName() + "/cucumber-html-reports/" + page.getWebPage(),
                "Last results");
    }

    private void validateFeaturesInNaviBar(Element featureLink) {
        validateLink(featureLink, "feature-overview.html", "Features");
    }

    private void validateTagsInNaviBar(Element featureLink) {
        validateLink(featureLink, "tag-overview.html", "Tags");
    }

    private void validateStepsInNaviBar(Element featureLink) {
        validateLink(featureLink, "step-overview.html", "Steps");
    }

    private ElementWrapper extractHeader(ElementWrapper navigation) {
        return navigation.byId("header");
    }

    private Elements extractSummaryHeaderValues(ElementWrapper document) {
        return document.bySelectors("thead > tr > th");
    }

    private void validateProjectSummaryHeader(Element value) {
        assertThat(value.text()).isEqualTo("Project");
    }

    private void validateDateSummaryHeader(Element value) {
        assertThat(value.text()).isEqualTo("Date");
    }

    private void validateBuildNumberSummaryHeader(Element value) {
        assertThat(value.text()).isEqualTo("Number");
    }

    private Elements extractSummaryBodyValues(ElementWrapper document) {
        return document.bySelectors("tbody > tr > td");
    }

    private void validateProjectSummaryBody(Element project) {
        assertThat(project.text()).isEqualTo(configuration.getProjectName());
    }

    private void validateBuildSummaryBody(Element date) {
        // date format: dd MMM yyyy, HH:mm
        assertThat(date.text()).matches("^[0-3][0-9] \\w{3} \\d{4}, \\d{2}:\\d{2}$");
    }

    private void validateBuildNumberSummaryBody(Element buildNumber) {
        assertThat(buildNumber.text()).isEqualTo(configuration.getBuildNumber());
    }

    private ElementWrapper extractFooter(ElementWrapper document) {
        return document.byId("footer");
    }

    private Elements extractFooterLinks(ElementWrapper footer) {
        return footer.bySelectors("a");
    }

}
