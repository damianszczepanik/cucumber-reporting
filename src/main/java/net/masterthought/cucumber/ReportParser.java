package net.masterthought.cucumber;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonFactoryBuilder;
import com.fasterxml.jackson.core.StreamReadConstraints;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import net.masterthought.cucumber.json.Feature;
import net.masterthought.cucumber.reducers.ReducingMethod;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Takes <code>json</code> files and converts them into objects used for report generation.
 *
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class ReportParser {

    private static final Logger LOG = LoggerFactory.getLogger(ReportParser.class);

    private final ObjectMapper mapper;
    private final Configuration configuration;

    public ReportParser(Configuration configuration) {
        this.configuration = configuration;

        StreamReadConstraints streamReadConstraints = StreamReadConstraints.builder()
                .maxStringLength(configuration.getMaxStreamStringLength()).build();
        JsonFactory factoryBuilder = new JsonFactoryBuilder()
                .streamReadConstraints(streamReadConstraints).build();

        mapper = new ObjectMapper(factoryBuilder);
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
            throw new ValidationException("No JSON report file was found!");
        }

        List<Feature> featureResults = new ArrayList<>();
        for (String jsonFile : jsonFiles) {
            // if file is empty (is not valid JSON report), check if should be skipped or not
            if (new File(jsonFile).length() == 0
                    && configuration.containsReducingMethod(ReducingMethod.SKIP_EMPTY_JSON_FILES)) {
                continue;
            }
            Feature[] features = parseForFeature(jsonFile);
            LOG.info("File '{}' contains {} feature(s)", jsonFile, features.length);
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
                LOG.info("File '{}' does not contain features", jsonFile);
            }
            String jsonFileName = extractQualifier(jsonFile);
            Arrays.stream(features).forEach(feature ->
                    feature.setQualifier(Objects.toString(configuration.getQualifier(jsonFileName), jsonFileName))
            );

            return features;
        } catch (JsonMappingException e) {
            throw new ValidationException(
                    String.format("File '%s' is not a valid Cucumber report! %s", jsonFile, e.getMessage()), e.getCause());
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
        if (propertiesFiles != null && !propertiesFiles.isEmpty()) {
            for (String propertyFile : propertiesFiles) {
                if (StringUtils.isNotEmpty(propertyFile)) {
                    processClassificationFile(propertyFile);
                }
            }
        }
    }

    private void processClassificationFile(String file) {
        FileBasedConfigurationBuilder<PropertiesConfiguration> builder = new FileBasedConfigurationBuilder<>(PropertiesConfiguration.class)
                .configure(new Parameters().properties().setFile(new File(file))
                        .setThrowExceptionOnMissing(true));
        try {
            PropertiesConfiguration config = builder.getConfiguration();

            config.getKeys().forEachRemaining(key -> {
                if (config.getStringArray(key).length > 1) {
                    // Duplicate keys
                    this.configuration.addClassifications(key, Arrays.toString(config.getStringArray(key)));
                } else {
                    this.configuration.addClassifications(key, config.getString(key));
                }
            });
        } catch (ConfigurationException e) {
            throw new ValidationException(String.format("File '%s' doesn't exist or the properties file is invalid!", file), e);
        }
    }

}
