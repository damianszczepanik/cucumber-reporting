package net.masterthought.cucumber.generators;

import java.util.List;

import net.masterthought.cucumber.generators.integrations.PageTest;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.velocity.VelocityContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
class ErrorPageTest extends PageTest {

    @BeforeEach
    void setUp() {
        setUpWithJson(SAMPLE_JSON);
    }

    @Test
    void prepareReport_AddsCustomProperties() {

        // give
        Exception exception = new Exception();
        page = new ErrorPage(null, configuration, exception, jsonReports);

        // when
        page.prepareReport();

        // then
        VelocityContext context = page.context;
        assertThat(context.getKeys()).hasSize(16);
        assertThat(context.get("classifications")).isInstanceOf(List.class);
        assertThat(context.get("output_message")).isEqualTo(ExceptionUtils.getStackTrace(exception));
        assertThat(context.get("json_files")).isEqualTo(jsonReports);
        assertThat(context.get("directory_suffix")).isEqualTo(configuration.getDirectorySuffixWithSeparator());
    }
}
