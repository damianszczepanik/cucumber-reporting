package net.masterthought.cucumber;

import static org.apache.commons.collections.CollectionUtils.isNotEmpty;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.StringUtils;
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
     * @param jsonFiles
     *            JSON files to read
     * @return array of parsed features
     */
    public List<Feature> parseJsonFiles(List<String> jsonFiles) {
        if (jsonFiles.isEmpty()) {
            throw new ValidationException("No report file was added!");
        }

        List<Feature> featureResults = new ArrayList<>(jsonFiles.size() * 10);
        int jsonFileNo = 0;
        
        for (String jsonFile : jsonFiles) {
        	Feature[] features = parseForFeature(jsonFile);
        	LOG.info("File '{}' contain {} features", jsonFile, features.length);
        	collectFeaturesAndSetMetadata(featureResults, features, jsonFile, jsonFileNo++);
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
    
    
    /** 
     * Collects features, sets additional information and calculates values which should be calculated during object creation. 
     */
    private void collectFeaturesAndSetMetadata(Collection<Feature> featuresAccumulator, Feature[] features, String jsonFile, int jsonFileNo) {
    	for (Feature feature : features) {
            feature.setMetaData(jsonFile, jsonFileNo, configuration);
            featuresAccumulator.add(feature);
        }
    }

    /**
     * Parses passed properties files for classifications. These classifications within each file get added to the overview-features page as metadata.
     * File and metadata order within the individual files are preserved when classifications are added.
     *
     * @param propertiesFiles
     *            property files to read
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
            
            @SuppressWarnings("unchecked")
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
