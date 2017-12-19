package net.masterthought.cucumber.generators;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.apache.velocity.VelocityContext;
import org.junit.Before;
import org.junit.Test;

import net.masterthought.cucumber.generators.integrations.PageTest;
import net.masterthought.cucumber.json.Element;
import net.masterthought.cucumber.json.Feature;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class FailuresOverviewPageTest extends PageTest {

    @Before
    public void setUp() {
        setUpWithJson(SAMPLE_JSON);
    }

    @Test
    public void getWebPage_ReturnsFailureReportFileName() {

        // given
        page = new FailuresOverviewPage();

        // when
        String fileName = page.getWebPage();

        // then
        assertThat(fileName).isEqualTo(FailuresOverviewPage.WEB_PAGE);
    }

    @Test
    public void prepareReport_AddsCustomProperties() {

        // given
    	VelocityContext context = new VelocityContext();
    	
        page = new FailuresOverviewPage();
        // this page only has failed scenarios (elements) so extract them into
        // a list to compare
        List<Element> failures = new ArrayList<>();
        for (Feature feature : features) {
            if (feature.getStatus().isPassed()) {
                continue;
            }

            for (Element element : feature.getElements()) {
                if (element.getStepsStatus().isPassed())
                    continue;

                failures.add(element);
            }
        }

        // when
        page.preparePageContext(context, configuration, reportResult);

        // then
        assertThat(context.getKeys()).hasSize(1);

        @SuppressWarnings("unchecked")
		List<Element> elements = (List<Element>) context.get("failures");
        assertThat(elements).hasSameElementsAs(failures);
    }
}
