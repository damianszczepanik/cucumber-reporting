package net.masterthought.cucumber.generators;

import static org.assertj.core.api.Assertions.assertThat;

import org.jsoup.select.Elements;
import org.junit.Test;

import net.masterthought.cucumber.json.Element;
import net.masterthought.cucumber.json.Feature;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class FeaturesReportPageIntegrationTest extends ReportPage {

    @Test
    public void generatePage_generatesTitle() {

        // given
        setUpWithJson(SAMPLE_JOSN);
        final Feature feature = features.get(0);
        page = new FeatureReportPage(reportResult, configuration, feature);
        final String titleValue = String.format("Cucumber-JVM Html Reports  - Feature: %s", feature.getName());

        // when
        page.generatePage();

        // then
        ElementWrapper document = documentFrom(page.getWebPage());
        String title = getTitle(document).text();

        assertThat(title).isEqualTo(titleValue);
    }

    @Test
    public void generatePage_generatesStatsTableBody() {

        // given
        setUpWithJson(SAMPLE_JOSN);
        configuration.setStatusFlags(true, false, false, true);
        final Feature feature = features.get(0);
        page = new FeatureReportPage(reportResult, configuration, feature);

        // when
        page.generatePage();

        // then
        ElementWrapper document = documentFrom(page.getWebPage());
        Elements bodyRows = getBodyOfStatsTable(document);

        assertThat(bodyRows).hasSize(1);

        Elements firstRow = getCells(bodyRows.get(0));
        validateElements(firstRow, feature.getName(), "1", "1", "0", "10", "7", "0", "0", "2", "1", "0", "343 ms",
                "Passed");
        validateCSSClasses(firstRow, "tagname", "", "", "", "", "", "", "", "pending", "undefined", "", "duration",
                "passed");
    }

    @Test
    public void generatePage_generatesFeatureDetails() {

        // given
        setUpWithJson(SAMPLE_JOSN);
        final Feature feature = features.get(0);
        page = new FeatureReportPage(reportResult, configuration, feature);

        // when
        page.generatePage();

        // then
        ElementWrapper document = documentFrom(page.getWebPage());
        ElementWrapper featureDetails = getFeatureDetails(document);

        validateBrief(featureDetails, feature.getKeyword(), feature.getName());

        Elements featureTags = getFeatureTags(featureDetails);
        assertThat(featureTags).hasSize(1);
        validateLink(featureTags.get(0), "featureTag.html", "@featureTag");

        String FeatureDescriptor = getFeatureDescription(featureDetails).text();
        assertThat(FeatureDescriptor).isEqualTo(feature.getDescription());
    }

    @Test
    public void generatePage_generatesScenarioDetails() {

        // given
        setUpWithJson(SAMPLE_JOSN);
        final Feature feature = features.get(0);
        page = new FeatureReportPage(reportResult, configuration, feature);

        // when
        page.generatePage();

        // then
        ElementWrapper document = documentFrom(page.getWebPage());

        Elements elements = getScenarios(document);
        assertThat(elements).hasSize(feature.getElements().length);

        ElementWrapper firstElement = new ElementWrapper(elements.get(0));
        Element scenario = feature.getElements()[0];
        validateBrief(firstElement, scenario.getKeyword(), scenario.getName());
    }

    @Test
    public void generatePage_generatesScenariosTags() {

        // given
        setUpWithJson(SAMPLE_JOSN);
        final Feature feature = features.get(0);
        page = new FeatureReportPage(reportResult, configuration, feature);

        // when
        page.generatePage();

        // then
        ElementWrapper document = documentFrom(page.getWebPage());

        Elements elements = getScenarios(document);
        assertThat(elements).hasSize(feature.getElements().length);

        ElementWrapper firstElement = new ElementWrapper(elements.get(0));
        Elements firstFeatureTags = getFeatureTags(firstElement);
        assertThat(firstFeatureTags).hasSize(feature.getElements()[0].getTags().length);
    }
}
