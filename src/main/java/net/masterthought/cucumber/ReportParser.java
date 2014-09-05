package net.masterthought.cucumber;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import net.masterthought.cucumber.json.Element;
import net.masterthought.cucumber.json.Feature;
import net.masterthought.cucumber.util.Util;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ReportParser {

    private Map<String, List<Feature>> jsonReportFiles;

    public ReportParser(List<String> jsonReportFiles) throws IOException {
        this.jsonReportFiles = parseJsonResults(jsonReportFiles);
    }

    public Map<String, List<Feature>> getFeatures() {
        return jsonReportFiles;
    }

    private Map<String, List<Feature>> parseJsonResults(List<String> jsonReportFiles) throws IOException {
    	GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Element[].class, new ElementDeserializer());
        Gson gson2 = builder.create();
    	
        Map<String, List<Feature>> featureResults = new LinkedHashMap<String, List<Feature>>();
        for (String jsonFile : jsonReportFiles) {
            String fileContent = Util.readFileAsString(jsonFile);
            if (Util.isValidCucumberJsonReport(fileContent)) {
            	Feature[] features =gson2.fromJson(Util.U2U(fileContent), Feature[].class); 
                featureResults.put(jsonFile, Arrays.asList(features));
            }
        }

        return featureResults;
    }
}

class ElementDeserializer implements JsonDeserializer<Element[]> {
    public Element[] deserialize(JsonElement json, Type typeOfT,
            JsonDeserializationContext context) throws JsonParseException {

        if (json == null)
            return null;
        List<Element> al = new ArrayList<Element>();
        for (JsonElement e : json.getAsJsonArray()) {

            boolean deserialize = !e.getAsJsonObject().get("type")
                    .getAsString().equals("scenario_outline");
            if (deserialize)
                al.add((Element) context.deserialize(e, Element.class));
        }

        return al.toArray(new Element[al.size()]);
    }
}