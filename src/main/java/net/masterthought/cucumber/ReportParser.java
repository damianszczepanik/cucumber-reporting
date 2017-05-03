package net.masterthought.cucumber;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
     * @param jsonFiles
     *            JSON files to read
     * @return array of parsed features
     */
    public List<Feature> parseJsonFiles(List<String> jsonFiles) {
        if (jsonFiles.isEmpty()) {
            throw new ValidationException("None report file was added!");
        }

        List<Feature> featureResults = new ArrayList<>();
        for (int i = 0; i < jsonFiles.size(); i++) {
            String jsonFile = jsonFiles.get(i);
            Feature[] features = parseForFeature(jsonFile);
            LOG.info("File '{}' contain {} features", jsonFile, features.length);
            setMetadata(features, jsonFile, i);
            featureResults.addAll(Arrays.asList(features));
        }

        // report that has no features seems to be not valid
        if (featureResults.isEmpty()) {
            throw new ValidationException("Passed files have no features!");
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
            Feature[] features = mapper.readValue(reader, Feature[].class);
            if (ArrayUtils.isEmpty(features)) {
                LOG.info("File '{}' does not contain features", jsonFile);
            }
            return features;
        } catch (JsonMappingException e) {
            throw new ValidationException(String.format("File '%s' is not proper Cucumber report!", jsonFile), e);
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
