package net.masterthought.cucumber;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.StringContains.containsString;
import static org.hamcrest.core.StringEndsWith.endsWith;

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
    public void parseJsonResults_ReturnsFeatureFiles() {

        // given
        initWithJSon(SAMPLE_JSON, SIMPLE_JSON);
        ReportParser reportParser = new ReportParser(configuration);

        // when
        List<Feature> features = reportParser.parseJsonFiles(jsonReports);

        // then
        assertThat(features).hasSize(3);
    }

    @Test
    public void parseJsonResults_OnNoFeatures_ThrowsException() {

        // given
        initWithJSon(EMPTY_JSON);
        ReportParser reportParser = new ReportParser(configuration);

        // when
        thrown.expect(ValidationException.class);
        thrown.expectMessage("Passed files have no features!");
        reportParser.parseJsonFiles(jsonReports);
    }

    @Test
    public void parseJsonResults_OnNoReport_ThrowsException() {

        // given
        initWithJSon();
        ReportParser reportParser = new ReportParser(configuration);

        // then
        thrown.expect(ValidationException.class);
        thrown.expectMessage("None report file was added!");
        reportParser.parseJsonFiles(jsonReports);
    }

    @Test
    public void parseJsonResults_OnInvalidReport_ThrowsException() {

        // given
        initWithJSon(INVALID_REPORT_JSON);
        ReportParser reportParser = new ReportParser(configuration);

        // when
        thrown.expect(ValidationException.class);
        thrown.expectMessage(endsWith("is not proper Cucumber report!"));
        reportParser.parseJsonFiles(jsonReports);
    }

    @Test
    public void parseJsonResults_OnNoExistingFile_ThrowsException() {

        // given
        final String invalidFile = "?no-existing%file.json";
        initWithJSon(EMPTY_JSON);
        jsonReports.add(invalidFile);
        ReportParser reportParser = new ReportParser(configuration);

        // then
        thrown.expect(ValidationException.class);
        thrown.expectMessage(containsString(invalidFile));

        reportParser.parseJsonFiles(jsonReports);
    }
}
