package net.masterthought.cucumber;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.masterthought.cucumber.json.Feature;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.MalformedJsonException;

public class ReportParser {

    private final Map<String, List<Feature>> featureFiles;

    public ReportParser(List<String> jsonReportFiles) throws IOException, JsonSyntaxException {
        this.featureFiles = parseJsonResults(jsonReportFiles);
    }

    public Map<String, List<Feature>> getFeatures() {
        return featureFiles;
    }

    private Map<String, List<Feature>> parseJsonResults(List<String> jsonReportFiles) throws IOException,
            JsonSyntaxException {
        Map<String, List<Feature>> featureResults = new LinkedHashMap<>();
        Gson gson = new Gson();
        for (String jsonFile : jsonReportFiles) {
            if (FileUtils.sizeOf(new File(jsonFile)) > 0) {
                try {
                    Feature[] features = gson.fromJson(new FileReader(jsonFile), Feature[].class);
                    featureResults.put(jsonFile, Arrays.asList(features));
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
}
