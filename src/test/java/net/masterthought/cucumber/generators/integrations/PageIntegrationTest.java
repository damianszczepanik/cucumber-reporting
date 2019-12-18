package net.masterthought.cucumber.generators.integrations;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

import net.masterthought.cucumber.generators.FailuresOverviewPage;
import net.masterthought.cucumber.generators.FeaturesOverviewPage;
import net.masterthought.cucumber.generators.StepsOverviewPage;
import net.masterthought.cucumber.generators.TagReportPage;
import net.masterthought.cucumber.generators.integrations.helpers.BuildInfoAssertion;
import net.masterthought.cucumber.generators.integrations.helpers.DocumentAssertion;
import net.masterthought.cucumber.generators.integrations.helpers.LinkAssertion;
import net.masterthought.cucumber.generators.integrations.helpers.NavigationAssertion;
import net.masterthought.cucumber.generators.integrations.helpers.NavigationItemAssertion;
import net.masterthought.cucumber.generators.integrations.helpers.TableRowAssertion;
import net.masterthought.cucumber.generators.integrations.helpers.WebAssertion;
import net.masterthought.cucumber.presentation.PresentationMode;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class PageIntegrationTest extends PageTest {

    @Before
    public void prepare() {
        Locale.setDefault(Locale.UK);
    }

    @Test
    public void generatePage_onDefaultConfiguration_generatesDefaultItemsInNaviBar() {

        // given
        setUpWithJson(SAMPLE_JSON);
        page = new FeaturesOverviewPage(reportResult, configuration);

        // when
        page.generatePage();

        // then
        DocumentAssertion document = documentFrom(page.getWebPage());
        NavigationAssertion navigation = document.getNavigation();
        NavigationItemAssertion[] menuItems = navigation.getNaviBarLinks();

        navigation.hasPluginName();
        assertThat(menuItems).hasSize(4);

        menuItems[0].hasLinkToFeatures();
        menuItems[1].hasLinkToTags();
        menuItems[2].hasLinkToSteps();
        menuItems[3].hasLinkToFailures();
    }

    @Test
    public void generatePage_onJenkinsConfiguration_generatesAllItemsInNaviBar() {

        // given
        setUpWithJson(SAMPLE_JSON);
        configuration.addPresentationModes(PresentationMode.RUN_WITH_JENKINS);
        configuration.setBuildNumber("123");

        page = new TagReportPage(reportResult, configuration, reportResult.getAllTags().get(0));

        // when
        page.generatePage();

        // then
        DocumentAssertion document = documentFrom(page.getWebPage());
        NavigationAssertion navigation = document.getNavigation();
        NavigationItemAssertion[] menuItems = navigation.getNaviBarLinks();

        navigation.hasPluginName();
        assertThat(navigation.getNaviBarLinks()).hasSize(7);

        menuItems[0].hasLinkToJenkins(configuration);
        menuItems[1].hasLinkToPreviousResult(configuration, page.getWebPage());
        menuItems[2].hasLinkToLastResult(configuration, page.getWebPage());
    }

    @Test
    public void generatePage_onTrendsStatsFile_generatesAllItemsInNaviBar() {

        // given
        setUpWithJson(SAMPLE_JSON);
        configuration.setTrendsStatsFile(new File("someTmpFile"));

        page = new FailuresOverviewPage(reportResult, configuration);

        // when
        page.generatePage();

        // then
        DocumentAssertion document = documentFrom(page.getWebPage());
        NavigationAssertion navigation = document.getNavigation();
        NavigationItemAssertion[] menuItems = navigation.getNaviBarLinks();

        navigation.hasPluginName();
        assertThat(navigation.getNaviBarLinks()).hasSize(5);

        menuItems[3].hasLinkToTrends();
    }

    @Test
    public void generatePage_onDefaultConfiguration_generatesSummaryTable() {

        // given
        setUpWithJson(SAMPLE_JSON);
        page = new StepsOverviewPage(reportResult, configuration);

        // when
        page.generatePage();

        // then
        DocumentAssertion document = documentFrom(page.getWebPage());
        BuildInfoAssertion buildInfo = document.getBuildInfo();

        TableRowAssertion headValues = buildInfo.getHeaderRow();
        headValues.hasExactValues("Project", "Date");

        assertThat(buildInfo.getProjectName()).isEqualTo(configuration.getProjectName());
        buildInfo.hasBuildDate(false);
    }

    @Test
    public void generatePage_onJenkinsConfiguration_generatesSummaryTableWithBuildNumber() {

        // given
        setUpWithJson(SAMPLE_JSON);
        configuration.addPresentationModes(PresentationMode.RUN_WITH_JENKINS);
        configuration.setBuildNumber("123");

        page = new StepsOverviewPage(reportResult, configuration);

        // when
        page.generatePage();

        // then
        DocumentAssertion document = documentFrom(page.getWebPage());
        BuildInfoAssertion buildInfo = document.getBuildInfo();

        TableRowAssertion headValues = buildInfo.getHeaderRow();
        headValues.hasExactValues("Project", "Number", "Date");

        assertThat(buildInfo.getProjectName()).isEqualTo(configuration.getProjectName());
        assertThat(buildInfo.getBuildNumber()).isEqualTo(configuration.getBuildNumber());
        buildInfo.hasBuildDate(true);
    }

    @Test
    public void generatePage_generatesFooter() {

        // given
        setUpWithJson(SAMPLE_JSON);
        page = new TagReportPage(reportResult, configuration, reportResult.getAllTags().get(0));

        // when
        page.generatePage();

        // then
        DocumentAssertion document = documentFrom(page.getWebPage());
        WebAssertion footer = extractFooter(document);
        LinkAssertion[] footerLinks = extractFooterLinks(footer);

        assertThat(footerLinks).hasSize(4);
        footerLinks[0].hasLabelAndAddress("Jenkins Plugin", "https://github.com/jenkinsci/cucumber-reports-plugin");
        footerLinks[1].hasLabelAndAddress("Standalone", "https://github.com/damianszczepanik/cucumber-reporting");
        footerLinks[2].hasLabelAndAddress("Sandwich", "https://github.com/damianszczepanik/cucumber-sandwich");
        footerLinks[3].hasLabelAndAddress("Maven", "https://github.com/damianszczepanik/maven-cucumber-reporting");
    }

    private WebAssertion extractFooter(WebAssertion document) {
        return document.byId("footer", WebAssertion.class);
    }

    private LinkAssertion[] extractFooterLinks(WebAssertion footer) {
        return footer.allBySelector("a", LinkAssertion.class);
    }

}
