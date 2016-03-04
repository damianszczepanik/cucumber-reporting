package net.masterthought.cucumber.templates;

import static org.assertj.core.api.Assertions.assertThat;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

import net.masterthought.cucumber.generators.FeatureOverviewPage;
import net.masterthought.cucumber.generators.TagOverviewPage;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class CommonTemplateIntegrationTest extends TemplateUtility {

    @Before
    public void setup() {
        addReport("project3.json");
        createConfiguration();
    }

    @Test
    public void shouldHaveDefaultItemsInNaviBarforDefaultConfiguration() {

        // given
        createReportBuilder();

        FeatureOverviewPage featuresPage = new FeatureOverviewPage(reportResult, configuration);
        featuresPage.generatePage();
        ElementWrapper document = documentFrom(featuresPage.getWebPage());

        // when
        ElementWrapper navigation = extractNaviBar(document);
        Elements menuItems = extractNaviBarLinks(navigation);

        // then
        validatePluginName(navigation);
        assertThat(menuItems.size()).isEqualTo(3);

        validateFeaturesLink(menuItems.get(0));
        validateTagsLink(menuItems.get(1));
        validateStepsLink(menuItems.get(2));
    }

    @Test
    public void shouldHaveAllItemsInNaviBarforJenkinsConfiguration() {

        // given
        configuration.setRunWithJenkins(true);
        configuration.setBuildNumber("123");
        createReportBuilder();

        TagOverviewPage tagsPage = new TagOverviewPage(reportResult, configuration);
        tagsPage.generatePage();
        ElementWrapper document = documentFrom(tagsPage.getWebPage());

        // when
        ElementWrapper navigation = extractNaviBar(document);
        Elements menuItems = extractNaviBarLinks(navigation);

        // then
        validatePluginName(navigation);
        assertThat(menuItems.size()).isEqualTo(6);

        validateNaviBarLink(menuItems.get(0), "/job/" + configuration.getProjectName() + "/" + configuration.getBuildNumber(), "Jenkins");
        Integer prevBuildNumber = Integer.parseInt(configuration.getBuildNumber()) - 1;
        validateNaviBarLink(menuItems.get(1), "/job/" + configuration.getProjectName() + "/" + prevBuildNumber
                + "/cucumber-html-reports/" + tagsPage.getWebPage(), "Previous results");
        validateNaviBarLink(menuItems.get(2),
                "/job/" + configuration.getProjectName() + "/cucumber-html-reports/" + tagsPage.getWebPage(),
                "Last results");

        validateFeaturesLink(menuItems.get(3));
        validateTagsLink(menuItems.get(4));
        validateStepsLink(menuItems.get(5));
    }

    private static ElementWrapper extractNaviBar(ElementWrapper navigation) {
        return navigation.byId("navigation");
    }

    private static Elements extractNaviBarLinks(ElementWrapper document) {
        return document.bySelectors("a");
    }

    private static void validatePluginName(ElementWrapper navigation) {
        String pluginName = navigation.bySelector("p").text();
        assertThat(pluginName).isEqualTo("Cucumber-jvm Report");
    }

    private void validateFeaturesLink(Element featureLink) {
        validateNaviBarLink(featureLink, "feature-overview.html", "Features");
    }

    private void validateTagsLink(Element featureLink) {
        validateNaviBarLink(featureLink, "tag-overview.html", "Tags");
    }

    private void validateStepsLink(Element featureLink) {
        validateNaviBarLink(featureLink, "step-overview.html", "Steps");
    }

    private static void validateNaviBarLink(Element link, String href, String name) {
        assertThat(link.text()).isEqualTo(name);
        assertThat(link.attr("href")).isEqualTo(href);
    }
}
