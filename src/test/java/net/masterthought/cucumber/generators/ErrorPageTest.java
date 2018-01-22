package net.masterthought.cucumber.generators;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.velocity.VelocityContext;
import org.junit.Before;
import org.junit.Test;

import net.masterthought.cucumber.generators.integrations.PageTest;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class ErrorPageTest extends PageTest {

    @Before
    public void setUp() {
        setUpWithJson(SAMPLE_JSON);
    }

    @Test
    public void preparePageContext() {

        // give
        VelocityContext pageContext = new VelocityContext();

        Exception exception = new Exception();
        page = new ErrorPage(exception, jsonReports);

        // when
        page.preparePageContext(pageContext, configuration, null);

        // then
        assertThat(pageContext.getKeys()).hasSize(2);
        assertThat(pageContext.get("output_message")).isEqualTo(ExceptionUtils.getStackTrace(exception));
        assertThat(pageContext.get("json_files")).isEqualTo(jsonReports);
    }
}
