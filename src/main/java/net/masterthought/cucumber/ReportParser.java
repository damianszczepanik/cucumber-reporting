package net.masterthought.cucumber;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.MalformedJsonException;

import net.masterthought.cucumber.json.Feature;

public class ReportParser {

    public List<Feature> parseJsonResults(List<String> jsonReportFiles)
            throws IOException, JsonSyntaxException {
        List<Feature> featureResults = new ArrayList<>();
        Gson gson = new Gson();
        for (String jsonFile : jsonReportFiles) {
            if (FileUtils.sizeOf(new File(jsonFile)) > 0) {
                try {
                    try (FileReader reader = new FileReader(jsonFile)) {
                        Feature[] features = gson.fromJson(reader, Feature[].class);
                        setMetadata(features, jsonFile);

                        featureResults.addAll(Arrays.asList(features));
                    }
                } catch (JsonSyntaxException e) {
                    System.err.println("[ERROR] File " + jsonFile + " is not a valid json report:  " + e.getMessage());
                    if (e.getCause() instanceof MalformedJsonException) {
                        // malformed json will be handled otherwise silently skip invalid cucumber json report
                        throw e;
                    }
                }
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
