package net.masterthought.cucumber;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.masterthought.cucumber.json.Feature;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class ReportParser {

    private static final Logger LOG = LogManager.getLogger(ReportParser.class);

    private final ObjectMapper mapper = new ObjectMapper();
    private final Configuration configuration;

    public ReportParser(Configuration configuration) {
        this.configuration = configuration;

        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // this prevents printing eg. 2.20 as 2.2
        mapper.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
        // pass configuration to deserializers
        InjectableValues values = new InjectableValues.Std().addValue(Configuration.class, configuration);
        mapper.setInjectableValues(values);
    }

    /**
     * Parsed passed files and extracts features files.
     * 
     * @param jsonReportFiles
     *            JSON files to read
     * @return array of parsed features
     */
    public List<Feature> parseJsonResults(List<String> jsonReportFiles) {
        List<Feature> featureResults = new ArrayList<>();

        for (int i = 0; i < jsonReportFiles.size(); i++) {
            String jsonFile = jsonReportFiles.get(i);
            Feature[] features = parseForFeature(jsonFile);
            if (ArrayUtils.isEmpty(features)) {
                LOG.info(String.format("File '%s' does not contain features", jsonFile));
            } else {
                setMetadata(features, jsonFile, i);
                featureResults.addAll(Arrays.asList(features));
            }
        }

        return featureResults;
    }

    /**
     * Reads passed file and returns parsed features.
     * 
     * @param jsonFile
     *            JSON file that should be read
     * @return array of parsed features
     */
    private Feature[] parseForFeature(String jsonFile) {
        try (Reader reader = new InputStreamReader(new FileInputStream(jsonFile), StandardCharsets.UTF_8)) {
            return mapper.readValue(reader, Feature[].class);
        } catch (JsonMappingException e) {
            LOG.info(String.format("File '%s' is not proper Cucumber-JVM report", jsonFile), e);
            return new Feature[0];
        } catch (IOException e) {
            // IO problem - stop generating and re-throw the problem
            throw new ValidationException(e);
        }
    }

    /** Sets additional information and calculates values which should be calculated during object creation. */
    private void setMetadata(Feature[] features, String jsonFile, int jsonFileNo) {
        for (Feature feature : features) {
            feature.setMetaData(jsonFile, jsonFileNo, configuration);
        }
    }
}
