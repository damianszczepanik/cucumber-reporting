package net.masterthought.cucumber.generators.integrations;

import static org.assertj.core.api.Assertions.assertThat;

import net.masterthought.cucumber.generators.TagReportPage;
import net.masterthought.cucumber.generators.integrations.helpers.BriefAssertion;
import net.masterthought.cucumber.generators.integrations.helpers.DocumentAssertion;
import net.masterthought.cucumber.generators.integrations.helpers.ElementAssertion;
import net.masterthought.cucumber.generators.integrations.helpers.LinkAssertion;
import net.masterthought.cucumber.generators.integrations.helpers.TableRowAssertion;
import net.masterthought.cucumber.json.Step;
import net.masterthought.cucumber.json.support.TagObject;
import org.junit.jupiter.api.Test;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
class TagReportPageIntegrationTest extends PageTest {

    @Test
    void generatePage_generatesTitle() {

        // given
        setUpWithJson(SAMPLE_JSON);
        final TagObject tag = tags.get(0);
        page = new TagReportPage(reportResult, configuration, tag);
        final String titleValue = String.format("Cucumber Reports  - Tag: %s", tag.getName());

        // when
        page.generatePage();

        // then
        DocumentAssertion document = documentFrom(page.getWebPage());
        String title = document.getHead().getTitle();

        assertThat(title).isEqualTo(titleValue);
    }

    @Test
    void generatePage_generatesStatsTableBody() {

        // given
        setUpWithJson(SAMPLE_JSON);
        final TagObject tag = tags.get(0);
        page = new TagReportPage(reportResult, configuration, tag);

        // when
        page.generatePage();

        // then
        DocumentAssertion document = documentFrom(page.getWebPage());
        TableRowAssertion bodyRow = document.getReport().getTableStats().getBodyRow();

        bodyRow.hasExactValues(tag.getName(), "10", "1", "2", "1", "2", "16", "1", "1", "2", "0.231", "Failed");
        bodyRow.hasExactCSSClasses("tagname", "passed", "failed", "skipped", "pending", "undefined", "total", "passed", "failed", "total", "duration", "failed");
    }

    @Test
    void generatePage_generatesFeatureNames() {

        // given
        setUpWithJson(SAMPLE_JSON);
        final TagObject tag = tags.get(1);
        page = new TagReportPage(reportResult, configuration, tag);

        // when
        page.generatePage();

        // then
        DocumentAssertion document = documentFrom(page.getWebPage());
        ElementAssertion[] elements = document.getElements();

        LinkAssertion featureName = elements[0].getFeatureName();
        featureName.hasLabelAndAddress(features.get(0).getName(), features.get(0).getReportFileName());
    }

    @Test
    void generatePage_generatesTagsList() {

        // given
        setUpWithJson(SAMPLE_JSON);
        final TagObject tag = tags.get(0);
        page = new TagReportPage(reportResult, configuration, tag);

        // when
        page.generatePage();

        // then
        DocumentAssertion document = documentFrom(page.getWebPage());
        ElementAssertion[] elements = document.getElements();

        assertThat(elements).hasSameSizeAs(tags.get(0).getElements());
    }

    @Test
    void generatePage_generatesSteps() {

        // given
        setUpWithJson(SAMPLE_JSON);
        final TagObject tag = tags.get(1);
        page = new TagReportPage(reportResult, configuration, tag);

        // when
        page.generatePage();

        // then
        DocumentAssertion document = documentFrom(page.getWebPage());

        BriefAssertion stepElement = document.getElements()[0].getStepsSection().getSteps()[3].getBrief();
        Step step = tag.getElements().get(0).getSteps()[3];
        assertThat(stepElement.getName()).hasSameSizeAs(step.getName());
    }
}
