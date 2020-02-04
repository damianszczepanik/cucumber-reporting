package net.masterthought.cucumber;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.data.MapEntry.entry;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;

import mockit.Deencapsulation;
import net.masterthought.cucumber.json.Element;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.data.Index;
import org.junit.Test;

import net.masterthought.cucumber.json.Feature;
import net.masterthought.cucumber.reducers.ReducingMethod;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class ReportParserTest extends ReportGenerator {

    @Test
    public void parseJsonFiles_ReturnsFeatureFiles() {

        // given
        initWithJson(SAMPLE_JSON, SIMPLE_JSON);
        ReportParser reportParser = new ReportParser(configuration);

        // when
        List<Feature> features = reportParser.parseJsonFiles(jsonReports);

        // then
        assertThat(features).hasSize(3);
    }

    @Test
    public void parseJsonFiles_Timestamp() {
        // given
        initWithJson(CUCUMBER_TIMESTAMPED_JSON);
        ReportParser reportParser = new ReportParser(configuration);

        // when
        List<Feature> features = reportParser.parseJsonFiles(jsonReports);

        // then
        assertThat(features).hasSize(2);

        SoftAssertions.assertSoftly(a -> {
            for (Feature f : features) {
                for (Element elm : f.getElements()) {
                    if (elm.isScenario()) {
                        a.assertThat(elm.getStartTime()).isNotNull();
                    }
                }
            }
        });
    }

    @Test
    public void parseJsonFiles_OnNoFeatures_ThrowsException() {

        // given
        initWithJson(EMPTY_JSON);
        ReportParser reportParser = new ReportParser(configuration);

        // when & then
        assertThatThrownBy(() -> reportParser.parseJsonFiles(jsonReports))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Passed files have no features!");
    }

    @Test
    public void parseJsonFiles_OnNoReport_ThrowsException() {
        // given
        initWithJson();
        ReportParser reportParser = new ReportParser(configuration);

        // when & then
        assertThatThrownBy(() -> reportParser.parseJsonFiles(jsonReports))
                .isInstanceOf(ValidationException.class)
                .hasMessage("None report file was added!");
    }

    @Test
    public void parseJsonFiles_OnInvalidReport_ThrowsException() {

        // given
        initWithJson(INVALID_REPORT_JSON);
        ReportParser reportParser = new ReportParser(configuration);

        // when & then
        assertThatThrownBy(() -> reportParser.parseJsonFiles(jsonReports))
                .isInstanceOf(ValidationException.class)
                .hasMessageEndingWith("is not proper Cucumber report!");
    }

    @Test
    public void parseJsonFiles_OnNoExistingFile_ThrowsException() {

        // given
        final String invalidFile = "?no-existing%file.json";
        initWithJson(EMPTY_JSON);
        jsonReports.add(invalidFile);
        ReportParser reportParser = new ReportParser(configuration);

        // when & then
        assertThatThrownBy(() -> reportParser.parseJsonFiles(jsonReports))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining(invalidFile);
    }

    @Test
    public void parseJsonFiles_OnEmptyFile_SkipsJSONReport() {

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
    public void extractTarget_ReturnsFileNameWithoutExtension() {

        // given
        String jsonFile = SAMPLE_JSON;
        initWithJson(SAMPLE_JSON);
        ReportParser reportParser = new ReportParser(configuration);

        // when
        String qualifier = Deencapsulation.invoke(reportParser, "extractQualifier", jsonFile);


        // then
        assertThat(qualifier).isEqualTo(jsonFile.substring(0, jsonFile.length() - 5));
    }

    @Test
    public void extractTarget_OnNoJSONFile_ReturnsFileName() {

        // given
        String jsonFile = SAMPLE_JSON + ".txt";
        initWithJson(SAMPLE_JSON);
        ReportParser reportParser = new ReportParser(configuration);

        // when
        String qualifier = Deencapsulation.invoke(reportParser, "extractQualifier", jsonFile);


        // then
        assertThat(qualifier).isEqualTo(jsonFile);
    }

    @Test
    public void parseClassificationsFiles_EmptyProperties() {

        // given
        initWithProperties(EMPTY_PROPERTIES);
        ReportParser reportParser = new ReportParser(configuration);

        // when
        reportParser.parseClassificationsFiles(classificationFiles);

        // then
        assertThat(configuration.getClassifications()).isEmpty();
    }

    @Test
    public void parseClassificationsFiles_Populates_One_File() {

        // given
        initWithProperties(SAMPLE_ONE_PROPERTIES);
        ReportParser reportParser = new ReportParser(configuration);

        // when
        reportParser.parseClassificationsFiles(classificationFiles);

        // then
        assertThat(configuration.getClassifications()).hasSize(5);
    }

    @Test
    public void parseClassificationsFiles_Populates_Two_Files() {

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
    public void parseClassificationsFiles_Populates_Two_Files_One_Empty() {

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
    public void parseClassificationsFiles_Populates_Check_Content_Integrity_And_Order() {

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
    public void parseClassificationsFiles_Populates_Check_Duplicates() {

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
    public void parseClassificationsFiles_Populates_Check_Special_Characters() {

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
    public void parseClassificationsFiles_OnInvalidFilePath_ThrowsException() {

        // given
        final String invalidFile = "?on-invalid-file-path.properties";
        initWithProperties(EMPTY_PROPERTIES);
        classificationFiles.add(invalidFile);
        ReportParser reportParser = new ReportParser(configuration);

        // when & then
        assertThatThrownBy(() -> reportParser.parseClassificationsFiles(classificationFiles))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining(invalidFile)
                .hasMessageEndingWith("doesn't exist or the properties file is invalid!");
    }

}
