package net.masterthought.cucumber;

public class Artifact {

    private final String scenario;
    private final String step;
    private final String keyword;
    private final String artifactFile;
    private final String contentType;

    public Artifact(String scenario, String step, String keyword, String artifactFile, String contentType) {
        this.scenario = scenario;
        this.step = step;
        this.keyword = keyword;
        this.artifactFile = artifactFile;
        this.contentType = contentType;
    }

    public String getScenario() {
        return scenario;
    }

    public String getKeyword() {
        return keyword;
    }

    public String getStep() {
           return step;
       }

    public String getArtifactFile() {
        return artifactFile;
    }

    public String getContentType(){
        return contentType;
    }

}
