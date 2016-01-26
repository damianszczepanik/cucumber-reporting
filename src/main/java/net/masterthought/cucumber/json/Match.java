package net.masterthought.cucumber.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.commons.lang3.StringUtils;

import java.util.LinkedList;
import java.util.List;

public class Match {

    // Start: attributes from JSON file report
    private final JsonElement location = null;
    // End: attributes from JSON file report

    public String getLocation() {
        if (location == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        JsonObject locationObject;

        if (location.isJsonPrimitive() && location.getAsJsonPrimitive().isString()) {
            return location.getAsString();
        } else {
            locationObject = location.getAsJsonObject();
            String filename;
            if (locationObject.has("file") && locationObject.get("file").getAsJsonPrimitive().isString()) {
                filename = locationObject.get("file").getAsString();
            } else if (locationObject.has("filepath") && locationObject.get("filepath").isJsonObject()) {
                JsonObject filePath = locationObject.get("filepath").getAsJsonObject();
                if (filePath.has("filename") && filePath.get("filename").getAsJsonPrimitive().isString()) {
                    filename = filePath.get("filename").getAsString();
                } else {
                    return null;
                }
            } else {
                return null;
            }

            sb.append(filename).append(":");

            LinkedList<Integer> lineList = new LinkedList<>();
            if (locationObject.has("lines") && locationObject.get("lines").isJsonObject()) {
                JsonObject lines = locationObject.get("lines").getAsJsonObject();
                if (lines.has("data") && lines.get("data").isJsonArray()) {
                    JsonArray data = lines.get("data").getAsJsonArray();
                    for (JsonElement lineElement : data) {
                        try {
                            int line = Integer.parseInt(lineElement.getAsString());
                            lineList.add(line);
                        } catch (NumberFormatException e) {
                            return null;
                        }
                    }
                } else {
                    return null;
                }
            } else {
                return null;
            }

            if (lineList.size() > 1 && areConsecutive(lineList)) {
                sb.append(lineList.getFirst()).append("..").append(lineList.getLast());
            } else {
                sb.append(StringUtils.join(lineList, ','));
            }
            return sb.toString();
        }
    }

    private boolean areConsecutive(List<Integer> lines) {
        int firstLine = lines.get(0);
        for (int i = 1; i < lines.size(); i++) {
            if (!(lines.get(i).equals(firstLine + i))) {
                return false;
            }
        }
        return true;
    }
}
