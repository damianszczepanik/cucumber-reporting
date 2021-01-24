package net.masterthought.cucumber;

import static org.apache.commons.collections.CollectionUtils.isNotEmpty;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import net.masterthought.cucumber.json.Feature;
import net.masterthought.cucumber.reducers.ReducingMethod;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ArrayUtils;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class ReportParser {

    private static final Logger LOG = Logger.getLogger(ReportParser.class.getName());

    private final ObjectMapper mapper = new ObjectMapper();
    private final Configuration configuration;

    public ReportParser(Configuration configuration) {
        this.configuration = configuration;

        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // this prevents printing eg. 2.20 as 2.2
        mapper.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
        mapper.registerModule(new JavaTimeModule());

        // pass configuration to deserializers
        InjectableValues values = new InjectableValues.Std().addValue(Configuration.class, configuration);
        mapper.setInjectableValues(values);
    }

    /**
     * Parsed passed files and extracts features files.
     *
     * @param jsonFiles JSON files to read
     * @return array of parsed features
     */
    public List<Feature> parseJsonFiles(List<String> jsonFiles) {
        if (jsonFiles.isEmpty()) {
            throw new ValidationException("None report file was added!");
        }

        List<Feature> featureResults = new ArrayList<>();
        for (int i = 0; i < jsonFiles.size(); i++) {
            String jsonFile = jsonFiles.get(i);
            // if file is empty (is not valid JSON report), check if should be skipped or not
            if (new File(jsonFile).length() == 0
                    && configuration.containsReducingMethod(ReducingMethod.SKIP_EMPTY_JSON_FILES)) {
                continue;
            }
            Feature[] features = parseForFeature(jsonFile);
            LOG.log(Level.INFO, () -> String.format("File '%s' contains %d features", jsonFile, features.length));
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
     * @param jsonFile JSON file that should be read
     * @return array of parsed features
     */
    private Feature[] parseForFeature(String jsonFile) {
        try (Reader reader = new InputStreamReader(new FileInputStream(jsonFile), StandardCharsets.UTF_8)) {
            Feature[] features = mapper.readValue(reader, Feature[].class);
            if (ArrayUtils.isEmpty(features)) {
                LOG.log(Level.INFO, () -> String.format("File '%s' does not contain features", jsonFile));
            }
            String jsonFileName = extractQualifier(jsonFile);
            Arrays.stream(features).forEach(feature ->
                    feature.setQualifier(StringUtils.defaultString(configuration.getQualifier(jsonFileName), jsonFileName))
            );

            return features;
        } catch (JsonMappingException e) {
            throw new ValidationException(String.format("File '%s' is not proper Cucumber report!", jsonFile), e);
        } catch (IOException e) {
            // IO problem - stop generating and re-throw the problem
            throw new ValidationException(e);
        }
    }

    private String extractQualifier(String jsonFileName) {
        File jsonFile = new File(jsonFileName);
        String target = jsonFile.getName();

        final String jsonExtension = ".json";
        if (target.toLowerCase().endsWith(jsonExtension)) {
            return target.substring(0, target.length() - jsonExtension.length());
        }
        return target;
    }

    /**
     * Parses passed properties files for classifications. These classifications within each file get added to the overview-features page as metadata.
     * File and metadata order within the individual files are preserved when classifications are added.
     *
     * @param propertiesFiles property files to read
     */
    public void parseClassificationsFiles(List<String> propertiesFiles) {
        if (isNotEmpty(propertiesFiles)) {
            for (String propertyFile : propertiesFiles) {
                if (StringUtils.isNotEmpty(propertyFile)) {
                    processClassificationFile(propertyFile);
                }
            }
        }
    }

    private void processClassificationFile(String file) {
        try {
            PropertiesConfiguration config = new PropertiesConfiguration(file);
            Iterator<String> keys = config.getKeys();
            while (keys.hasNext()) {
                String key = keys.next();
                String value = config.getProperty(key).toString();
                this.configuration.addClassifications(key, value);
            }
        } catch (ConfigurationException e) {
            throw new ValidationException(String.format("File '%s' doesn't exist or the properties file is invalid!", file), e);
        }
    }

}
