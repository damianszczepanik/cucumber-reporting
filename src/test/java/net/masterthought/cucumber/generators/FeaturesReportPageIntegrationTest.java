package net.masterthought.cucumber.generators;

import static org.assertj.core.api.Assertions.assertThat;

import org.jsoup.select.Elements;
import org.junit.Test;

import net.masterthought.cucumber.json.Feature;
import net.masterthought.cucumber.json.Tag;

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
        final String titleValue = String.format("Cucumber-JVM Html Reports  - Feature: %s", feature.getRawName());

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
        validateElements(firstRow, feature.getRawName(), "1", "1", "0", "10", "7", "0", "0", "2", "1", "0", "111ms",
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

        String firstKeyword = getFeatureKeyword(document).text();
        assertThat(firstKeyword).isEqualTo(feature.getKeyword() + ": " + feature.getName());

        Elements featureTags = getFeatureTags(featureDetails);
        assertThat(featureTags).hasSize(1);
        validateLink(featureTags.get(0), "featureTag.html", "@featureTag");

        String FeatureDescriptor = getFeatureDescription(featureDetails).text();
        assertThat(FeatureDescriptor).isEqualTo(feature.getDescription());
    }

    @Test
    public void generatePage_generatesScenarios() {

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
        validateElementKeyword(firstElement, feature.getElements()[0]);


        ElementWrapper secondElement = new ElementWrapper(elements.get(1));
        Elements secondFeatureTags = getFeatureTags(secondElement);
        Tag[] tags = feature.getElements()[1].getTags();
        assertThat(secondFeatureTags).hasSize(tags.length);
        for (int i = 0; i < tags.length; i++) {
            validateLink(secondFeatureTags.get(i), tags[i].getFileName(), tags[i].getName());
        }
        validateElementKeyword(secondElement, feature.getElements()[1]);
    }
}
