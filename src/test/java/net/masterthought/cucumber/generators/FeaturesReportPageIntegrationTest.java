package net.masterthought.cucumber.generators;

import static org.assertj.core.api.Assertions.assertThat;

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
        ElementWrapper[] bodyRows = getBodyOfStatsTable(document);

        assertThat(bodyRows).hasSize(1);

        ElementWrapper[] firstRow = getCells(bodyRows[0]);
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

        ElementWrapper brief = getBrief(featureDetails);
        validateBrief(brief, feature.getKeyword(), feature.getName());

        ElementWrapper[] tags = getTags(featureDetails);
        assertThat(tags).hasSize(1);
        validateLink(tags[0], "featureTag.html", "@featureTag");

        String descriptor = getDescription(featureDetails).text();
        assertThat(descriptor).isEqualTo(feature.getDescription());
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

        ElementWrapper[] elements = getElements(document);
        assertThat(elements).hasSize(feature.getElements().length);

        ElementWrapper firstElement = elements[1];
        Element scenario = feature.getElements()[1];

        ElementWrapper[] tags = getTags(firstElement);
        assertThat(tags).hasSize(scenario.getTags().length);
        for (int i = 0; i < tags.length; i++) {
            validateLink(tags[i], scenario.getTags()[i].getFileName(), scenario.getTags()[i].getName());
        }

        ElementWrapper brief = getBrief(firstElement);
        validateBrief(brief, scenario.getKeyword(), scenario.getName());

        String descriptor = getDescription(firstElement).text();
        assertThat(descriptor).isEqualTo(scenario.getDescription());
    }

    @Test
    public void generatePage_generatesScenarioHooks() {

        // given
        setUpWithJson(SAMPLE_JOSN);
        final Feature feature = features.get(1);
        page = new FeatureReportPage(reportResult, configuration, feature);

        // when
        page.generatePage();

        // then
        ElementWrapper document = documentFrom(page.getWebPage());

        ElementWrapper secondElement = getElements(document)[0];

        Element element = feature.getElements()[0];

        ElementWrapper[] before = getBefore(secondElement);
        assertThat(before).hasSize(element.getBefore().length);
        validateHook(before, element.getBefore(), "Before");

        ElementWrapper[] after = getAfter(secondElement);
        assertThat(after).hasSize(element.getAfter().length);
        validateHook(after, element.getAfter(), "After");
    }

}
