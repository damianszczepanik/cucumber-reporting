package net.masterthought.cucumber;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.data.MapEntry.entry;
import static org.hamcrest.core.StringContains.containsString;
import static org.hamcrest.core.StringEndsWith.endsWith;

import java.util.List;
import java.util.Map;

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

    @Test
    public void parsePropertyFiles_EmptyProperties() {

        // given
        initWithProperties(EMPTY_PROPERTIES);
        ReportParser reportParser = new ReportParser(configuration);

        // when
        reportParser.parsePropertiesFiles(propertyReports);

        // then
        assertThat(configuration.getClassifications()).isEmpty();
    }

    @Test
    public void parsePropertyFiles_Populate_One_File() {

        // given
        initWithProperties(SAMPLE_ONE_PROPERTIES);
        ReportParser reportParser = new ReportParser(configuration);

        // when
        reportParser.parsePropertiesFiles(propertyReports);

        // then
        assertThat(configuration.getClassifications()).hasSize(5);
    }

    @Test
    public void parsePropertyFiles_Populate_Two_Files() {

        // given
        initWithProperties(SAMPLE_ONE_PROPERTIES, SAMPLE_TWO_PROPERTIES);
        ReportParser reportParser = new ReportParser(configuration);

        // when
        reportParser.parsePropertiesFiles(propertyReports);

        // then
        assertThat(configuration.getClassifications()).hasSize(8);
    }

    @Test
    public void parsePropertyFiles_Populate_Check_Contents_Integrity() {

        // given
        initWithProperties(SAMPLE_TWO_PROPERTIES);
        ReportParser reportParser = new ReportParser(configuration);

        // when
        reportParser.parsePropertiesFiles(propertyReports);

        List<Map.Entry<String, String>> classifications = configuration.getClassifications();

        // then
        assertThat(classifications).hasSize(3);
        assertThat(classifications).containsExactly(entry("NodeJsVersion","8.5.0"),entry("Proxy", "http=//172.22.240.68:18717"),entry("NpmVersion", "5.3.0"));
    }

    @Test
    public void parsePropertyFiles_Populate_Check_Contents_Order() {

        // given
        initWithProperties(SAMPLE_TWO_PROPERTIES);
        ReportParser reportParser = new ReportParser(configuration);

        // when
        reportParser.parsePropertiesFiles(propertyReports);

        List<Map.Entry<String, String>> classifications = configuration.getClassifications();

        // then
        assertThat(classifications).hasSize(3);
        assertThat(classifications.get(0).getKey()).isEqualTo("NodeJsVersion");
        assertThat(classifications.get(1).getKey()).isEqualTo("Proxy");
        assertThat(classifications.get(2).getKey()).isEqualTo("NpmVersion");

    }

    @Test
    public void parsePropertyFiles_Populate_Check_Duplicates() {

        // given
        initWithProperties(DUPLICATE_PROPERTIES);
        ReportParser reportParser = new ReportParser(configuration);

        // when
        reportParser.parsePropertiesFiles(propertyReports);

        List<Map.Entry<String, String>> classifications = configuration.getClassifications();

        // then
        assertThat(classifications).hasSize(1);
        assertThat(classifications.get(0).getKey()).isEqualTo("BaseUrl_QA");
        assertThat(classifications.get(0).getValue()).isEqualTo("[Internal=https://internal.test.com, External=https://external.test.com]");

    }

    @Test
    public void parsePropertyFiles_Populate_Check_Special_Characters() {

        // given
        initWithProperties(SPECIAL_CHARACTERS_PROPERTIES);
        ReportParser reportParser = new ReportParser(configuration);

        // when
        reportParser.parsePropertiesFiles(propertyReports);

        List<Map.Entry<String, String>> classifications = configuration.getClassifications();

        // then
        assertThat(classifications).hasSize(6);
        assertThat(classifications).containsExactly(
                entry("website","https://en.wikipedia.org/"),
                entry("language", "English"),
                entry("message", "Welcome to Wikipedia!"),
                entry("key with spaces", "This is the value that could be looked up with the key \"key with spaces\"."),
                entry("tab", "\t"),
                entry("path", "c:\\wiki\\templates")
        );

    }

    @Test
    public void parsePropertyFiles_OnInvalidFilePath_ThrowsException() {

        // given
        final String invalidFile = "?on-invalid-file-path.properties";
        initWithProperties(EMPTY_PROPERTIES);
        propertyReports.add(invalidFile);
        ReportParser reportParser = new ReportParser(configuration);

        // then
        thrown.expect(ValidationException.class);
        thrown.expectMessage(containsString(invalidFile));
        thrown.expectMessage(endsWith("doesn't exist or the properties file is invalid!"));

        reportParser.parsePropertiesFiles(propertyReports);
    }

}
