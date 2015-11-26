package net.masterthought.cucumber;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import net.masterthought.cucumber.json.Feature;

public class ReportParser {

    public List<Feature> parseJsonResults(List<String> jsonReportFiles) throws IOException, JsonSyntaxException {
        List<Feature> featureResults = new ArrayList<>();
        Gson gson = new Gson();

        for (String jsonFile : jsonReportFiles) {
            try (InputStream in = new FileInputStream(jsonFile);
                    Reader reader = new InputStreamReader(in, StandardCharsets.UTF_8)) {
                Feature[] features = gson.fromJson(reader, Feature[].class);
                if (features == null) {
                    throw new IllegalArgumentException(String.format("File '%s' does not contan features!", jsonFile));
                }
                setMetadata(features, jsonFile);

                featureResults.addAll(Arrays.asList(features));
            }
        }

        return featureResults;
    }

    /** Sets additional information and calculates values which should be calculated during object creation. */
    private void setMetadata(Feature[] features, String jsonFile) {
        for (Feature feature : features) {
            feature.setMetaData(jsonFile);
        }
    }
}
