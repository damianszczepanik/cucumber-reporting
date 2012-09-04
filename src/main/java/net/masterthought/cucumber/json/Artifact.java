package net.masterthought.cucumber.json;

public class Artifact {

    private String scenario;
    private String step;
    private String keyword;
    private String artifactFile;
    private String contentType;

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
