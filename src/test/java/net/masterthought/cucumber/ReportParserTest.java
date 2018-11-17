package net.masterthought.cucumber;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.data.MapEntry.entry;
import static org.hamcrest.core.StringContains.containsString;
import static org.hamcrest.core.StringEndsWith.endsWith;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;

import org.assertj.core.data.Index;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import net.masterthought.cucumber.json.Feature;
import net.masterthought.cucumber.reducers.ReducingMethod;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class ReportParserTest extends ReportGenerator {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void parseJsonResults_ReturnsFeatureFiles() {

        // given
        initWithJson(SAMPLE_JSON, SIMPLE_JSON);
        ReportParser reportParser = new ReportParser(configuration);

        // when
        List<Feature> features = reportParser.parseJsonFiles(jsonReports);

        // then
        assertThat(features).hasSize(3);
    }

    @Test
    public void parseJsonResults_OnNoFeatures_ThrowsException() {

        // given
        initWithJson(EMPTY_JSON);
        ReportParser reportParser = new ReportParser(configuration);

        // when & then
        thrown.expect(ValidationException.class);
        thrown.expectMessage("Passed files have no features!");

        reportParser.parseJsonFiles(jsonReports);
    }

    @Test
    public void parseJsonResults_OnNoReport_ThrowsException() {

        // given
        initWithJson();
        ReportParser reportParser = new ReportParser(configuration);

        // when & then
        thrown.expect(ValidationException.class);
        thrown.expectMessage("None report file was added!");

        reportParser.parseJsonFiles(jsonReports);
    }

    @Test
    public void parseJsonResults_OnInvalidReport_ThrowsException() {

        // given
        initWithJson(INVALID_REPORT_JSON);
        ReportParser reportParser = new ReportParser(configuration);

        // when & then
        thrown.expect(ValidationException.class);
        thrown.expectMessage(endsWith("is not proper Cucumber report!"));

        reportParser.parseJsonFiles(jsonReports);
    }

    @Test
    public void parseJsonResults_OnNoExistingFile_ThrowsException() {

        // given
        final String invalidFile = "?no-existing%file.json";
        initWithJson(EMPTY_JSON);
        jsonReports.add(invalidFile);
        ReportParser reportParser = new ReportParser(configuration);

        // when & then
        thrown.expect(ValidationException.class);
        thrown.expectMessage(containsString(invalidFile));

        reportParser.parseJsonFiles(jsonReports);
    }

    @Test
    public void parseJsonResults_OnEmptyFile_SkipsJSONReport() {

        // given
        initWithJson(EMPTY_FILE_JSON, SAMPLE_JSON);
        configuration.addReducingMethod(ReducingMethod.SKIP_EMPTY_JSON_FILES);
        ReportParser reportParser = new ReportParser(configuration);

        // when
        List<Feature> features = reportParser.parseJsonFiles(jsonReports);

        // then
        assertThat(features).hasSize(2);
    }

    @Test
    public void parsePropertyFiles_EmptyProperties() {

        // given
        initWithProperties(EMPTY_PROPERTIES);
        ReportParser reportParser = new ReportParser(configuration);

        // when
        reportParser.parseClassificationsFiles(classificationFiles);

        // then
        assertThat(configuration.getClassifications()).isEmpty();
    }

    @Test
    public void parsePropertyFiles_Populates_One_File() {

        // given
        initWithProperties(SAMPLE_ONE_PROPERTIES);
        ReportParser reportParser = new ReportParser(configuration);

        // when
        reportParser.parseClassificationsFiles(classificationFiles);

        // then
        assertThat(configuration.getClassifications()).hasSize(5);
    }

    @Test
    public void parsePropertyFiles_Populates_Two_Files() {

        // given
        initWithProperties(SAMPLE_ONE_PROPERTIES, SAMPLE_TWO_PROPERTIES);
        ReportParser reportParser = new ReportParser(configuration);

        // when
        reportParser.parseClassificationsFiles(classificationFiles);

        // then
        List<Map.Entry<String, String>> returnedClassifications = configuration.getClassifications();
        assertThat(returnedClassifications).hasSize(8);
        assertThat(returnedClassifications).contains(new AbstractMap.SimpleEntry<>("AutUiVersion", "1.25.3"), Index.atIndex(1));
        assertThat(returnedClassifications).contains(new AbstractMap.SimpleEntry<>("firefoxVersion", "56.0"), Index.atIndex(4));
        assertThat(returnedClassifications).contains(new AbstractMap.SimpleEntry<>("Proxy", "http=//172.22.240.68:18717"), Index.atIndex(6));
        assertThat(returnedClassifications).contains(new AbstractMap.SimpleEntry<>("NpmVersion", "5.3.0"), Index.atIndex(7));
    }

    @Test
    public void parsePropertyFiles_Populates_Two_Files_One_Empty() {

        // given
        initWithProperties(SAMPLE_ONE_PROPERTIES);
        ReportParser reportParser = new ReportParser(configuration);
        classificationFiles.add("");

        // when
        reportParser.parseClassificationsFiles(classificationFiles);

        // then
        List<Map.Entry<String, String>> returnedClassifications = configuration.getClassifications();
        assertThat(returnedClassifications).hasSize(5);
        assertThat(returnedClassifications).contains(new AbstractMap.SimpleEntry<>("AutUiVersion", "1.25.3"), Index.atIndex(1));
        assertThat(returnedClassifications).contains(new AbstractMap.SimpleEntry<>("firefoxVersion", "56.0"), Index.atIndex(4));
    }

    @Test
    public void parsePropertyFiles_Populates_Check_Content_Integrity_And_Order() {

        // given
        initWithProperties(SAMPLE_TWO_PROPERTIES);
        ReportParser reportParser = new ReportParser(configuration);

        // when
        reportParser.parseClassificationsFiles(classificationFiles);

        // then
        List<Map.Entry<String, String>> classifications = configuration.getClassifications();
        assertThat(classifications).hasSize(3);
        assertThat(classifications).containsExactly(
                entry("NodeJsVersion","8.5.0"),
                entry("Proxy", "http=//172.22.240.68:18717"),
                entry("NpmVersion", "5.3.0")
        );
    }

    @Test
    public void parsePropertyFiles_Populates_Check_Duplicates() {

        // given
        initWithProperties(DUPLICATE_PROPERTIES);
        ReportParser reportParser = new ReportParser(configuration);

        // when
        reportParser.parseClassificationsFiles(classificationFiles);

        // then
        List<Map.Entry<String, String>> classifications = configuration.getClassifications();
        assertThat(classifications).hasSize(1);
        assertThat(classifications).containsExactly(
                entry("BaseUrl_QA","[Internal=https://internal.test.com, External=https://external.test.com]")
        );
    }

    @Test
    public void parsePropertyFiles_Populates_Check_Special_Characters() {

        // given
        initWithProperties(SPECIAL_CHARACTERS_PROPERTIES);
        ReportParser reportParser = new ReportParser(configuration);

        // when
        reportParser.parseClassificationsFiles(classificationFiles);

        // then
        List<Map.Entry<String, String>> classifications = configuration.getClassifications();
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
        classificationFiles.add(invalidFile);
        ReportParser reportParser = new ReportParser(configuration);

        // then
        thrown.expect(ValidationException.class);
        thrown.expectMessage(containsString(invalidFile));
        thrown.expectMessage(endsWith("doesn't exist or the properties file is invalid!"));
        reportParser.parseClassificationsFiles(classificationFiles);
    }

}
