package net.masterthought.cucumber.generators;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.velocity.VelocityContext;
import org.junit.Before;
import org.junit.Test;

import net.masterthought.cucumber.generators.integrations.PageTest;
import net.masterthought.cucumber.json.Element;
import net.masterthought.cucumber.json.Embedding;
import net.masterthought.cucumber.json.Feature;
import net.masterthought.cucumber.json.Step;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class FeatureReportPageTest extends PageTest {

    @Before
    public void setUp() {
        setUpWithJson(SAMPLE_JSON);
    }

    @Test
    public void getWebPage_ReturnsFeatureFileName() {

        // given
        Feature feature = features.get(1);
        page = new FeatureReportPage(feature);

        // when
        String fileName = page.getWebPage();

        // then
        assertThat(fileName).isEqualTo(feature.getReportFileName());
    }

    @Test
    public void prepareReport_AddsCustomProperties() {

        // given
    	VelocityContext context = new VelocityContext();
        Feature feature = features.get(1);
        page = new FeatureReportPage(feature);

        // when
        page.preparePageContext(context, configuration, reportResult);

        // then
        assertThat(context.getKeys()).hasSize(2);
        assertThat(context.get("parallel")).isEqualTo(configuration.isParallelTesting());
        assertThat(context.get("feature")).isEqualTo(feature);
    }

    @Test
    public void getMimeType_OnEmbeddingFromV2CucumberReportFile_SupportsScreenshots() {
        // given
        Feature feature = features.get(0);
        Element element = feature.getElements()[0];
        Step step = element.getSteps()[0];

        // when
        Embedding[] embeddings = step.getEmbeddings();

        // then
        assertThat(embeddings[0].getMimeType()).isEqualTo("image/url");
    }

    @Test
    public void getMimeType_OnEmbeddingFromV3CucumberReportFile_SupportsScreenshots() {
        // given
        Feature feature = features.get(0);
        Element element = feature.getElements()[0];
        Step step = element.getSteps()[0];

        // when
        Embedding[] embeddings = step.getEmbeddings();

        // then
        assertThat(embeddings[1].getMimeType()).isEqualTo("text/plain");
    }
}
