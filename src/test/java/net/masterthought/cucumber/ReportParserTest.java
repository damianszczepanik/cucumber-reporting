package net.masterthought.cucumber;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.StringContains.containsString;

import java.io.IOException;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import net.masterthought.cucumber.json.Feature;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class ReportParserTest extends ReportGenerator {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void parseJsonResults_ReturnsParsedFeatureFiles() throws IOException {

        // given
        setUpWithJson(SAMPLE_JSON, SIMPLE_JSON);

        // when
        List<Feature> features = new ReportParser(configuration).parseJsonFiles(jsonReports);

        // then
        assertThat(features).hasSize(3);
    }

    @Test
    public void parseJsonResults_OnInvalidJSON_SkipFiles() throws IOException {

        // given
        setUpWithJson(INVALID_JSON, SIMPLE_JSON);

        // when
        List<Feature> features = new ReportParser(configuration).parseJsonFiles(jsonReports);

        // then
        assertThat(features).hasSize(1);
    }

    @Test
    public void parseJsonResults_OnEmptyJSON_SkipFiles() throws IOException {

        // given
        setUpWithJson(EMPTY_JSON);

        // when
        List<Feature> features = new ReportParser(configuration).parseJsonFiles(jsonReports);

        // then
        assertThat(features).isEmpty();
    }

    @Test
    public void parseJsonResults_OnInvalidReport_SkipsFiles() throws IOException {

        // given
        setUpWithJson(INVALID_REPORT_JSON, SIMPLE_JSON);

        // when
        List<Feature> features = new ReportParser(configuration).parseJsonFiles(jsonReports);

        // then
        assertThat(features).hasSize(1);
    }

    @Test
    public void parseJsonResultsFails_OnNoExistingFile_ThrowsException() {

        // given
        final String invalidFile = "?no-existing%file.json";
        setUpWithJson(EMPTY_JSON);
        jsonReports.add(invalidFile);

        // then
        thrown.expect(ValidationException.class);
        thrown.expectMessage(containsString(invalidFile));
        new ReportParser(configuration).parseJsonFiles(jsonReports);
    }
}
