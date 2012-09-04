package net.masterthought.cucumber;

import net.masterthought.cucumber.json.Artifact;

import java.util.HashMap;
import java.util.Map;

public class ArtifactProcessor {

    private String configuration;

    public ArtifactProcessor(String configuration) {
        this.configuration = configuration;
    }

    public Map<String, Artifact> process() throws Exception {
        Map<String, Artifact> map = new HashMap<String, Artifact>();
        String[] lines = configuration.split("\\n");
        for (String line : lines) {
            String[] data = line.split("~");
            if (data.length == 5) {
                String scenario = data[0].trim();
                String step = data[1].trim();
                String keyword = data[2].trim();
                String artifactFile = data[3].trim();
                String contentType = data[4].trim();
                map.put(scenario+step, new Artifact(scenario, step, keyword, artifactFile, contentType));
            } else {
                throw new Exception("Error configuration should have 5 parts: Scenario ~ Step ~ Keyword ~ Artifact File ~ ContentType");
            }
        }
        return map;
    }

}
