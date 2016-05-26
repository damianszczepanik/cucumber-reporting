package net.masterthought.cucumber;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.Charsets;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import net.masterthought.cucumber.json.Feature;

public class ReportParser {

    private static final Logger LOG = LogManager.getLogger(ReportParser.class);

    private final Gson gson = new Gson();
    private final Configuration configuration;

    public ReportParser(Configuration configuration){
        this.configuration = configuration;
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
                LOG.warn(String.format("File '%s' does not contain features", jsonFile));
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
        try (Reader reader = new InputStreamReader(new FileInputStream(jsonFile), Charsets.UTF_8)) {
            return gson.fromJson(reader, Feature[].class);
        } catch (JsonSyntaxException e) {
            // invalid JSON files are accepted, should be skipped
            LOG.info("File '{}' could not be parsed properly", jsonFile, e);
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
